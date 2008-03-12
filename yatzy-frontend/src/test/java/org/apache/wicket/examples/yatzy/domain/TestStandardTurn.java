package org.apache.wicket.examples.yatzy.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestStandardTurn {

	@Test
	public void testRoundtrip() {
		List<IDice> diceList = new ArrayList<IDice>();
		diceList.add(new CubeDice());
		diceList.add(new CubeDice());
		diceList.add(new CubeDice());
		diceList.add(new CubeDice());
		diceList.add(new CubeDice());
		diceList.add(new CubeDice());

		IPlayer player = new AdhocPlayer("Mock Player");

		ITurn turn = new StandardTurn(diceList, 3, player);

		// 3 rolls left
		assertThat(turn.mayRoll(), is(true));

		turn.roll();

		// 2 rolls left
		assertThat(turn.mayRoll(), is(true));

		turn.roll();

		// 1 roll left
		assertThat(turn.mayRoll(), is(true));

		turn.roll();

		// no rolls left
		assertThat(turn.mayRoll(), is(false));
	}

}
