package org.examples.yatzy.score;

public class ThreeOfAKindScore extends AbstractOfAKindScore {
	private static final long serialVersionUID = 1L;

	public ThreeOfAKindScore() {
	}

	@Override
	public int getValueCount() {
		return 3;
	}

	@Override
	public ThreeOfAKindScore copy() {
		return new ThreeOfAKindScore(this);
	}

	private ThreeOfAKindScore(ThreeOfAKindScore s) {
		super(s);
	}

}
