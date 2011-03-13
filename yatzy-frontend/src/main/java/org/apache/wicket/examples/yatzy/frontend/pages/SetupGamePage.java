package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.MaxiGame;

public class SetupGamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public SetupGamePage(PageParameters pageParameters) {
		YatzyGameDao yatzyGameDao = YatzyApplication.get().getYatzyGameDao();
		
		StringValue gameKeyString = pageParameters.get(0);
		if (gameKeyString.isEmpty()) {
			MaxiGame maxiGame = new MaxiGame();
			YatzyGame yatzyGame = yatzyGameDao.createGame(maxiGame);
			PageParameters params = new PageParameters();
			params.set(0, yatzyGame.getKey());
			throw new RestartResponseException(SetupGamePage.class, params);
		}

		String gameKey = gameKeyString.toString();
		YatzyGame yatzyGame = yatzyGameDao.getYatzyGame(gameKey);
		
		yatzyGame.getGame().addPlayer(new AdhocPlayer("Frank"));
		yatzyGame.getGame().addPlayer(new AdhocPlayer("Bodil"));
		yatzyGame.setStarted(true);
		yatzyGameDao.saveYatzyGame(yatzyGame);

		PageParameters params = new PageParameters();
		params.set(0, yatzyGame.getKey());
		throw new RestartResponseException(GamePage.class, params);
	}
	
}
