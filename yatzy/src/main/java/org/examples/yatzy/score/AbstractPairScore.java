package org.examples.yatzy.score;

public abstract class AbstractPairScore extends AbstractCountScore {

	@Override
	protected int getGroupCount() {
		return 2;
	}

}
