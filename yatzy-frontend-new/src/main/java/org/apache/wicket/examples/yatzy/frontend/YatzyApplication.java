package org.apache.wicket.examples.yatzy.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.wicket.Page;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.examples.yatzy.frontend.pages.TestFrontPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

	private final List<MultiPlayerGame> games = new CopyOnWriteArrayList<MultiPlayerGame>();

	@Override
	protected void init() {
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));
	}

	@Override
	public Class<? extends Page<?>> getHomePage() {
		return TestFrontPage.class;
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

}
