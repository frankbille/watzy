package org.apache.wicket.examples.yatzy.domain.score;

import org.apache.wicket.examples.yatzy.domain.IPlayer;

public abstract class AbstractStandardBonusScoreGroup extends AbstractScoreGroup implements IScoreGroup {

	public AbstractStandardBonusScoreGroup() {
		super();
	}

	@Override
	public int getScore(IPlayer player) {
		int score = super.getScore(player);

		if (isBonusAvailable(player)) {
			score += getBonus(player);
		}

		return score;
	}

	public int getScoreWithoutBonus(IPlayer player) {
		return super.getScore(player);
	}

	public abstract boolean isBonusAvailable(IPlayer playerp);

	public abstract int getBonus(IPlayer player);

}
