package org.apache.wicket.examples.yatzy.frontend.components;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.apphosting.api.ApiProxy.CallNotFoundException;

/**
 * Component that links Apache Wicket and Google App Engines Channel API. It
 * uses Wickets inter-component events, which were introduced in version 1.5.
 * 
 * Currently it does 3 trips from client to server when an update is to be
 * pushed to the clients.
 * 
 * <ol>
 * <li>Server->Client. The server tells the client(s) that an update is
 * available on the server. (LIGHTWEIGHT)
 * <li>Client->Server. The client(s) initializes an AJAX request to the server
 * to instantiate the {@link AjaxRequestTarget} on the server. (LIGHTWEIGHT)
 * <li>Server->Client. On the server sends an event to everyone interested that
 * an update has happened on a given channel. An instance of
 * {@link AjaxRequestTarget} is attached to the event, which consumers can then
 * use to do what they want. (LIGHTWEIGHT->HEAVYWEIGHT depending on how much the
 * constumers want to update.)
 * </ol>
 * 
 * In theory it should be possible to hook into Wickets request flow and gather
 * what components want to do before making the first trip. This would eliminate
 * trip 2 and 3. This option hasn't been explored yet.
 * 
 * @see http://code.google.com/appengine/docs/java/channel/overview.html
 * @see https
 *      ://cwiki.apache.org/WICKET/migration-to-wicket-15.html#MigrationtoWicket1
 *      .5-Intercomponentevents
 */
public class ChannelComponent extends Panel {
	private static final long serialVersionUID = 1L;
	
	public static final ChannelUpdateEventPayload TRIGGER_UPDATE_EVENT = new ChannelUpdateEventPayload();
	
	public static final class ChannelUpdatedPayload {
		private String channelName;
		private AjaxRequestTarget target;

		public ChannelUpdatedPayload(String channelName, AjaxRequestTarget target) {
			this.channelName = channelName;
			this.target = target;
		}

		public AjaxRequestTarget getAjaxRequestTarget() {
			return target;
		}
		
		public String getChannelName() {
			return channelName;
		}
	}
	
	private static final class ChannelUpdateEventPayload {
	}
	
	private class AjaxCallback extends AbstractDefaultAjaxBehavior {
		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			send(YatzyApplication.get(), Broadcast.BREADTH, new ChannelUpdatedPayload(channelName, target));
		}
		
		@Override
		public CharSequence getCallbackScript() {
			return super.getCallbackScript();
		}
	}
	
	private String channelToken;
	private final String channelName;
	private AjaxCallback ajaxCallback;

	public ChannelComponent(String id, String channelName) {
		super(id);
		this.channelName = channelName;
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		try {
			channelToken = channelService.createChannel(channelName);
		} catch (CallNotFoundException e) {
			if (YatzyApplication.get().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
				channelToken = "DUMMY";
			} else {
				throw e;
			}
		}
		
		ajaxCallback = new AjaxCallback();
		add(ajaxCallback);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		StringBuilder channelInit = new StringBuilder();
		
		channelInit.append("channel = new goog.appengine.Channel('").append(channelToken).append("');").append("\n");
		channelInit.append("socket = channel.open();").append("\n");
		channelInit.append("socket.onmessage = function(msg) {").append(ajaxCallback.getCallbackScript()).append("};").append("\n");
		
		response.renderOnDomReadyJavaScript(channelInit.toString());
	}

	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof ChannelUpdateEventPayload) {
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			try {
				channelService.sendMessage(new ChannelMessage(channelName, "do"));
			} catch (CallNotFoundException e) {
				if (YatzyApplication.get().getConfigurationType() != RuntimeConfigurationType.DEVELOPMENT) {
					throw e;
				}
			}
		}
	}

}
