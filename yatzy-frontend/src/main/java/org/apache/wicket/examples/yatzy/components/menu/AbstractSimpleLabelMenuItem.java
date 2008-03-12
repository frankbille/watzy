package org.apache.wicket.examples.yatzy.components.menu;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AbstractSimpleLabelMenuItem implements IMenuItem {
	private final IModel labelModel;

	public AbstractSimpleLabelMenuItem(String label) {
		this.labelModel = new Model(label);
	}

	public AbstractSimpleLabelMenuItem(IModel labelModel) {
		this.labelModel = labelModel;
	}

	public Component createLabelComponent(String wicketId) {
		return new Label(wicketId, labelModel);
	}

}
