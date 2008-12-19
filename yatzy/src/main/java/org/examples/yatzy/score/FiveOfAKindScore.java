package org.examples.yatzy.score;

public class FiveOfAKindScore extends AbstractOfAKindScore {
	private static final long serialVersionUID = 1L;

	public FiveOfAKindScore() {
	}

	@Override
	public int getValueCount() {
		return 5;
	}

	@Override
	public FiveOfAKindScore copy() {
		return new FiveOfAKindScore(this);
	}

	private FiveOfAKindScore(FiveOfAKindScore s) {
		super(s);
	}

}
