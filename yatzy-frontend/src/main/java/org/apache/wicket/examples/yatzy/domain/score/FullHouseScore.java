package org.apache.wicket.examples.yatzy.domain.score;

import java.util.List;

import org.apache.wicket.examples.yatzy.domain.ITurn;

public class FullHouseScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		List<Integer> values = getValues(turn);

		if (values.size() >= 2) {
			int threeValue = 0;
			int twoValue = 0;

			// Three
			for (int i = values.size() - 1; i >= 0; i--) {
				int value = values.get(i);
				int valueCount = getValueCount(turn, value);
				if (valueCount >= 3) {
					threeValue = value;
					break;
				}
			}

			// Two
			for (int i = values.size() - 1; i >= 0; i--) {
				int value = values.get(i);
				if (value != threeValue) {
					int valueCount = getValueCount(turn, value);
					if (valueCount >= 2) {
						twoValue = value;
						break;
					}
				}
			}

			if (threeValue > 0 && twoValue > 0) {
				score = threeValue * 3 + twoValue * 2;
			}
		}

		return score;
	}

}
