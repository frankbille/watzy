package org.apache.wicket.examples.yatzy.domain;

public abstract class AbstractDice implements IDice {

	private int value = 1;

	private boolean hasValue;

	public AbstractDice() {
	}

	public int getValue() {
		if (hasValue == false) {
			throw new IllegalStateException("Can't get value if it hasn't been rolled");
		}

		return value;
	}

	public void roll() {
		value = doRoll();
		hasValue = true;
	}

	public boolean hasValue() {
		return hasValue;
	}

	protected abstract int doRoll();

}
