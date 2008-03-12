package org.examples.yatzy;

import java.util.ArrayList;
import java.util.List;

public abstract class StandardDiceFactory implements IDiceFactory {

	public List<IDice> createDiceList() {
		List<IDice> diceList = new ArrayList<IDice>();
		addDice(diceList);
		return diceList;
	}

	protected abstract void addDice(List<IDice> diceList);

}
