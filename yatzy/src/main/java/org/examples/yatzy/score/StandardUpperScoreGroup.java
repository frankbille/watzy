package org.examples.yatzy.score;

import org.examples.yatzy.IPlayer;

public class StandardUpperScoreGroup extends AbstractUpperScoreGroup {
	private static final long serialVersionUID = 1L;

	public StandardUpperScoreGroup() {
	}

	@Override
	protected int getValueCount() {
		return 3;
	}

	@Override
	public int getBonus(IPlayer player) {
		return 50;
	}

	@Override
	public StandardUpperScoreGroup copy() {
		return new StandardUpperScoreGroup(this);
	}

	private StandardUpperScoreGroup(StandardUpperScoreGroup s) {
		super(s);
	}

}
