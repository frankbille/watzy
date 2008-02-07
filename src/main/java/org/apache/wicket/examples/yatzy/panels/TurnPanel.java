package org.apache.wicket.examples.yatzy.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.examples.yatzy.domain.IDice;
import org.apache.wicket.examples.yatzy.domain.ITurn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class TurnPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private final AjaxFallbackLink rollLink;
	private final Label turnLabel;

	public TurnPanel(String id, IModel model) {
		super(id, model);

		final ListView diceList = new ListView("diceList", new PropertyModel(model, "diceList")) {
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
						IDice dice = (IDice) item.getModelObject();
						return dice.hasValue();
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
					target.addChildren(diceList, ListItem.class);
					target.addComponent(rollLink);
					target.addComponent(turnLabel);
				}
			}

			@Override
			public boolean isEnabled() {
				ITurn turn = (ITurn) TurnPanel.this.getModelObject();
				return turn.mayRoll();
			}
		};
		rollLink.setOutputMarkupId(true);
		add(rollLink);

		rollLink.add(new Image("rollImage", "roll.png"));

		// Label rollLabel = new Label("label", new StringResourceModel("roll",
		// this, null));
		// rollLabel.setRenderBodyOnly(true);
		// rollLink.add(rollLabel);

		turnLabel = new Label("turnLabel",
				new StringResourceModel("rollsLeft", this, new PropertyModel(model, "rules"))) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return false;
			}
		};
		turnLabel.setOutputMarkupId(true);
		add(turnLabel);
	}

}
