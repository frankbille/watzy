package org.apache.wicket.examples.yatzy.domain.score;

import java.util.List;

import org.apache.wicket.examples.yatzy.domain.IDice;
import org.apache.wicket.examples.yatzy.domain.ITurn;

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
