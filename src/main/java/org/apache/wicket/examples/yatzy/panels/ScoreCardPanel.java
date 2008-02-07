package org.apache.wicket.examples.yatzy.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.examples.yatzy.domain.IPlayer;
import org.apache.wicket.examples.yatzy.domain.ITurn;
import org.apache.wicket.examples.yatzy.domain.score.IScore;
import org.apache.wicket.examples.yatzy.domain.score.IScoreCard;
import org.apache.wicket.examples.yatzy.domain.score.IScoreGroup;
import org.apache.wicket.examples.yatzy.domain.score.ITurnScore;
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

public abstract class ScoreCardPanel extends Panel {

	private final IModel turnModel;

	public ScoreCardPanel(String id, final IModel turnModel, IModel model) {
		super(id, model);
		this.turnModel = turnModel;

		setOutputMarkupId(true);

		// Header
		add(new ListView("players", new PropertyModel(model, "players")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final IPlayer player = (IPlayer) item.getModelObject();
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						ITurn turn = (ITurn) turnModel.getObject();
						return turn.getPlayer() == player ? "currentPlayer" : null;
					}
				}));
				item.add(new Label("playerName", new PropertyModel(item.getModel(), "name")).setRenderBodyOnly(true));
			}
		});

		// Body
		RepeatingView scores = new RepeatingView("scores");
		add(scores);

		addScores(scores, (IScoreGroup) model.getObject());

		// Footer
		add(new ListView("playerTotals", new PropertyModel(model, "players")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final IPlayer player = (IPlayer) item.getModelObject();

				item.add(new AttributeAppender("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						ITurn turn = (ITurn) turnModel.getObject();
						return turn.getPlayer() == player ? "currentPlayerGrandTotal" : null;
					}
				}, " "));

				IModel model = new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						IScoreCard scoreCard = (IScoreCard) ScoreCardPanel.this.getModelObject();
						return scoreCard.getScore(player);
					}
				};

				item.add(new Label("total", model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						IScoreCard scoreCard = (IScoreCard) ScoreCardPanel.this.getModelObject();
						return scoreCard.hasScore(player);
					}
				});
			}
		});
	}

	private void addScores(RepeatingView scores, IScoreGroup scoreGroup) {
		for (IScore score : scoreGroup.getScores()) {
			if (score instanceof ITurnScore) {
				WebMarkupContainer scoreContainer = new WebMarkupContainer(scores.newChildId());
				scores.add(scoreContainer);

				ITurnScore turnScore = (ITurnScore) score;
				ScorePanel scorePanel = new ScorePanel("scorePanel", turnModel, new Model(turnScore)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void combinationSelected(AjaxRequestTarget target, IModel scoreModel) {
						ScoreCardPanel.this.combinationSelected(target, scoreModel);

						if (target != null) {
							target.addComponent(ScoreCardPanel.this);
						}
					}
				};
				scorePanel.setRenderBodyOnly(true);
				scoreContainer.add(scorePanel);
			} else if (score instanceof IScoreGroup) {
				IScoreGroup childScoreGroup = (IScoreGroup) score;

				addScores(scores, childScoreGroup);

				WebMarkupContainer scoreContainer = new WebMarkupContainer(scores.newChildId());
				scores.add(scoreContainer);

				ScoreSumPanel scoreSumPanel = new ScoreSumPanel("scorePanel", turnModel, new StringResourceModel(
						"total", this, null), new Model(childScoreGroup));
				scoreSumPanel.setRenderBodyOnly(true);
				scoreContainer.add(scoreSumPanel);
			}
		}
	}

	protected abstract void combinationSelected(AjaxRequestTarget target, IModel scoreModel);

}
