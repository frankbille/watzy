package org.apache.wicket.examples.yatzy.frontend;

import java.util.List;

import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.examples.yatzy.IDice;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;

public class MultiPlayerTurn implements ITurn {
	private static final long serialVersionUID = 1L;

	private final ITurn turn;
	private final MultiPlayerGame multiPlayerGame;

	public MultiPlayerTurn(ITurn turn, MultiPlayerGame multiPlayerGame) {
		this.turn = turn;
		this.multiPlayerGame = multiPlayerGame;
	}

	public void changeHold(IDice dice) {
		turn.changeHold(dice);
	}

	public List<IDice> getDiceList() {
		return turn.getDiceList();
	}

	public IPlayer getPlayer() {
		return turn.getPlayer();
	}

	public boolean hasValue() {
		return turn.hasValue();
	}

	public boolean mayRoll() {
		return multiPlayerGame.getGameStatus() == GameStatus.STARTED
				&& multiPlayerGame.isPlayingFromThisSeat(getPlayer()) && turn.mayRoll();
	}

	public void roll() {
		turn.roll();
	}

	public boolean shouldHold(IDice dice) {
		return turn.shouldHold(dice);
	}

	public ITurn getInnerTurn() {
		return turn;
	}

}
