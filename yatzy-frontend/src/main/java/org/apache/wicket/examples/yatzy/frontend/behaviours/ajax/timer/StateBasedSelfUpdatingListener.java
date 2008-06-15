package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;

public class StateBasedSelfUpdatingListener<T extends Component<?>> extends
		ComponentSelfUpdatingListener<T> {
	private static final long serialVersionUID = 1L;

	private final IObjectState objectState;

	public StateBasedSelfUpdatingListener(T component) {
		this(component, new SerializableObjectState());
	}

	public StateBasedSelfUpdatingListener(T component, IObjectState objectState) {
		super(component);
		this.objectState = objectState;

		component.add(new AbstractBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void beforeRender(Component<?> component) {
				updateState();
			}
		});

		updateState();
	}

	@Override
	protected boolean shouldUpdate(T component) {
		return objectState.updateState(getStateObject(component));
	}

	private void updateState() {
		this.objectState.updateState(getStateObject(getComponent()));
	}

	protected Object getStateObject(T component) {
		return component.getModelObject();
	}

}
