package org.examples.yatzy.score;

public class FourOfAKindScore extends AbstractOfAKindScore {
	private static final long serialVersionUID = 1L;

	public FourOfAKindScore() {
	}

	@Override
	public int getValueCount() {
		return 4;
	}

	@Override
	public FourOfAKindScore copy() {
		return new FourOfAKindScore(this);
	}

	private FourOfAKindScore(FourOfAKindScore s) {
		super(s);
	}

}
