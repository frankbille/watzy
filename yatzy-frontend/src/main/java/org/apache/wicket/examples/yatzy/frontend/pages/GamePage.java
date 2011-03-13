package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.components.ChannelComponent;


public class GamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public GamePage() {
		String gameKey = "adh5erv45ygeshwergs";
		
		add(new ChannelComponent("channelComponent", gameKey));
		
		add(new AjaxLink<Void>("testLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				send(YatzyApplication.get(), Broadcast.BREADTH, ChannelComponent.TRIGGER_UPDATE_EVENT);
			}
		});
	}
	
}
