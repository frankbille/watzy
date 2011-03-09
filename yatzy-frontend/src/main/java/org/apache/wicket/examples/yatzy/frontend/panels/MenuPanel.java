package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.examples.yatzy.frontend.pages.BasePage.ILeftMenuBlock;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

public class MenuPanel extends GenericPanel<List<IMenuItem>> {
	private static final long serialVersionUID = 1L;

	public static class MenuBlock implements ILeftMenuBlock {
		private static final long serialVersionUID = 1L;

		private final IModel<List<IMenuItem>> model;

		public MenuBlock(IModel<List<IMenuItem>> model) {
			this.model = model;
		}

		public Component createMenuBlock(String wicketId) {
			return new MenuPanel(wicketId, model);
		}
	}

	private MenuPanel(String id, IModel<List<IMenuItem>> model) {
		super(id, model);

		add(new SimpleAttributeModifier("class", "menu"));

		add(new ListView<IMenuItem>("items", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<IMenuItem> item) {
				IMenuItem menuItem = item.getModelObject();

				MarkupContainer link = menuItem.createLink("link");
				item.add(link);

				link.add(menuItem.createLabelComponent("label"));
			}
		});
	}

}
