package org.examples.yatzy.score;

public class ThreePairScore extends AbstractPairScore {
	private static final long serialVersionUID = 1L;

	public ThreePairScore() {
	}

	@Override
	protected int getNumberOfGroups() {
		return 3;
	}

	@Override
	public ThreePairScore copy() {
		return new ThreePairScore(this);
	}

	private ThreePairScore(ThreePairScore s) {
		super(s);
	}

}