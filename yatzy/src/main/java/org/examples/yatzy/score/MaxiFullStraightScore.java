package org.examples.yatzy.score;

public class MaxiFullStraightScore extends AbstractStraightScore {
	private static final long serialVersionUID = 1L;

	public MaxiFullStraightScore() {
	}

	@Override
	protected int[] getStraight() {
		return new int[] { 1, 2, 3, 4, 5, 6 };
	}

	@Override
	protected int getStraightScore() {
		return 30;
	}

	@Override
	public MaxiFullStraightScore copy() {
		return new MaxiFullStraightScore(this);
	}

	private MaxiFullStraightScore(MaxiFullStraightScore s) {
		super(s);
	}

}
