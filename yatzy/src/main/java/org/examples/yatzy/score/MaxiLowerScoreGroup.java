package org.examples.yatzy.score;

public class MaxiLowerScoreGroup extends AbstractScoreGroup {
	private static final long serialVersionUID = 1L;

	public MaxiLowerScoreGroup() {
		addScore(new OnePairScore());
		addScore(new TwoPairScore());
		addScore(new ThreePairScore());

		addScore(new ThreeOfAKindScore());
		addScore(new FourOfAKindScore());
		addScore(new FiveOfAKindScore());

		addScore(new SmallStraightScore());
		addScore(new LargeStraightScore());
		addScore(new MaxiFullStraightScore());

		addScore(new FullHouseScore());
		addScore(new TwoTimesThreeScore());

		addScore(new ChanceScore());
		addScore(new MaxiYatzyScore());
	}

	@Override
	public AbstractScoreGroup copy() {
		return new MaxiLowerScoreGroup(this);
	}

	private MaxiLowerScoreGroup(MaxiLowerScoreGroup s) {
		super(s);
	}

}
