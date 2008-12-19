package org.examples.yatzy.ai;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;

public interface AIPlayer extends IPlayer {

	boolean handles(IGame game);

	void handleTurn(ITurn turn, IGame game);

}
