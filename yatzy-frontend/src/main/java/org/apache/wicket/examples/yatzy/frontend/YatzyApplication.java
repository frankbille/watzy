package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.examples.yatzy.frontend.dao.YatzyGameDao;
import org.apache.wicket.examples.yatzy.frontend.pages.FrontPage;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.examples.yatzy.frontend.pages.SetupGamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}
	
	private YatzyGameDao yatzyGameDao;
	private ApplicationContext context;
	
	@Override
	protected void init() {
		super.init();

		context = new ClassPathXmlApplicationContext(getApplicationContextLocation());
		yatzyGameDao = context.getBean("yatzyGameDao", YatzyGameDao.class);
		
		mountPage("/game", GamePage.class);
		mountPage("/setup", SetupGamePage.class);
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
	}
	
	private String getApplicationContextLocation() {
		String location = null;
		
		location = System.getProperty("yatzy.applicationContext");
		
		if (location == null) {
			location = getInitParameter("applicationContext");
		}
		
		return location;
	}

}
