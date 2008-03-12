package org.examples.yatzy;

import java.util.List;

public class StandardRound implements IRound {
	private static final long serialVersionUID = 1L;

	private final List<IPlayer> players;

	private int currentPlayer = -1;

	private final IDiceFactory diceFactory;

	private final int rolls;

	public StandardRound(List<IPlayer> players, IDiceFactory diceFactory, int rolls) {
		this.diceFactory = diceFactory;
		this.rolls = rolls;
		this.players = players;
	}

	public IPlayer getCurrentPlayer() {
		return players.get(currentPlayer);
	}

	public boolean hasMoreTurns() {
		return players.isEmpty() == false && currentPlayer + 1 < players.size();
	}

	public ITurn nextTurn() {
		currentPlayer++;
		return new StandardTurn(diceFactory.createDiceList(), rolls, getCurrentPlayer());
	}

}
