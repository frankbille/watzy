package org.examples.yatzy.score;

public class FourOfAKindScore extends AbstractOfAKindScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getValueCount() {
		return 4;
	}

}
