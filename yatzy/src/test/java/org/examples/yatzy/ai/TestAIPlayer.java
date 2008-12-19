package org.examples.yatzy.ai;

import org.examples.yatzy.GamePrinter;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.MaxiGame;
import org.junit.Test;

public class TestAIPlayer {

	@Test
	public void testAiGame() {
		MaxiYatzyAIPlayer aiPlayer = new MaxiYatzyAIPlayer();
		aiPlayer.setName("AI1");

		IGame game = new MaxiGame();
		game.addPlayer(aiPlayer);

		while (game.isComplete() == false) {
			IRound round = game.newRound();
			while (round.hasMoreTurns()) {
				ITurn turn = round.nextTurn();
				aiPlayer.handleTurn(turn, game);
			}
		}

		GamePrinter.printGame(game);

	}

}
