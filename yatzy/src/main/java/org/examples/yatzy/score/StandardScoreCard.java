package org.examples.yatzy.score;

public class StandardScoreCard extends AbstractScoreGroup implements IScoreCard {
	private static final long serialVersionUID = 1L;

	public StandardScoreCard() {
	}

	@Override
	public StandardScoreCard copy() {
		return new StandardScoreCard(this);
	}

	private StandardScoreCard(StandardScoreCard s) {
		super(s);
	}

}
