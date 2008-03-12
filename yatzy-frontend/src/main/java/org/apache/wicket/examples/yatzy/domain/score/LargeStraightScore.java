package org.apache.wicket.examples.yatzy.domain.score;

public class LargeStraightScore extends AbstractStraightScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int[] getStraight() {
		return new int[] { 2, 3, 4, 5, 6 };
	}

	@Override
	protected int getStraightScore() {
		return 20;
	}

}
