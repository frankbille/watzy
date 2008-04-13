package org.apache.wicket.examples.yatzy.behaviours;

import java.io.Serializable;

import org.apache.wicket.util.diff.Diff;
import org.apache.wicket.util.diff.DifferentiationFailedException;
import org.apache.wicket.util.lang.Objects;

public final class ObjectState implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient String state;

	public ObjectState(Serializable object) {
		state = createStateObject(object);
	}

	public boolean newState(Object object) {
		if (object != null && object instanceof Serializable == false) {
			throw new IllegalArgumentException("The object must be Serializable: " + object);
		}

		return newState((Serializable) object);
	}

	/**
	 * @return True if the state of the object has changed
	 */
	public boolean newState(Serializable object) {
		boolean changed = false;

		String newState = null;
		if (object != null) {
			newState = createStateObject(object);
		} else {
			newState = "";
		}

		if (state.equals(newState) == false) {
			try {
				System.out.println(Diff.diff(new Object[] { state }, new Object[] { newState }));
			} catch (DifferentiationFailedException e) {
				e.printStackTrace();
			}
			changed = true;
		}

		state = newState;

		return changed;
	}

	private String createStateObject(Serializable object) {
		return new String(Objects.objectToByteArray(object));
	}
}
