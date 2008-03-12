package org.examples.yatzy.score;

import org.examples.yatzy.IPlayer;

public abstract class AbstractUpperScoreGroup extends AbstractStandardBonusScoreGroup {

	public AbstractUpperScoreGroup() {
		addScore(new SumScore(1));
		addScore(new SumScore(2));
		addScore(new SumScore(3));
		addScore(new SumScore(4));
		addScore(new SumScore(5));
		addScore(new SumScore(6));
	}

	@Override
	public boolean isBonusAvailable(IPlayer player) {
		int vc = getValueCount();
		int bonusScore = vc * 1 + vc * 2 + vc * 3 + vc * 4 + vc * 5 + vc * 6;
		return getScoreWithoutBonus(player) >= bonusScore;
	}

	protected abstract int getValueCount();

}
