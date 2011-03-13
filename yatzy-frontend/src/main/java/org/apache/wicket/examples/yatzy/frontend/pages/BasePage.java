package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

public abstract class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public BasePage() {
		add(new Label("pageTitle", getPageTitle()));
	}

	protected IModel<String> getPageTitle() {
		return new StringResourceModel("yatzy", this, null);
	}

}
