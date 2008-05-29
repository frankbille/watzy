package org.apache.wicket.examples.yatzy.frontend;

import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;

public class MultiPlayerRound implements IRound {
	private static final long serialVersionUID = 1L;

	private final IRound round;
	private transient final MultiPlayerGame multiPlayerGame;
	private MultiPlayerTurn currentTurn;

	public MultiPlayerRound(IRound round, MultiPlayerGame multiPlayerGame) {
		this.round = round;
		this.multiPlayerGame = multiPlayerGame;
	}

	public IPlayer getCurrentPlayer() {
		return round.getCurrentPlayer();
	}

	public boolean hasMoreTurns() {
		return round.hasMoreTurns();
	}

	public ITurn nextTurn() {
		currentTurn = new MultiPlayerTurn(round.nextTurn(), multiPlayerGame);
		return currentTurn;
	}

	public MultiPlayerTurn getCurrentTurn() {
		return currentTurn;
	}

}
