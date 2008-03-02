package org.apache.wicket.examples.yatzy.components.menu;

import java.io.Serializable;

import org.apache.wicket.Component;

public interface IExpandableContentMenuItem extends IMenuItem {

	public static interface ContentHolderMarkupIdProvider extends Serializable {
		String getMarkupId();
	}

	Component createExpandableContent(String wicketId);

	void setContentHolderMarkupIdProvider(ContentHolderMarkupIdProvider markupIdProvider);

}
