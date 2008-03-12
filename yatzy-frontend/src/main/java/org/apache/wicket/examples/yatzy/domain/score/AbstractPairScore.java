package org.apache.wicket.examples.yatzy.domain.score;

public abstract class AbstractPairScore extends AbstractCountScore {

	@Override
	protected int getGroupCount() {
		return 2;
	}

}
