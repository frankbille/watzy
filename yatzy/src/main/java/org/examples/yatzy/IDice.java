package org.examples.yatzy;

import java.io.Serializable;

public interface IDice extends Serializable {

	void roll();

	int getValue();

	boolean hasValue();

}
