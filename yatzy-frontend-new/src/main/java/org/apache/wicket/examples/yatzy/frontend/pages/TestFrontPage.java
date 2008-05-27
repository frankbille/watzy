package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.examples.yatzy.MaxiGame;

public class TestFrontPage extends BasePage<Void> {

	public TestFrontPage() {
		add(new Link<Void>("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				MaxiGame game = new MaxiGame();
				getRequestCycle().setResponsePage(new GamePage(game));
			}
		});
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new Model<String>("Hej");
	}

}
