package org.apache.wicket.examples.yatzy.domain;

import java.io.Serializable;

public interface IRound extends Serializable {

	ITurn nextTurn();

	boolean hasMoreTurns();

	IPlayer getCurrentPlayer();

}
