package org.apache.wicket.examples.yatzy.frontend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.wicket.Page;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.examples.yatzy.frontend.pages.HighscorePage;
import org.apache.wicket.examples.yatzy.frontend.pages.NewGamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.MaxiGame;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

	private final List<MultiPlayerGame> games = new CopyOnWriteArrayList<MultiPlayerGame>();

	private List<Highscore> highscores;

	@Override
	protected void init() {
		mountBookmarkablePage("/highscore", HighscorePage.class);
		mountBookmarkablePage("/newgame", NewGamePage.class);
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));

		resetHighscores();
	}

	@Override
	public Class<? extends Page<?>> getHomePage() {
		return NewGamePage.class;
	}

	public void addGame(MultiPlayerGame game) {
		games.add(game);
	}

	public List<MultiPlayerGame> getAvailableGames() {
		List<MultiPlayerGame> availableGames = new ArrayList<MultiPlayerGame>();

		for (MultiPlayerGame multiPlayerGame : games) {
			if (multiPlayerGame.getGameStatus() == GameStatus.SETTING_UP) {
				availableGames.add(multiPlayerGame);
			}
		}

		return availableGames;
	}

	public void resetHighscores() {
		highscores = new ArrayList<Highscore>();

		highscores.add(new Highscore(new MaxiGame(), new AdhocPlayer("Frank"), 140));
		highscores.add(new Highscore(new MaxiGame(), new AdhocPlayer("Frank"), 130));
		highscores.add(new Highscore(new MaxiGame(), new AdhocPlayer("Frank"), 120));
		highscores.add(new Highscore(new MaxiGame(), new AdhocPlayer("Frank"), 110));
		highscores.add(new Highscore(new MaxiGame(), new AdhocPlayer("Frank"), 100));
	}

	public void registerHighscore(IGame game, IPlayer player, int score) {
		highscores.add(new Highscore(game, player, score));

		Collections.sort(highscores);
	}

	public List<Highscore> getHighscores() {
		return Collections.unmodifiableList(highscores);
	}

}
