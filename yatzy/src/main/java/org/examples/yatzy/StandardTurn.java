package org.examples.yatzy;

import java.util.List;

public class StandardTurn extends AbstractTurn implements IRollTurn {
	private static final long serialVersionUID = 1L;

	private int rolls;

	public StandardTurn(List<IDice> diceList, int rolls, IPlayer player) {
		super(diceList, player);
		this.rolls = rolls;
	}

	public boolean mayRoll() {
		return rolls > 0;
	}

	public int getRolls() {
		return rolls;
	}

	@Override
	protected void rolled() {
		rolls--;
	}

}
