package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.components.ChannelComponent;
import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

public class GamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public GamePage(PageParameters pageParameters) {
		YatzyGameDao yatzyGameDao = YatzyApplication.get().getYatzyGameDao();

		StringValue gameKeyString = pageParameters.get(0);
		if (gameKeyString.isEmpty()) {
			throw new RestartResponseException(SetupGamePage.class);
		}

		String gameKey = gameKeyString.toString();
		YatzyGame yatzyGame = yatzyGameDao.getYatzyGame(gameKey);

		// If the game hasn't been setup yet go to the setup page
		if (false == yatzyGame.isStarted()) {
			PageParameters params = new PageParameters();
			params.set(0, yatzyGame.getKey());
			throw new RestartResponseException(SetupGamePage.class, params);
		}

		add(new ChannelComponent("channelComponent", gameKey));

		// Score card
		// add(new ScoreCardComponent("scorecard", new
		// PropertyModel<IGame>(yatzyGame, "game")));
	}

}
