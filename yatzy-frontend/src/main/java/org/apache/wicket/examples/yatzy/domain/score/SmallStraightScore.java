package org.apache.wicket.examples.yatzy.domain.score;

public class SmallStraightScore extends AbstractStraightScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int[] getStraight() {
		return new int[] { 1, 2, 3, 4, 5 };
	}

	@Override
	protected int getStraightScore() {
		return 15;
	}

}
