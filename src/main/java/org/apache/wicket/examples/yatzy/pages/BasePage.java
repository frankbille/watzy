package org.apache.wicket.examples.yatzy.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public abstract class BasePage extends WebPage {

	public BasePage() {
		this(null);
	}

	public BasePage(IModel model) {
		super(model);

		add(new Label("pageTitle", getPageTitleModel()));
	}

	protected abstract IModel getPageTitleModel();

}
