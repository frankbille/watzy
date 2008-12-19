package org.examples.yatzy.score;

public class YatzyScore extends AbstractYatzyScore {
	private static final long serialVersionUID = 1L;

	public YatzyScore() {
	}

	@Override
	protected int getYatzyScore(int value) {
		return 50;
	}

	@Override
	public YatzyScore copy() {
		return new YatzyScore(this);
	}

	private YatzyScore(YatzyScore s) {
		super(s);
	}

}
