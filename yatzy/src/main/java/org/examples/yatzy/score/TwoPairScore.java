package org.examples.yatzy.score;

public class TwoPairScore extends AbstractPairScore {
	private static final long serialVersionUID = 1L;

	public TwoPairScore() {
	}

	@Override
	protected int getNumberOfGroups() {
		return 2;
	}

	@Override
	public TwoPairScore copy() {
		return new TwoPairScore(this);
	}

	private TwoPairScore(TwoPairScore s) {
		super(s);
	}

}