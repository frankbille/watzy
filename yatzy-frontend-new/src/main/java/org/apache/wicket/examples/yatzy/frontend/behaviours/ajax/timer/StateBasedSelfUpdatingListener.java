package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateBasedSelfUpdatingListener<T extends Component<?>> extends
		ComponentSelfUpdatingListener<T> {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(StateBasedSelfUpdatingListener.class);

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
		log.debug("Updating state for: " + component);
		return objectState.updateState(getStateObject(component));
	}

	private void updateState() {
		log.debug("Updating state for: " + getComponent());
		this.objectState.updateState(getStateObject(getComponent()));
	}

	protected Object getStateObject(T component) {
		return component.getModelObject();
	}

}
