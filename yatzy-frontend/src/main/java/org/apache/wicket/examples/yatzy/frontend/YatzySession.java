package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class YatzySession extends WebSession {
	private static final long serialVersionUID = 1L;

	public YatzySession(Request request) {
		super(request);
	}
	
	public String getChannelKey() {
		return getId();
	}
	
	public static YatzySession get() {
		return (YatzySession) WebSession.get();
	}

}
