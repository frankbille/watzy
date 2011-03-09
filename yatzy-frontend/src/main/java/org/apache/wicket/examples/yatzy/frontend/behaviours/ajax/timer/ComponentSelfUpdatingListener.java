package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class ComponentSelfUpdatingListener<T extends Component> implements ITimerListener {
	private static final long serialVersionUID = 1L;

	private final T component;

	public ComponentSelfUpdatingListener(T component) {
		if (component == null) {
			throw new IllegalArgumentException("Component may not be null");
		}

		this.component = component;

		component.setOutputMarkupId(true);
	}

	public void tick(AjaxRequestTarget target) {
		if (shouldUpdate(component)) {
			target.addComponent(component);
		}
	}

	public T getComponent() {
		return component;
	}

	protected boolean shouldUpdate(T component) {
		return true;
	}

}
