package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.dao.objectify.ObjectifyYatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.pages.FrontPage;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}
	
	private YatzyGameDao yatzyGameDao;
	
	@Override
	protected void init() {
		super.init();
		
		yatzyGameDao = new ObjectifyYatzyGameDao();
		
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
	
	public YatzyGameDao getYatzyGameDao() {
		return yatzyGameDao;
	};

}
