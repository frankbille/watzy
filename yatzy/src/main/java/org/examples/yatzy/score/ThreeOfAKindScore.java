package org.examples.yatzy.score;

public class ThreeOfAKindScore extends AbstractOfAKindScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getValueCount() {
		return 3;
	}

}
