package org.examples.yatzy.score;

import org.examples.yatzy.IDice;
import org.examples.yatzy.ITurn;

public class ChanceScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

	public ChanceScore() {
	}

	@Override
	protected int calculateScore(ITurn turn) {
		int score = 0;

		for (IDice dice : turn.getDiceList()) {
			if (dice.hasValue()) {
				score += dice.getValue();
			}
		}

		return score;
	}

	@Override
	public ChanceScore copy() {
		return new ChanceScore(this);
	}

	private ChanceScore(ChanceScore s) {
		super(s);
	}

}
