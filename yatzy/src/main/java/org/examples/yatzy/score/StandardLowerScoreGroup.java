package org.examples.yatzy.score;

public class StandardLowerScoreGroup extends AbstractScoreGroup {
	private static final long serialVersionUID = 1L;

	public StandardLowerScoreGroup() {
		addScore(new OnePairScore());
		addScore(new TwoPairScore());

		addScore(new ThreeOfAKindScore());
		addScore(new FourOfAKindScore());

		addScore(new SmallStraightScore());
		addScore(new LargeStraightScore());

		addScore(new FullHouseScore());

		addScore(new ChanceScore());
		addScore(new YatzyScore());
	}

	@Override
	public StandardLowerScoreGroup copy() {
		return new StandardLowerScoreGroup(this);
	}

	private StandardLowerScoreGroup(StandardLowerScoreGroup s) {
		super(s);
	}

}
