package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import org.apache.wicket.util.lang.Objects;

public class SerializableObjectState implements IObjectState {
	private static final long serialVersionUID = 1L;

	private String serializedObjectState;

	public SerializableObjectState() {
		updateState(null);
	}

	public boolean updateState(Object stateObject) {
		boolean stateChanged = true;

		byte[] data = Objects.objectToByteArray(stateObject);
		String dataString = new String(data);

		if (dataString.equals(serializedObjectState)) {
			stateChanged = false;
		}

		serializedObjectState = dataString;

		return stateChanged;
	}
}
