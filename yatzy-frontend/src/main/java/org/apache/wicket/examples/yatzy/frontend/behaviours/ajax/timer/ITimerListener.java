package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface ITimerListener extends Serializable {

	void tick(AjaxRequestTarget target);

}
