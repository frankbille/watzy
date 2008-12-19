package org.examples.yatzy.score;

public class OnePairScore extends AbstractPairScore {
	private static final long serialVersionUID = 1L;

	public OnePairScore() {
	}

	@Override
	protected int getNumberOfGroups() {
		return 1;
	}

	@Override
	public AbstractPairScore copy() {
		return new OnePairScore(this);
	}

	private OnePairScore(OnePairScore s) {
		super(s);
	}

}