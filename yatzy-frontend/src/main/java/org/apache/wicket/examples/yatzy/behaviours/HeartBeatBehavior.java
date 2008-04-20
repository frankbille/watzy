package org.apache.wicket.examples.yatzy.behaviours;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.time.Duration;

public class HeartBeatBehavior extends AbstractAjaxTimerBehavior {
	private static final long serialVersionUID = 1L;

	public static interface HeartBeat extends Serializable {
		void ping();
	}

	private final HeartBeat heartBeat;

	public HeartBeatBehavior(Duration updateInterval, HeartBeat heartBeat) {
		super(updateInterval);
		this.heartBeat = heartBeat;
	}

	@Override
	protected void onTimer(AjaxRequestTarget target) {
		heartBeat.ping();
	}

}
