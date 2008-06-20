package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class HeartBeatListener implements ITimerListener {
	private static final long serialVersionUID = 1L;

	public static interface IHeartBeat extends Serializable {
		void beat();
	}

	private final IHeartBeat heartBeat;

	public HeartBeatListener(IHeartBeat heartBeat) {
		if (heartBeat == null) {
			throw new IllegalArgumentException("Heartbeat may not be null");
		}

		this.heartBeat = heartBeat;
	}

	public void tick(AjaxRequestTarget target) {
		heartBeat.beat();
	}

}
