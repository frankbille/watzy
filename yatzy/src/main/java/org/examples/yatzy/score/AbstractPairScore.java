package org.examples.yatzy.score;

public abstract class AbstractPairScore extends AbstractCountScore {
	private static final long serialVersionUID = 1L;

	public AbstractPairScore() {
	}

	@Override
	protected int getGroupCount() {
		return 2;
	}

	@Override
	public abstract AbstractPairScore copy();

	protected AbstractPairScore(AbstractPairScore s) {
		super(s);
	}

}
