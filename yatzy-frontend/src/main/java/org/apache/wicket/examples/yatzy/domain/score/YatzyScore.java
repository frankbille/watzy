package org.apache.wicket.examples.yatzy.domain.score;

public class YatzyScore extends AbstractYatzyScore {
	private static final long serialVersionUID = 1L;

	@Override
	protected int getYatzyScore(int value) {
		return 50;
	}

}