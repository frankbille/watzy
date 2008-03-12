package org.apache.wicket.examples.yatzy.domain.score;

public class TwoTimesThreeScore extends AbstractCountScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getGroupCount() {
		return 3;
	}

	@Override
	protected int getNumberOfGroups() {
		return 2;
	}

}
