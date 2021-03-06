package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerTurn;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryHotkeyBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryScrollToBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IDice;
import org.examples.yatzy.IRollTurn;
import org.examples.yatzy.ITurn;

public class TurnPanel extends GenericPanel<ITurn> {
	private static final long serialVersionUID = 1L;
	private final AjaxFallbackLink<ITurn> rollLink;
	private final Label turnLabel;

	public TurnPanel(String id, IModel<ITurn> turnModel) {
		super(id, turnModel);

		final ListView<IDice> diceList = new ListView<IDice>("diceList", new PropertyModel<List<IDice>>(turnModel,
				"diceList")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<IDice> item) {
				item.setOutputMarkupId(true);

				final IModel<String> holdModel = new StringResourceModel("hold", TurnPanel.this, null);
				final Label holdLabel = new Label("hold", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						IDice dice = item.getModelObject();
						ITurn turn = TurnPanel.this.getModelObject();

						String label = null;

						if (turn.shouldHold(dice)) {
							label = holdModel.getObject();
						} else {
							label = "&nbsp;";
						}

						return label;
					}

				});
				holdLabel.setEscapeModelStrings(false);
				holdLabel.setOutputMarkupId(true);
				item.add(holdLabel);

				final AjaxFallbackLink<ITurn> holdLink = new AjaxFallbackLink<ITurn>("holdLink", TurnPanel.this
						.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						IDice dice = item.getModelObject();
						ITurn turn = getModelObject();
						turn.changeHold(dice);

						if (target != null) {
							target.addComponent(holdLabel);
						}
					}

					@Override
					public boolean isEnabled() {
						ITurn turn = getModelObject();
						if (turn != null) {
							IDice dice = item.getModelObject();
							return dice.hasValue() && TurnPanel.this.isEnabled() && turn.mayRoll();
						} else {
							return false;
						}
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
				holdLink.setOutputMarkupId(true);
				holdLink.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						String className;
						IDice dice = item.getModelObject();

						if (dice.hasValue()) {
							className = "dice_" + dice.getValue();
						} else {
							className = "empty";
						}

						return className;
					}
				}));
				holdLink.add(new JQueryHotkeyBehavior(new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return "" + (item.getIndex() + 1);
					}
				}, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return "$('#" + holdLink.getMarkupId() + "').click();";
					}
				}, true) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled(Component component) {
						return component.isEnabled();
					}
				});
				item.add(holdLink);
			}
		};
		add(diceList);

		rollLink = new AjaxFallbackLink<ITurn>("rollLink", turnModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ITurn turn = getModelObject();
				turn.roll();

				if (target != null) {
					target.addComponent(TurnPanel.this);
				}
			}

			@Override
			public boolean isEnabled() {
				ITurn turn = getModelObject();
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

		rollLink.add(new JQueryHotkeyBehavior(new Model<String>("r"), new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "$('#" + rollLink.getMarkupId() + "').click();";
			}
		}, true));
		add(rollLink);

		rollLink.add(new Image("rollImage", "roll_white.png"));

		IModel<ITurn> rollsLeftModel = new AbstractReadOnlyModel<ITurn>() {
			private static final long serialVersionUID = 1L;

			@Override
			public ITurn getObject() {
				ITurn turn = TurnPanel.this.getModelObject();

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
				ITurn turn = TurnPanel.this.getModelObject();

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
