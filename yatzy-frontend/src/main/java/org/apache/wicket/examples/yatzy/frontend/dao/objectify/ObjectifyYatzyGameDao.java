package org.apache.wicket.examples.yatzy.frontend.dao.objectify;

import java.util.UUID;

import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.examples.yatzy.IGame;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyYatzyGameDao implements YatzyGameDao {

	private Objectify objectify;

	public ObjectifyYatzyGameDao() {
		ObjectifyService.register(YatzyGame.class);
		objectify = ObjectifyService.begin();
	}
	
	public YatzyGame createGame(IGame game) {
		YatzyGame yatzyGame = new YatzyGame();
		yatzyGame.setGame(game);
		yatzyGame.setKey(UUID.randomUUID().toString());
		objectify.put(yatzyGame);
		return yatzyGame;
	}

	public YatzyGame getYatzyGame(String gameKey) {
		return objectify.get(YatzyGame.class, gameKey);
	}
	
}
