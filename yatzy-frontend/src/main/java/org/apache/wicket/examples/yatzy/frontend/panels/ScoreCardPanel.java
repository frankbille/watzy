package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryDimensionsBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryHotkeyBehavior;
import org.apache.wicket.examples.yatzy.frontend.panels.ScoreSumPanel.SumProvider;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.score.AbstractStandardBonusScoreGroup;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.IScoreGroup;
import org.examples.yatzy.score.ITurnScore;

public abstract class ScoreCardPanel extends Panel<IScoreCard> {
	private static final long serialVersionUID = 1L;

	private final RepeatingView<Object> scores;

	private final IModel<ITurn> turnModel;

	public ScoreCardPanel(String id, final IModel<ITurn> turnModel,
			IModel<IScoreCard> scoreCardModel) {
		super(id, scoreCardModel);
		this.turnModel = turnModel;

		setOutputMarkupId(true);

		add(new JQueryDimensionsBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);

				response.renderJavascriptReference(new ResourceReference(ScoreCardPanel.class,
						"scoreCardNavigation.js"));
			}
		});

		add(new JQueryHotkeyBehavior(new Model<String>("a"), new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "prev('" + getMarkupId() + "');";
			}
		}, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component<?> component) {
				return ScoreCardPanel.this.isEnabled();
			}
		});

		add(new JQueryHotkeyBehavior(new Model<String>("s"), new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "next('" + getMarkupId() + "');";
			}
		}, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component<?> component) {
				return ScoreCardPanel.this.isEnabled();
			}
		});

		add(new JQueryHotkeyBehavior(new Model<String>("v"), new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "select('" + getMarkupId() + "');";
			}
		}, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component<?> component) {
				return ScoreCardPanel.this.isEnabled();
			}
		});

		PropertyModel<List<IPlayer>> playersModel = new PropertyModel<List<IPlayer>>(
				scoreCardModel, "players");

		// Header
		ListView<IPlayer> players = new ListView<IPlayer>("players", playersModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IPlayer> item) {
				final IPlayer player = item.getModelObject();
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						ITurn turn = turnModel.getObject();
						return turn != null && turn.getPlayer() == player ? "currentPlayer" : null;
					}
				}));
				item.add(new Label<String>("playerName", new PropertyModel<String>(item.getModel(),
						"name")).setRenderBodyOnly(true));
			}
		};
		add(players);

		// Body
		scores = new RepeatingView<Object>("scores");
		add(scores);

		addScores(scoreCardModel.getObject());

		// Footer
		add(new ListView<IPlayer>("playerTotals", playersModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IPlayer> item) {
				final IPlayer player = item.getModelObject();

				item.add(new AttributeAppender("class", true, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						ITurn turn = turnModel.getObject();
						return turn != null && turn.getPlayer() == player ? "currentPlayerGrandTotal"
								: null;
					}
				}, " "));

				IModel<String> model = new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						IScoreCard scoreCard = ScoreCardPanel.this.getModelObject();
						if (scoreCard.hasScore(player)) {
							return "" + scoreCard.getScore(player);
						} else {
							return "&nbsp;";
						}
					}
				};

				Label<String> totalLabel = new Label<String>("total", model);
				totalLabel.setRenderBodyOnly(true);
				totalLabel.setEscapeModelStrings(false);
				item.add(totalLabel);
			}
		});
	}

	private void addScores(IScoreGroup scoreGroup) {
		for (IScore score : scoreGroup.getScores()) {
			if (score instanceof ITurnScore) {
				WebMarkupContainer<Object> scoreContainer = new WebMarkupContainer<Object>(scores
						.newChildId());
				scores.add(scoreContainer);

				ITurnScore turnScore = (ITurnScore) score;
				ScorePanel scorePanel = new ScorePanel("scorePanel", turnModel,
						new Model<ITurnScore>(turnScore)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void combinationSelected(AjaxRequestTarget target,
							IModel<ITurnScore> scoreModel) {
						ScoreCardPanel.this.combinationSelected(target, scoreModel);

						if (target != null) {
							target.addComponent(ScoreCardPanel.this);
						}
					}

					@Override
					protected boolean combinationSelectable(IModel<ITurnScore> scoreModel) {
						return ScoreCardPanel.this.combinationSelectable(scoreModel);
					}
				};
				scorePanel.setRenderBodyOnly(true);
				scoreContainer.add(scorePanel);
			} else if (score instanceof IScoreGroup) {
				IScoreGroup childScoreGroup = (IScoreGroup) score;

				addScores(childScoreGroup);

				if (childScoreGroup instanceof AbstractStandardBonusScoreGroup) {
					final AbstractStandardBonusScoreGroup bonusScoreGroup = (AbstractStandardBonusScoreGroup) childScoreGroup;

					WebMarkupContainer<Object> scoreContainer = new WebMarkupContainer<Object>(
							scores.newChildId());
					scores.add(scoreContainer);

					ScoreSumPanel scoreSumPanel = new ScoreSumPanel("scorePanel", turnModel,
							new StringResourceModel("bonus", this, null), new Model<IScoreGroup>(
									childScoreGroup), new SumProvider() {
								private static final long serialVersionUID = 1L;

								public int getSum(IScoreGroup scoreGroup, IPlayer player) {
									return bonusScoreGroup.getBonus(player);
								}

								public boolean hasSum(IScoreGroup scoreGroup, IPlayer player) {
									return bonusScoreGroup.isBonusAvailable(player);
								}
							});
					scoreSumPanel.setRenderBodyOnly(true);
					scoreContainer.add(scoreSumPanel);
				}

				WebMarkupContainer<Object> scoreContainer = new WebMarkupContainer<Object>(scores
						.newChildId());
				scores.add(scoreContainer);

				ScoreSumPanel scoreSumPanel = new ScoreSumPanel("scorePanel", turnModel,
						new StringResourceModel("total", this, null), new Model<IScoreGroup>(
								childScoreGroup), new SumProvider() {
							private static final long serialVersionUID = 1L;

							public int getSum(IScoreGroup scoreGroup, IPlayer player) {
								return scoreGroup.getScore(player);
							}

							public boolean hasSum(IScoreGroup scoreGroup, IPlayer player) {
								return scoreGroup.hasScore(player);
							}
						});
				scoreSumPanel.setRenderBodyOnly(true);
				scoreContainer.add(scoreSumPanel);
			}
		}
	}

	protected abstract void combinationSelected(AjaxRequestTarget target,
			IModel<ITurnScore> scoreModel);

	protected boolean combinationSelectable(IModel<ITurnScore> scoreModel) {
		return true;
	}

	public IModel<ITurn> getTurnModel() {
		return turnModel;
	}

}
