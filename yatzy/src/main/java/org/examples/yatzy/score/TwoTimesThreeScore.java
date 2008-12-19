package org.examples.yatzy.score;

public class TwoTimesThreeScore extends AbstractCountScore {
	private static final long serialVersionUID = 1L;

	public TwoTimesThreeScore() {
	}

	@Override
	protected int getGroupCount() {
		return 3;
	}

	@Override
	protected int getNumberOfGroups() {
		return 2;
	}

	@Override
	public TwoTimesThreeScore copy() {
		return new TwoTimesThreeScore(this);
	}

	private TwoTimesThreeScore(TwoTimesThreeScore s) {
		super(s);
	}

}
