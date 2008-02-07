package org.apache.wicket.examples.yatzy.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StandardTurn implements ITurn {
	private static final long serialVersionUID = 1L;

	private final Map<IDice, Boolean> diceList = new LinkedHashMap<IDice, Boolean>();
	private int rolls;
	private final IPlayer player;

	public StandardTurn(List<IDice> diceList, int rolls, IPlayer player) {
		this.rolls = rolls;
		this.player = player;

		for (IDice dice : diceList) {
			this.diceList.put(dice, false);
		}
	}

	public void changeHold(IDice dice) {
		checkDice(dice);

		diceList.put(dice, diceList.get(dice) == false);
	}

	public List<IDice> getDiceList() {
		return new ArrayList<IDice>(this.diceList.keySet());
	}

	public boolean mayRoll() {
		return rolls > 0;
	}

	public void roll() {
		if (rolls <= 0) {
			throw new IllegalStateException("Can't roll because no more rolls left. Call mayRoll before calling this.");
		}

		for (IDice dice : diceList.keySet()) {
			if (diceList.get(dice) == false) {
				dice.roll();
			}
		}

		rolls--;
	}

	public boolean shouldHold(IDice dice) {
		checkDice(dice);

		return diceList.get(dice);
	}

	public IPlayer getPlayer() {
		return player;
	}

	public boolean hasValue() {
		boolean hasValue = false;

		for (IDice dice : diceList.keySet()) {
			if (dice.hasValue()) {
				hasValue = true;
				break;
			}
		}

		return hasValue;
	}

	private void checkDice(IDice dice) {
		if (diceList.containsKey(dice) == false) {
			throw new IllegalArgumentException("This turn doesn't contain the dice");
		}
	}

}
