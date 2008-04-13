package org.apache.wicket.examples.yatzy;

import org.apache.wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import org.apache.wicket.examples.yatzy.pages.AuthenticatedBasePage;
import org.apache.wicket.examples.yatzy.pages.EnterNamePage;

public class YatzyAuthorizationStrategy extends SimplePageAuthorizationStrategy {

	public YatzyAuthorizationStrategy() {
		super(AuthenticatedBasePage.class, EnterNamePage.class);
	}

	@Override
	protected boolean isAuthorized() {
		return YatzySession.get().hasPlayer();
	}

}
