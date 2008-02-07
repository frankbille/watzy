package org.apache.wicket.examples.yatzy;

import org.apache.wicket.examples.yatzy.pages.GamePage;
import org.apache.wicket.examples.yatzy.pages.NewGamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;

public class YatzyApplication extends WebApplication {

	@Override
	protected void init() {
		mountBookmarkablePage("/newgame", NewGamePage.class);
		mount(new HybridUrlCodingStrategy("/game", GamePage.class, true));
	}

	@Override
	public Class<?> getHomePage() {
		return NewGamePage.class;
	}

	public static YatzyApplication get() {
		return (YatzyApplication) WebApplication.get();
	}

}
