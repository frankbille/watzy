package org.examples.yatzy;

import java.util.ArrayList;
import java.util.List;

import org.examples.yatzy.score.IScoreCard;

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

	public boolean isComplete() {
		return scoreCard.isComplete();
	}

	public IScoreCard getScoreCard() {
		if (scoreCard == null) {
			scoreCard = createScoreCard();
		}

		return scoreCard;
	}

	public final IGame newGame() {
		IGame game = createNewGame();

		for (IPlayer player : players) {
			game.addPlayer(player);
		}

		return game;
	}

	protected abstract IGame createNewGame();

	protected abstract int getRollsCount();

	protected abstract IDiceFactory getDiceFactory();

	protected abstract IScoreCard createScoreCard();

}
