package org.examples.yatzy.score;

/**
 * 6 dice yatzy
 */
public class MaxiYatzyScore extends AbstractYatzyScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getYatzyScore(int value) {
		return 100;
	}

}