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
import org.apache.wicket.examples.yatzy.frontend.persistence.HibernatePersistence;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

	private final List<MultiPlayerGame> games = new CopyOnWriteArrayList<MultiPlayerGame>();

	private MultiPlayerGameCleanup multiPlayerGameCleanup;

	@Override
	protected void init() {
		mountBookmarkablePage("/highscore", HighscorePage.class);
		mountBookmarkablePage("/newgame", NewGamePage.class);
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));

		multiPlayerGameCleanup = new MultiPlayerGameCleanup(this);
		new Thread(multiPlayerGameCleanup).start();
		
		getHighscores();
	}

	@Override
	protected void onDestroy() {
		multiPlayerGameCleanup.stop();
	}

	@Override
	public Class<? extends Page> getHomePage() {
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

	public List<MultiPlayerGame> getAllGames() {
		return games;
	}

	public void registerHighscore(IGame game, IPlayer player, int score) {
		Highscore highscore = new Highscore(game, player, score);
		HibernatePersistence.saveHighscore(highscore);
	}

	public List<Highscore> getHighscores() {
		return Collections.unmodifiableList(HibernatePersistence.getHighscores());
	}

}
