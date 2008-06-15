package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.time.Duration;

public class CompoundAjaxTimerBehavior extends AbstractAjaxTimerBehavior {
	private static final long serialVersionUID = 1L;

	private final List<ITimerListener> timerListeners = new ArrayList<ITimerListener>();

	public CompoundAjaxTimerBehavior(Duration updateInterval) {
		super(updateInterval);
	}

	public void addListener(ITimerListener listener) {
		timerListeners.add(listener);
	}

	@Override
	protected void onTimer(AjaxRequestTarget target) {
		for (ITimerListener listener : timerListeners) {
			listener.tick(target);
		}
	}

	@Override
	public boolean isEnabled(Component<?> component) {
		return timerListeners.isEmpty() == false;
	}

}
