package org.examples.yatzy.score;

import org.examples.yatzy.IPlayer;

public interface IBonusScoreGroup extends IScoreGroup {

	int getScoreWithoutBonus(IPlayer player);

	boolean isBonusAvailable(IPlayer playerp);

	int getBonus(IPlayer player);

}