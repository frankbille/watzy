package org.examples.yatzy;

public class CubeDice extends AbstractStandardDice {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getMaxValue() {
		return 6;
	}

	@Override
	protected int getMinValue() {
		return 1;
	}

}
