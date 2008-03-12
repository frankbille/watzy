package org.examples.yatzy;

import java.util.List;

import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.StandardLowerScoreGroup;
import org.examples.yatzy.score.StandardScoreCard;
import org.examples.yatzy.score.StandardUpperScoreGroup;

public class StandardGame extends AbstractStandardGame {
	private static final long serialVersionUID = 1L;

	private final IDiceFactory diceFactory;

	public StandardGame() {
		diceFactory = new StandardDiceFactory() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void addDice(List<IDice> diceList) {
				diceList.add(new CubeDice());
				diceList.add(new CubeDice());
				diceList.add(new CubeDice());
				diceList.add(new CubeDice());
				diceList.add(new CubeDice());
			}
		};
	}

	@Override
	protected IDiceFactory getDiceFactory() {
		return diceFactory;
	}

	@Override
	protected int getRollsCount() {
		return 3;
	}

	@Override
	protected IGame createNewGame() {
		return new StandardGame();
	}

	@Override
	protected IScoreCard createScoreCard() {
		StandardScoreCard sc = new StandardScoreCard();

		sc.addScore(new StandardUpperScoreGroup());
		sc.addScore(new StandardLowerScoreGroup());

		return sc;
	}

}
