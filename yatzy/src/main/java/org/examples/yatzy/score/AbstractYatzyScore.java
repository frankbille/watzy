package org.examples.yatzy.score;

import java.util.List;

import org.examples.yatzy.ITurn;

public abstract class AbstractYatzyScore extends AbstractTurnScore {

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		List<Integer> values = getValues(turn);

		if (values.size() == 1) {
			int value = values.get(0);
			score = getYatzyScore(value);
		}

		return score;
	}

	protected abstract int getYatzyScore(int value);

}
