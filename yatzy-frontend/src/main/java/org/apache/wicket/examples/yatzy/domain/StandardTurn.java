package org.apache.wicket.examples.yatzy.domain;

import java.util.List;

public class StandardTurn extends AbstractTurn {
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