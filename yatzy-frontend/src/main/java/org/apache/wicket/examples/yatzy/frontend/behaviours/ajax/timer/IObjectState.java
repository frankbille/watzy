package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import java.io.Serializable;

public interface IObjectState extends Serializable {

	/**
	 * Update the state with the state object and return true if the state
	 * object has changed since last time this method was called.
	 * 
	 * @param stateObject
	 * @return
	 */
	boolean updateState(Object stateObject);

}
