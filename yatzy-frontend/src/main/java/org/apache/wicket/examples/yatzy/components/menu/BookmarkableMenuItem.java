package org.apache.wicket.examples.yatzy.components.menu;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;

public class BookmarkableMenuItem extends AbstractSimpleLabelMenuItem {
	private static final long serialVersionUID = 1L;

	private final Class<? extends Page> pageClass;
	private final PageParameters pageParameters;

	public BookmarkableMenuItem(String label, Class<? extends Page> pageClass) {
		super(label);
		this.pageClass = pageClass;
		this.pageParameters = null;
	}

	public BookmarkableMenuItem(IModel labelModel, Class<? extends Page> pageClass) {
		super(labelModel);
		this.pageClass = pageClass;
		this.pageParameters = null;
	}

	public BookmarkableMenuItem(String label, Class<? extends Page> pageClass, PageParameters pageParameters) {
		super(label);
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}

	public BookmarkableMenuItem(IModel labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		super(labelModel);
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}

	public MarkupContainer createLink(String wicketId) {
		return new BookmarkablePageLink(wicketId, pageClass, pageParameters);
	}
}
