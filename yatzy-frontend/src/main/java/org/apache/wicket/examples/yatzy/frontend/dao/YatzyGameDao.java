package org.apache.wicket.examples.yatzy.frontend.dao;

import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.examples.yatzy.IGame;

public interface YatzyGameDao {

	YatzyGame getYatzyGame(String gameKey);
	
	void saveYatzyGame(YatzyGame yatzyGame);

	YatzyGame createGame(IGame game);

}