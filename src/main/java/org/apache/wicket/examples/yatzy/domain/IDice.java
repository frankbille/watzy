package org.apache.wicket.examples.yatzy.domain;

import java.io.Serializable;

public interface IDice extends Serializable {

	void roll();

	int getValue();

	boolean hasValue();

}
