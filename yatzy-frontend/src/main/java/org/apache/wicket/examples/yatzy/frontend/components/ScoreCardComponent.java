package org.apache.wicket.examples.yatzy.frontend.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.score.IBonusScoreGroup;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.IScoreGroup;
import org.examples.yatzy.score.SumScore;

public class ScoreCardComponent extends Panel {
	private static final long serialVersionUID = 1L;

	protected static class ScoreRow implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String labelKey;
		private Map<IPlayer, Integer> scores = new HashMap<IPlayer, Integer>();
		
		public String getLabelKey() {
			return labelKey;
		}
		
		public void setLabelKey(String labelKey) {
			this.labelKey = labelKey;
		}
		
		public void addScore(IPlayer player, int score) {
			scores.put(player, score);
		}
		
		public boolean hasScore(IPlayer player) {
			return scores.containsKey(player);
		}
		
		public int getScore(IPlayer player) {
			if (hasScore(player) == false) {
				throw new IllegalStateException("Can only get the score if the player has one");
			}
			
			return scores.get(player);
		}
	}
	
	private static class ScoreRowModel extends AbstractReadOnlyModel<List<ScoreRow>> {
		private static final long serialVersionUID = 1L;
		
		private final IModel<IScoreCard> scoreCardModel;

		public ScoreRowModel(IModel<IScoreCard> scoreCardModel) {
			this.scoreCardModel = scoreCardModel;
		}
		
		@Override
		public List<ScoreRow> getObject() {
			List<ScoreRow> rows = new ArrayList<ScoreRow>();
			
			IScoreCard scoreCard = scoreCardModel.getObject();
			
			traverseScores(rows, scoreCard);
			
			return rows;
		}
		
		private void traverseScores(List<ScoreRow> rows, IScore score) {
			ScoreRow scoreRow = new ScoreRow();

			if (score instanceof IScoreGroup) {
				IScoreGroup scoreGroup = (IScoreGroup) score;
				
				List<IScore> scores = scoreGroup.getScores();
				for (IScore childScore : scores) {
					traverseScores(rows, childScore);
				}
				
				if (scoreGroup instanceof IBonusScoreGroup) {
					IBonusScoreGroup bonusScoreGroup = (IBonusScoreGroup) scoreGroup;
					ScoreRow bonusScoreRow = new ScoreRow();
					bonusScoreRow.setLabelKey("bonus");
					for (IPlayer player : bonusScoreGroup.getPlayers()) {
						if (bonusScoreGroup.isBonusAvailable(player)) {
							scoreRow.addScore(player, bonusScoreGroup.getBonus(player));
						}
					}
					rows.add(bonusScoreRow);
				}
				
				if (scoreGroup instanceof IScoreCard) {
					scoreRow.setLabelKey("grandTotal");
				} else {
					scoreRow.setLabelKey("total");
				}
			} else {
				String labelKey = score.getClass().getSimpleName();

				if (score instanceof SumScore) {
					SumScore sumScore = (SumScore) score;
					labelKey += "."+sumScore.getValue();
				}
				
				scoreRow.setLabelKey(labelKey);
			}
			
			for (IPlayer player : score.getPlayers()) {
				if (score.hasScore(player)) {
					scoreRow.addScore(player, score.getScore(player));
				}
			}
			rows.add(scoreRow);
		}
		
		@Override
		public void detach() {
			scoreCardModel.detach();
		}
	}
	
	private static class PlayersModel extends AbstractReadOnlyModel<List<IPlayer>> {
		private static final long serialVersionUID = 1L;

		private final IModel<IGame> gameModel;

		public PlayersModel(IModel<IGame> gameModel) {
			this.gameModel = gameModel;
		}
		
		@Override
		public List<IPlayer> getObject() {
			return gameModel.getObject().getPlayers();
		}
		
		@Override
		public void detach() {
			gameModel.detach();
		}
	}
	
	private static class ScoreModel extends AbstractReadOnlyModel<String> {
		private static final long serialVersionUID = 1L;
		
		private final ScoreRow scoreRow;
		private final IPlayer player;

		public ScoreModel(ScoreRow scoreRow, IPlayer player) {
			this.scoreRow = scoreRow;
			this.player = player;
		}
		
		@Override
		public String getObject() {
			String value = null;
			
			if (scoreRow.hasScore(player)) {
				value = "" + scoreRow.getScore(player);
			}
			
			return value;
		}
	}
	
	public ScoreCardComponent(String id, IModel<IGame> gameModel) {
		super(id);
		
		final PlayersModel playersModel = new PlayersModel(gameModel);

		// Players
		add(new ListView<IPlayer>("playerNames", playersModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<IPlayer> item) {
				item.add(new Label("name", new PropertyModel<String>(item.getModelObject(), "name")));
			}
		});
		
		// Scores
		add(new ListView<ScoreRow>("scores", new ScoreRowModel(new PropertyModel<IScoreCard>(gameModel, "scoreCard"))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ScoreRow> item) {
				final ScoreRow scoreRow = item.getModelObject();
				item.add(new Label("scoreLabel", new StringResourceModel(scoreRow.getLabelKey(), this, null)));
				
				item.add(new ListView<IPlayer>("playerScores", playersModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<IPlayer> item) {
						item.add(new Label("score", new ScoreModel(scoreRow, item.getModelObject())));
					}
				});
			}
		});
	}

}
