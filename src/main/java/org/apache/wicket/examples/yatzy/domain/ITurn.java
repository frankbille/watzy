package org.apache.wicket.examples.yatzy.domain;

import java.io.Serializable;
import java.util.List;

public interface ITurn extends Serializable {

	List<IDice> getDiceList();

	void roll();

	boolean mayRoll();

	void changeHold(IDice dice);

	boolean shouldHold(IDice dice);

	IPlayer getPlayer();

	boolean hasValue();

}
