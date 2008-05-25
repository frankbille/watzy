package org.apache.wicket.examples.yatzy.panels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.score.IScoreCard;

public class GameResultPanel extends Panel<IGame> {
	private static final long serialVersionUID = 1L;

	public GameResultPanel(String id, final IModel<IGame> gameModel) {
		super(id, gameModel);

		// Find winner
		add(new Label<String>("winnerPlayer", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				IGame game = gameModel.getObject();

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
		IModel<List<IPlayer>> model = new AbstractReadOnlyModel<List<IPlayer>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<IPlayer> getObject() {
				IGame game = gameModel.getObject();
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
		add(new ListView<IPlayer>("scoreResults", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<IPlayer> item) {

				item.add(new Label<String>("player", new PropertyModel<String>(item.getModel(), "name")));
				item.add(new Label<Integer>("score", new AbstractReadOnlyModel<Integer>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject() {
						IGame game = gameModel.getObject();
						IPlayer player = item.getModelObject();

						return game.getScoreCard().getScore(player);
					}
				}));
			}
		});
	}
}
