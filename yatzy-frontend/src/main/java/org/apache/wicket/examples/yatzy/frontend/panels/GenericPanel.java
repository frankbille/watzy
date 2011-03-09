package org.apache.wicket.examples.yatzy.frontend.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class GenericPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public GenericPanel(String id) {
		super(id);
	}
	
	public GenericPanel(String id, IModel<T> model) {
		super(id, model);
	}

	public void setModel(IModel<T> model) {
		setDefaultModel(model);
	}
	
	@SuppressWarnings("unchecked")
	public IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}
	
	@SuppressWarnings("unchecked")
	public T getModelObject() {
		return (T) getDefaultModelObject();
	}
	
	public void setModelObject(T modelObject) {
		setDefaultModelObject(modelObject);
	}
	
}
