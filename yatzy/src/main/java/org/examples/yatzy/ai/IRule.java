package org.examples.yatzy.ai;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;

public interface IRule {

	IAction vote(IPlayer player, ITurn turn, IGame game);

}
