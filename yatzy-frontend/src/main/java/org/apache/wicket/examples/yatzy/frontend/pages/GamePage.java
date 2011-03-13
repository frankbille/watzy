package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.components.ChannelComponent;
import org.apache.wicket.examples.yatzy.frontend.components.ScoreCardComponent;
import org.apache.wicket.examples.yatzy.frontend.components.ChannelComponent.ChannelUpdatedPayload;
import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.models.YatzyGame;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.examples.yatzy.IGame;

public class GamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public GamePage(PageParameters pageParameters) {
		YatzyGameDao yatzyGameDao = YatzyApplication.get().getYatzyGameDao();

		StringValue gameKeyString = pageParameters.get(0);
		if (gameKeyString.isEmpty()) {
			throw new RestartResponseException(SetupGamePage.class);
		}

		final String gameKey = gameKeyString.toString();
		YatzyGame yatzyGame = yatzyGameDao.getYatzyGame(gameKey);
		
		if (yatzyGame == null) {
			throw new RestartResponseException(SetupGamePage.class);
		}

		// If the game hasn't been setup yet go to the setup page
		if (false == yatzyGame.isStarted()) {
			PageParameters params = new PageParameters();
			params.set(0, yatzyGame.getKey());
			throw new RestartResponseException(SetupGamePage.class, params);
		}

		add(new ChannelComponent("channelComponent", gameKey));

		
		
		// Score card
		ScoreCardComponent scoreCardComponent = new ScoreCardComponent("scorecard", new PropertyModel<IGame>(yatzyGame, "game")) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof ChannelUpdatedPayload) {
					ChannelUpdatedPayload channelUpdatedPayload = (ChannelUpdatedPayload) event.getPayload();
					if (channelUpdatedPayload.getChannelName().equals(gameKey)) {
						channelUpdatedPayload.getAjaxRequestTarget().add(this);
					}
				}
			}
		};
		scoreCardComponent.setOutputMarkupId(true);
		add(scoreCardComponent);
	}

}
