package org.examples.yatzy.score;

public class SmallStraightScore extends AbstractStraightScore {
	private static final long serialVersionUID = 1L;

	public SmallStraightScore() {
	}

	@Override
	protected int[] getStraight() {
		return new int[] { 1, 2, 3, 4, 5 };
	}

	@Override
	protected int getStraightScore() {
		return 15;
	}

	@Override
	public SmallStraightScore copy() {
		return new SmallStraightScore(this);
	}

	private SmallStraightScore(SmallStraightScore s) {
		super(s);
	}

}
