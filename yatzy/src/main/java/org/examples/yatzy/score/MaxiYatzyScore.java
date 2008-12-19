package org.examples.yatzy.score;

/**
 * 6 dice yatzy
 */
public class MaxiYatzyScore extends AbstractYatzyScore {
	private static final long serialVersionUID = 1L;

	public MaxiYatzyScore() {
	}

	@Override
	protected int getYatzyScore(int value) {
		return 100;
	}

	@Override
	public AbstractTurnScore copy() {
		return new MaxiYatzyScore(this);
	}

	private MaxiYatzyScore(MaxiYatzyScore s) {
		super(s);
	}

}
