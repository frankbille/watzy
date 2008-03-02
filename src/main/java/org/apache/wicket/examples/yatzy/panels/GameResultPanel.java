package org.apache.wicket.examples.yatzy.panels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.examples.yatzy.domain.IGame;
import org.apache.wicket.examples.yatzy.domain.IPlayer;
import org.apache.wicket.examples.yatzy.domain.score.IScoreCard;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class GameResultPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public GameResultPanel(String id, final IModel gameModel) {
		super(id, gameModel);

		// Find winner
		add(new Label("winnerPlayer", new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				IGame game = (IGame) gameModel.getObject();

				int maxScore = 0;
				IPlayer winner = null;

				for (IPlayer player : game.getPlayers()) {
					int score = game.getScoreCard().getScore(player);
					if (score > maxScore) {
						maxScore = score;
						winner = player;
					}
				}

				return winner.getName();
			}
		}));

		// List of players sorted by score with the biggest score first
		IModel model = new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				IGame game = (IGame) gameModel.getObject();
				final IScoreCard scoreCard = game.getScoreCard();

				List<IPlayer> players = game.getPlayers();

				Collections.sort(players, new Comparator<IPlayer>() {
					public int compare(IPlayer o1, IPlayer o2) {
						int score1 = scoreCard.getScore(o1);
						int score2 = scoreCard.getScore(o2);

						// Reverse the list
						return new Integer(score1).compareTo(score2) * -1;
					}
				});

				return players;
			}
		};
		add(new ListView("scoreResults", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {

				item.add(new Label("player", new PropertyModel(item.getModel(), "name")));
				item.add(new Label("score", new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						IGame game = (IGame) gameModel.getObject();
						IPlayer player = (IPlayer) item.getModelObject();

						return game.getScoreCard().getScore(player);
					}
				}));
			}
		});
	}
}
