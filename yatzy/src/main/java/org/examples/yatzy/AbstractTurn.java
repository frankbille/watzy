package org.examples.yatzy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTurn implements ITurn {
	private static final long serialVersionUID = 1L;

	private final Map<IDice, Boolean> diceList = new LinkedHashMap<IDice, Boolean>();
	private final IPlayer player;

	public AbstractTurn(List<IDice> diceList, IPlayer player) {
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

	public void roll() {
		if (mayRoll() == false) {
			throw new IllegalStateException("Can't roll because no more rolls left. Call mayRoll before calling this.");
		}

		for (IDice dice : diceList.keySet()) {
			if (diceList.get(dice) == false) {
				dice.roll();
			}
		}

		rolled();
	}

	protected abstract void rolled();

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
