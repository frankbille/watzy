package org.apache.wicket.examples.yatzy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.examples.yatzy.pages.EnterNamePage;
import org.apache.wicket.examples.yatzy.pages.GamePage;
import org.apache.wicket.examples.yatzy.pages.HighscorePage;
import org.apache.wicket.examples.yatzy.pages.NewGamePage;
import org.apache.wicket.examples.yatzy.pages.multi.MultiPlayerGamePage;
import org.apache.wicket.examples.yatzy.pages.multi.MultiPlayerGameSetupPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.examples.yatzy.IGame;
import org.examples.yatzy.MaxiGame;

public class YatzyApplication extends WebApplication {

	private List<Highscore> highscores;

	private final List<MultiPlayerGame> multiPlayerGames = new ArrayList<MultiPlayerGame>();

	@Override
	protected void init() {
		getSecuritySettings().setAuthorizationStrategy(new YatzyAuthorizationStrategy());

		mountBookmarkablePage("/newgame", NewGamePage.class);
		mountBookmarkablePage("/highscore", HighscorePage.class);
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));
		mount(new HybridUrlCodingStrategy("/multigame", MultiPlayerGamePage.class, true));
		mountBookmarkablePage("/multi", MultiPlayerGameSetupPage.class);
		mountBookmarkablePage("/entername", EnterNamePage.class);

		resetHighscores();

		addMultiPlayerGame(new MultiPlayerGame(2, new MaxiGame()));
		// addMultiPlayerGame(new MultiPlayerGame(4, new MaxiGame()));
		// addMultiPlayerGame(new MultiPlayerGame(2, new StandardGame()));
	}

	@Override
	public Class<?> getHomePage() {
		return NewGamePage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new YatzySession(request);
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

	public void addMultiPlayerGame(MultiPlayerGame multiPlayerGame) {
		synchronized (multiPlayerGames) {
			multiPlayerGames.add(multiPlayerGame);
		}
	}

	public void removeMultiPlayerGame(MultiPlayerGame multiPlayerGame) {
		synchronized (multiPlayerGames) {
			multiPlayerGames.remove(multiPlayerGame);
		}
	}

	public List<MultiPlayerGame> getMultiPlayerGames() {
		synchronized (multiPlayerGames) {
			return Collections.unmodifiableList(multiPlayerGames);
		}
	}

}
