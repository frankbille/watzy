package org.apache.wicket.examples.yatzy.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.examples.yatzy.domain.score.IScoreCard;

public abstract class AbstractStandardGame implements IGame {

	private final List<IPlayer> players = new ArrayList<IPlayer>();
	private IScoreCard scoreCard;

	public List<IPlayer> getPlayers() {
		return players;
	}

	public void addPlayer(IPlayer player) {
		if (players.contains(player) == false) {
			players.add(player);

			getScoreCard().addPlayer(player);
		}
	}

	public IRound newRound() {
		return new StandardRound(players, getDiceFactory(), getRollsCount());
	}

	public IScoreCard getScoreCard() {
		if (scoreCard == null) {
			scoreCard = createScoreCard();
		}

		return scoreCard;
	}

	protected abstract int getRollsCount();

	protected abstract IDiceFactory getDiceFactory();

	protected abstract IScoreCard createScoreCard();

}
