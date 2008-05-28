package org.examples.yatzy.score;

public abstract class AbstractPairScore extends AbstractCountScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getGroupCount() {
		return 2;
	}

}
