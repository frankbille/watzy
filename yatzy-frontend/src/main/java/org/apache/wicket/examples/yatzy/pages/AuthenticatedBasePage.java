package org.apache.wicket.examples.yatzy.pages;

import org.apache.wicket.model.IModel;

public abstract class AuthenticatedBasePage<T> extends BasePage<T> {

	public AuthenticatedBasePage() {
		super();
	}

	public AuthenticatedBasePage(IModel<T> model) {
		super(model);
	}

}
