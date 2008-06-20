package org.apache.wicket.examples.yatzy.frontend;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.Health;

public class MultiPlayerGameCleanup implements Runnable {

	private boolean run = true;
	private final YatzyApplication yatzyApplication;

	public MultiPlayerGameCleanup(YatzyApplication yatzyApplication) {
		this.yatzyApplication = yatzyApplication;
	}

	public void run() {
		while (run) {
			List<MultiPlayerGame> gamesToDelete = new ArrayList<MultiPlayerGame>();

			List<MultiPlayerGame> games = yatzyApplication.getAllGames();
			for (MultiPlayerGame multiPlayerGame : games) {
				GameStatus gameStatus = multiPlayerGame.getGameStatus();
				if (gameStatus == GameStatus.SETTING_UP || gameStatus == GameStatus.STARTED) {
					if (multiPlayerGame.getGameHealth() == Health.DEAD) {
						gamesToDelete.add(multiPlayerGame);
					}
				}
			}

			games.removeAll(gamesToDelete);

			// Wait
			for (int i = 0; i < 30 && run; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void stop() {
		run = false;
	}

}
