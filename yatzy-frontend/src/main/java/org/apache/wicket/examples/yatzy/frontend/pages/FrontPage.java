package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class FrontPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public FrontPage() {
		add(new BookmarkablePageLink<Void>("newGameLink", SetupGamePage.class));
	}
	
}
