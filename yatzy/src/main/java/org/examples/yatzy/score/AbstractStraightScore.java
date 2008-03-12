package org.examples.yatzy.score;

import org.examples.yatzy.ITurn;

public abstract class AbstractStraightScore extends AbstractTurnScore {

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		int[] counts = getValueCount(turn, getStraight());

		boolean accept = true;
		for (int count : counts) {
			if (count == 0) {
				accept = false;
				break;
			}
		}

		if (accept) {
			score = getStraightScore();
		}

		return score;
	}

	protected abstract int[] getStraight();

	protected abstract int getStraightScore();

}
