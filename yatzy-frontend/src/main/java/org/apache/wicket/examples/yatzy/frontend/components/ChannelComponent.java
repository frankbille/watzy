package org.apache.wicket.examples.yatzy.frontend.components;

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

public class ChannelComponent extends Panel {
	private static final long serialVersionUID = 1L;
	
	public static final ChannelUpdateEventPayload TRIGGER_UPDATE_EVENT = new ChannelUpdateEventPayload();
	
	public static final class ChannelUpdatedPayload {
		private AjaxRequestTarget target;

		public ChannelUpdatedPayload(AjaxRequestTarget target) {
			this.target = target;
		}

		public AjaxRequestTarget getAjaxRequestTarget() {
			return target;
		}
	}
	
	private static final class ChannelUpdateEventPayload {
	}
	
	private static class AjaxCallback extends AbstractDefaultAjaxBehavior {
		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			getComponent().send(YatzyApplication.get(), Broadcast.BREADTH, new ChannelUpdatedPayload(target));
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
		channelToken = channelService.createChannel(channelName);
		
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
			channelService.sendMessage(new ChannelMessage(channelName, "do"));
		}
	}

}
