package org.apache.wicket.examples.yatzy.frontend;

import org.apache.wicket.Page;
import org.apache.wicket.examples.yatzy.frontend.pages.GamePage;
import org.apache.wicket.examples.yatzy.frontend.pages.TestFrontPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;

public class YatzyApplication extends WebApplication {

	@Override
	protected void init() {
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));
	}

	@Override
	public Class<? extends Page<?>> getHomePage() {
		return TestFrontPage.class;
	}

}
