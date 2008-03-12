package org.apache.wicket.examples.yatzy.domain.score;

import org.apache.wicket.examples.yatzy.domain.IPlayer;

public class MaxiUpperScoreGroup extends AbstractUpperScoreGroup {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getValueCount() {
		return 4;
	}

	@Override
	public int getBonus(IPlayer player) {
		return 100;
	}

}
