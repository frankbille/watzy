package org.apache.wicket.examples.yatzy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.examples.yatzy.IGame;

public class InMemoryYatzyGameDao implements YatzyGameDao {

	private Map<String, YatzyGame> games = new HashMap<String, YatzyGame>();
	
	@Override
	public YatzyGame getYatzyGame(String gameKey) {
		return games.get(gameKey);
	}

	@Override
	public YatzyGame createGame(IGame game) {
		YatzyGame yatzyGame = new YatzyGame();
		yatzyGame.setGame(game);
		yatzyGame.setKey(UUID.randomUUID().toString());
		games.put(yatzyGame.getKey(), yatzyGame);
		return yatzyGame;
	}

}
