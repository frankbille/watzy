package org.examples.yatzy.score;

import org.examples.yatzy.IPlayer;

public class StandardUpperScoreGroup extends AbstractUpperScoreGroup {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getValueCount() {
		return 3;
	}

	@Override
	public int getBonus(IPlayer player) {
		return 50;
	}

}
