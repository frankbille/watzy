package org.apache.wicket.examples.yatzy.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.examples.yatzy.components.menu.ExpandableContentMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.IMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.Menu;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class BasePage extends WebPage {

	public BasePage() {
		this(null);
	}

	public BasePage(IModel model) {
		super(model);

		add(new Menu("menu", new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return BasePage.this.getMenuItems();
			}
		}));

		add(new Label("pageTitle", getPageTitleModel()));
	}

	protected abstract IModel getPageTitleModel();

	public List<IMenuItem> getMenuItems() {
		List<IMenuItem> menuItems = new ArrayList<IMenuItem>();

		addMenuItems(menuItems);
		menuItems.add(new ExpandableContentMenuItem("About") {
			private static final long serialVersionUID = 1L;

			public Component createExpandableContent(String wicketId) {
				return new AboutPanel(wicketId).setRenderBodyOnly(true);
			}
		});

		return menuItems;
	}

	protected void addMenuItems(List<IMenuItem> menuItems) {
	}

}
