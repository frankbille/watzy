package org.apache.wicket.examples.yatzy;

import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;

public class MultiPlayerRound implements IRound {
	private static final long serialVersionUID = 1L;

	private final IRound round;
	private final MultiPlayerGame multiPlayerGame;
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
		MultiPlayerTurn turn = null;

		if (currentTurn != null) {
			if (multiPlayerGame.isPlaying() == false) {
				turn = currentTurn;
			}
		}

		if (turn == null) {
			turn = new MultiPlayerTurn(round.nextTurn(), multiPlayerGame);
		}

		currentTurn = turn;

		return turn;
	}

	public MultiPlayerTurn getCurrentTurn() {
		return currentTurn;
	}

}
