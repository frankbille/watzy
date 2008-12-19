package org.apache.wicket.examples.yatzy.frontend;

import java.util.List;

import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.examples.yatzy.IDice;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.AIPlayer;

public class MultiPlayerTurn implements ITurn {
	private static final long serialVersionUID = 1L;

	private final ITurn turn;
	private transient final MultiPlayerGame multiPlayerGame;

	public MultiPlayerTurn(ITurn turn, MultiPlayerGame multiPlayerGame) {
		this.turn = turn;
		this.multiPlayerGame = multiPlayerGame;

		if (this.turn.getPlayer() instanceof AIPlayer) {
			final AIPlayer aiPlayer = (AIPlayer) this.turn.getPlayer();
			Runnable aiTurn = new Runnable() {
				public void run() {
					try {
						// Thread.sleep(500);

						aiPlayer.handleTurn(MultiPlayerTurn.this.turn, MultiPlayerTurn.this.multiPlayerGame);

						if (MultiPlayerTurn.this.multiPlayerGame.isComplete() == false) {
							IRound currentRound = MultiPlayerTurn.this.multiPlayerGame.getCurrentRound();
							if (currentRound.hasMoreTurns() == false) {
								currentRound = MultiPlayerTurn.this.multiPlayerGame.newRound();
							}
							currentRound.nextTurn();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(aiTurn).start();
		}
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
				&& multiPlayerGame.isPlayingFromThisSeat(getPlayer()) && turn.mayRoll()
				&& getPlayer() instanceof AIPlayer == false;
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
