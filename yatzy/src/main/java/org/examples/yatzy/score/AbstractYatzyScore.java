package org.examples.yatzy.score;

import java.util.List;

import org.examples.yatzy.ITurn;

public abstract class AbstractYatzyScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

	public AbstractYatzyScore() {
	}

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

	protected AbstractYatzyScore(AbstractYatzyScore s) {
		super(s);
	}

}
