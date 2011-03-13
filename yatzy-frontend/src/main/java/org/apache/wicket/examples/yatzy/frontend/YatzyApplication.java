package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.examples.yatzy.frontend.pages.FrontPage;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}
	
	@Override
	protected void init() {
		super.init();
		
		mountPage("/game", GamePage.class);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return FrontPage.class;
	}
	
	@Override
	public Session newSession(Request request, Response response) {
		return new YatzySession(request);
	}

}
