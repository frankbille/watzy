package org.apache.wicket.examples.yatzy.domain.score;

import java.util.List;

import org.apache.wicket.examples.yatzy.domain.ITurn;

public abstract class AbstractOfAKindScore extends AbstractTurnScore {

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		List<Integer> values = getValues(turn);

		for (int i = values.size() - 1; i >= 0; i--) {
			int value = values.get(i);
			int valueCount = getValueCount(turn, value);

			if (valueCount >= getValueCount()) {
				score = value * getValueCount();
				break;
			}
		}

		return score;
	}

	protected abstract int getValueCount();

}
