package org.apache.wicket.examples.yatzy.components.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.examples.yatzy.components.menu.IExpandableContentMenuItem.ContentHolderMarkupIdProvider;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class Menu extends Panel {
	private static final long serialVersionUID = 1L;

	private final Map<IExpandableContentMenuItem, Component> expandableComponents = new HashMap<IExpandableContentMenuItem, Component>();

	public Menu(String id, final IModel model) {
		super(id, model);

		IModel expandableContentModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected Object load() {
				expandableComponents.clear();

				List<IMenuItem> menuItems = (List<IMenuItem>) model.getObject();

				for (IMenuItem menuItem : menuItems) {
					if (menuItem instanceof IExpandableContentMenuItem) {
						IExpandableContentMenuItem expandableContentMenuItem = (IExpandableContentMenuItem) menuItem;
						expandableComponents.put(expandableContentMenuItem, null);
					}
				}

				return new ArrayList<IExpandableContentMenuItem>(expandableComponents.keySet());
			}
		};

		add(new ListView("contents", expandableContentModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				IExpandableContentMenuItem expandableContentMenuItem = (IExpandableContentMenuItem) item
						.getModelObject();

				item.setOutputMarkupId(true);
				expandableComponents.put(expandableContentMenuItem, item);

				item.add(expandableContentMenuItem.createExpandableContent("content"));
			}
		});

		add(new ListView("items", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				item.setRenderBodyOnly(true);

				IMenuItem menuItem = (IMenuItem) item.getModelObject();

				item.add(new WebComponent("separator") {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return item.getIndex() > 0;
					}
				});

				// expandable item
				if (menuItem instanceof IExpandableContentMenuItem) {
					IExpandableContentMenuItem expandableContentMenuItem = (IExpandableContentMenuItem) menuItem;
					final Component expandableComponent = expandableComponents.get(expandableContentMenuItem);
					expandableContentMenuItem.setContentHolderMarkupIdProvider(new ContentHolderMarkupIdProvider() {
						private static final long serialVersionUID = 1L;

						public String getMarkupId() {
							return expandableComponent.getMarkupId();
						}
					});
				}

				MarkupContainer link = menuItem.createLink("link");
				item.add(link);

				link.add(menuItem.createLabelComponent("label"));
			}
		});
	}
}
