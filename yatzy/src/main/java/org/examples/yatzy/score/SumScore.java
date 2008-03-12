package org.examples.yatzy.score;

import java.util.List;

import org.examples.yatzy.IDice;
import org.examples.yatzy.ITurn;

public class SumScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

	private final int value;

	public SumScore(int value) {
		this.value = value;
	}

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		List<IDice> diceList = turn.getDiceList();
		for (IDice dice : diceList) {
			if (dice.hasValue()) {
				if (dice.getValue() == value) {
					score += dice.getValue();
				}
			}
		}

		return score;
	}

	public int getValue() {
		return value;
	}

}
