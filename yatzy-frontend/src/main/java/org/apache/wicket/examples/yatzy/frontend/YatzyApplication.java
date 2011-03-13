package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.Page;
import org.apache.wicket.examples.yatzy.frontend.pages.FrontPage;
import org.apache.wicket.protocol.http.WebApplication;

public class YatzyApplication extends WebApplication {

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return FrontPage.class;
	}

}
