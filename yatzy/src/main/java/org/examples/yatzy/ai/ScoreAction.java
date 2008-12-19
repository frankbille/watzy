package org.examples.yatzy.ai;

import org.examples.yatzy.score.ITurnScore;

public class ScoreAction extends AbstractAction {
	private final ITurnScore turnScore;

	public ScoreAction(ITurnScore turnScore, Weight weight) {
		super(weight);

		if (turnScore == null) {
			throw new IllegalArgumentException("Score may not be null");
		}

		this.turnScore = turnScore;
	}

	public ITurnScore getTurnScore() {
		return turnScore;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append(ScoreAction.class.getSimpleName());
		b.append(" - Selected Score: ");
		b.append(turnScore);
		b.append(", Weight: ");
		b.append(getWeight());

		return b.toString();
	}

}
