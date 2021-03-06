package org.examples.yatzy.score;

import java.util.ArrayList;
import java.util.List;

import org.examples.yatzy.ITurn;

public abstract class AbstractCountScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

	public AbstractCountScore() {
	}

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		List<Integer> values = getValues(turn);

		List<Integer> pairValue = new ArrayList<Integer>();

		for (int i = values.size() - 1; i >= 0; i--) {
			int value = values.get(i);
			int valueCount = getValueCount(turn, value);

			if (valueCount >= getGroupCount()) {
				pairValue.add(value);

				if (pairValue.size() == getNumberOfGroups()) {
					for (Integer v : pairValue) {
						score += v * getGroupCount();
					}
					break;
				}
			}
		}

		return score;
	}

	protected abstract int getNumberOfGroups();

	protected abstract int getGroupCount();

	@Override
	public abstract AbstractCountScore copy();

	protected AbstractCountScore(AbstractCountScore s) {
		super(s);
	}

}
