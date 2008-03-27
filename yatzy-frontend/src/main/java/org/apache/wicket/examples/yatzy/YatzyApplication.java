package org.apache.wicket.examples.yatzy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.wicket.examples.yatzy.pages.GamePage;
import org.apache.wicket.examples.yatzy.pages.HighscorePage;
import org.apache.wicket.examples.yatzy.pages.NewGamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.examples.yatzy.IGame;

public class YatzyApplication extends WebApplication {

	public static class Highscore implements Comparable<Highscore> {
		private final Class<? extends IGame> gameType;
		private final String name;
		private final int score;
		private final Date timestamp;

		public Highscore(Class<? extends IGame> gameType, String name, int score) {
			this.gameType = gameType;
			this.name = name;
			this.score = score;
			timestamp = new Date();
		}

		public Class<? extends IGame> getGameType() {
			return gameType;
		}

		public String getName() {
			return name;
		}

		public int getScore() {
			return score;
		}

		public Date getTimestamp() {
			return timestamp;
		}

		public int compareTo(Highscore o) {
			int compare = 0;

			if (o != null) {
				compare = gameType.getName().compareTo(o.getGameType().getName());

				if (compare == 0) {
					compare = -1 * new Integer(score).compareTo(o.getScore());

					if (compare == 0) {
						compare = timestamp.compareTo(o.getTimestamp());
					}
				}
			} else {
				compare = -1;
			}

			return compare;
		}
	}

	private List<Highscore> highscores;

	@Override
	protected void init() {
		mountBookmarkablePage("/newgame", NewGamePage.class);
		mountBookmarkablePage("/highscore", HighscorePage.class);
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));

		resetHighscores();
	}

	@Override
	public Class<?> getHomePage() {
		return NewGamePage.class;
	}

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

	public synchronized void resetHighscores() {
		highscores = new ArrayList<Highscore>();
	}

	public synchronized void registerHighscore(Class<? extends IGame> gameType, String name, int score) {
		highscores.add(new Highscore(gameType, name, score));

		Collections.sort(highscores);
	}

	public List<Highscore> getHighscores() {
		return Collections.unmodifiableList(highscores);
	}

}
