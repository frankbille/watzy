package org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer;

import org.apache.wicket.util.lang.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializableObjectState implements IObjectState {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SerializableObjectState.class);

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
		} else {
			if (log.isDebugEnabled()) {
				log.debug(stateObject != null ? stateObject.getClass().getSimpleName() : "NULL");
				log.debug("Old state size: "
						+ (serializedObjectState != null ? serializedObjectState.length() : 0));
				log.debug("New state size: " + (dataString != null ? dataString.length() : 0));
			}
		}

		serializedObjectState = dataString;

		return stateChanged;
	}
}
