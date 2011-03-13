package org.examples.yatzy.score;

import org.examples.yatzy.IPlayer;

public abstract class AbstractStandardBonusScoreGroup extends AbstractScoreGroup implements IScoreGroup, IBonusScoreGroup {
	private static final long serialVersionUID = 1L;

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

	protected AbstractStandardBonusScoreGroup(AbstractStandardBonusScoreGroup s) {
		super(s);
	}

}
