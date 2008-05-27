package org.apache.wicket.examples.yatzy.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.examples.yatzy.frontend.components.menu.ExpandableContentMenuItem;
import org.apache.wicket.examples.yatzy.frontend.components.menu.IMenuItem;
import org.apache.wicket.examples.yatzy.frontend.components.menu.Menu;
import org.apache.wicket.examples.yatzy.frontend.panels.AboutPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

public abstract class BasePage<T> extends WebPage<T> {

	public BasePage() {
		this(null);
	}

	public BasePage(IModel<T> model) {
		super(model);

		add(new Menu("menu", new LoadableDetachableModel<List<IMenuItem>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IMenuItem> load() {
				return BasePage.this.getMenuItems();
			}
		}));

		add(new Label<String>("pageTitle", getPageTitleModel()));
	}

	protected abstract IModel<String> getPageTitleModel();

	public List<IMenuItem> getMenuItems() {
		List<IMenuItem> menuItems = new ArrayList<IMenuItem>();

		addMenuItems(menuItems);

		menuItems.add(new ExpandableContentMenuItem(new StringResourceModel("about", this, null)) {
			private static final long serialVersionUID = 1L;

			public Component<?> createExpandableContent(String wicketId) {
				return new AboutPanel(wicketId).setRenderBodyOnly(true);
			}
		});

		return menuItems;
	}

	protected void addMenuItems(List<IMenuItem> menuItems) {
	}

}
