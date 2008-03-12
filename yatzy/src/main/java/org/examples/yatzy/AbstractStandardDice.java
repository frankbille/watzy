package org.examples.yatzy;

/**
 * A standard dice is a dice with a sequential number of values, like 1, 2, 3,
 * 4, 5, 6 or 1, 2, 3, 4 (i.e no gaps between the values).
 */
public abstract class AbstractStandardDice extends AbstractDice {

	@Override
	protected int doRoll() {
		return (int) (Math.random() * (getMaxValue() - getMinValue() + 1)) + getMinValue();
	}

	protected abstract int getMinValue();

	protected abstract int getMaxValue();

}
