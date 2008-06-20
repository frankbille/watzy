package org.examples.yatzy;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * A standard dice is a dice with a sequential number of values, like 1, 2, 3,
 * 4, 5, 6 or 1, 2, 3, 4 (i.e no gaps between the values).
 */
public abstract class AbstractStandardDice extends AbstractDice {
	private static final long serialVersionUID = 1L;

	private final RandomData random = new RandomDataImpl();

	@Override
	protected int doRoll() {
		return random.nextSecureInt(getMinValue(), getMaxValue());
	}

	protected abstract int getMinValue();

	protected abstract int getMaxValue();

}
