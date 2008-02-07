package org.apache.wicket.examples.yatzy.domain.score;

public class OnePairScore extends AbstractPairScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getNumberOfGroups() {
		return 1;
	}

}