package org.examples.yatzy;

import java.io.Serializable;

public interface IRound extends Serializable {

	ITurn nextTurn();

	boolean hasMoreTurns();

	IPlayer getCurrentPlayer();

}
