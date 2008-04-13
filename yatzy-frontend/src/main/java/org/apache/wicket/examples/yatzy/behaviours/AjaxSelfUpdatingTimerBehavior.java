package org.apache.wicket.examples.yatzy.behaviours;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.util.time.Duration;

public class AjaxSelfUpdatingTimerBehavior extends AbstractAjaxTimerBehavior {
	private static final long serialVersionUID = 1L;

	private final ObjectState state;

	public AjaxSelfUpdatingTimerBehavior(Duration updateInterval) {
		super(updateInterval);

		state = new ObjectState(null);
	}

	@Override
	protected void onBind() {
		super.onBind();

		state.newState(getStateObject());
	}

	@Override
	protected void onTimer(AjaxRequestTarget target) {
		if (state.newState(getStateObject())) {
			target.addComponent(getComponent());

			onPostTimerUpdated(target);
		}
	}

	@Override
	protected void onHeadRendered(IHeaderResponse response) {
		state.newState(getComponent().getModelObject());
	}

	protected void onPostTimerUpdated(AjaxRequestTarget target) {
	}

	protected Object getStateObject() {
		return getComponent().getModelObject();
	}

}
