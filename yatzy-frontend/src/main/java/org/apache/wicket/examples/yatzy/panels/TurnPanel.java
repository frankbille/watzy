package org.apache.wicket.examples.yatzy.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.examples.yatzy.MultiPlayerTurn;
import org.apache.wicket.examples.yatzy.behaviours.jquery.JQueryScrollToBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IDice;
import org.examples.yatzy.IRollTurn;
import org.examples.yatzy.ITurn;

public class TurnPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private final AjaxFallbackLink rollLink;
	private final Label turnLabel;

	public TurnPanel(String id, IModel turnModel) {
		super(id, turnModel);

		final ListView diceList = new ListView("diceList", new PropertyModel(turnModel, "diceList")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				item.setOutputMarkupId(true);

				final IModel holdModel = new StringResourceModel("hold", TurnPanel.this, null);
				final Label holdLabel = new Label("hold", new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						IDice dice = (IDice) item.getModelObject();
						ITurn turn = (ITurn) TurnPanel.this.getModelObject();

						Object object = null;

						if (turn.shouldHold(dice)) {
							object = holdModel.getObject();
						} else {
							object = "&nbsp;";
						}

						return object;
					}

				});
				holdLabel.setEscapeModelStrings(false);
				holdLabel.setOutputMarkupId(true);
				item.add(holdLabel);

				AjaxFallbackLink holdLink = new AjaxFallbackLink("holdLink") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						IDice dice = (IDice) item.getModelObject();
						ITurn turn = (ITurn) TurnPanel.this.getModelObject();
						turn.changeHold(dice);

						if (target != null) {
							target.addComponent(holdLabel);
						}
					}

					@Override
					public boolean isEnabled() {
						ITurn turn = (ITurn) TurnPanel.this.getModelObject();
						IDice dice = (IDice) item.getModelObject();
						return dice.hasValue() && TurnPanel.this.isEnabled() && turn.mayRoll();
					}

					@Override
					public String getBeforeDisabledLink() {
						return null;
					}

					@Override
					public String getAfterDisabledLink() {
						return null;
					}
				};
				holdLink.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						String className;
						IDice dice = (IDice) item.getModelObject();

						if (dice.hasValue()) {
							className = "dice_" + dice.getValue();
						} else {
							className = "empty";
						}

						return className;
					}
				}));
				item.add(holdLink);
			}
		};
		add(diceList);

		rollLink = new AjaxFallbackLink("rollLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ITurn turn = (ITurn) TurnPanel.this.getModelObject();
				turn.roll();

				if (target != null) {
					target.addComponent(TurnPanel.this);
				}
			}

			@Override
			public boolean isEnabled() {
				ITurn turn = (ITurn) TurnPanel.this.getModelObject();
				return turn.mayRoll() && TurnPanel.this.isEnabled();
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxCallDecorator() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence decorateScript(CharSequence script) {
						StringBuilder b = new StringBuilder();
						b.append(script);
						b.append("$.scrollTo($(body), 0);");
						return b;
					}
				};
			}
		};
		rollLink.add(new JQueryScrollToBehavior());
		rollLink.setOutputMarkupId(true);
		add(rollLink);

		rollLink.add(new Image("rollImage", "roll.png"));

		IModel rollsLeftModel = new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				ITurn turn = (ITurn) TurnPanel.this.getModelObject();

				if (turn instanceof MultiPlayerTurn) {
					turn = ((MultiPlayerTurn) turn).getInnerTurn();
				}

				return turn;
			}
		};

		turnLabel = new Label("turnLabel", new StringResourceModel("rollsLeft", this, rollsLeftModel)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				ITurn turn = (ITurn) TurnPanel.this.getModelObject();

				if (turn instanceof MultiPlayerTurn) {
					turn = ((MultiPlayerTurn) turn).getInnerTurn();
				}

				return turn instanceof IRollTurn;
			}
		};
		turnLabel.setOutputMarkupId(true);
		add(turnLabel);
	}
}
