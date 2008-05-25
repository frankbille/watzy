package org.apache.wicket.examples.yatzy.components.menu;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AbstractSimpleLabelMenuItem implements IMenuItem {
	private final IModel<String> labelModel;

	public AbstractSimpleLabelMenuItem(String label) {
		this.labelModel = new Model<String>(label);
	}

	public AbstractSimpleLabelMenuItem(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}

	public Component<String> createLabelComponent(String wicketId) {
		return new Label<String>(wicketId, labelModel);
	}

}
