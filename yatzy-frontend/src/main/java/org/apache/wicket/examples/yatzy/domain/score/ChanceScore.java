package org.apache.wicket.examples.yatzy.domain.score;

import org.apache.wicket.examples.yatzy.domain.IDice;
import org.apache.wicket.examples.yatzy.domain.ITurn;

public class ChanceScore extends AbstractTurnScore {
	private static final long serialVersionUID = 1L;

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

}
