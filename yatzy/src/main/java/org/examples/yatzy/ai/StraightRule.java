package org.examples.yatzy.ai;

import java.util.List;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.IAction.Weight;
import org.examples.yatzy.score.AbstractStraightScore;

public class StraightRule extends AbstractRule {

	public IAction vote(IPlayer player, ITurn turn, IGame game) {
		IAction action = NoAction.INSTANCE;

		// Find available straight scores
		List<AbstractStraightScore> straightScores = getScores(game.getScoreCard(), AbstractStraightScore.class);
		filterSelectedScores(player, straightScores);

		if (straightScores.isEmpty() == false) {
			// First find out if any of the straight scores matches the roll.
			AbstractStraightScore mostValuableStraightScore = null;
			int score = 0;
			for (AbstractStraightScore straightScore : straightScores) {
				AbstractStraightScore copy = straightScore.copy();
				copy.setTurn(turn);
				if (copy.getScore(player) > score) {
					mostValuableStraightScore = straightScore;
					score = copy.getScore(player);
				}
			}

			// If there is a
			if (mostValuableStraightScore != null) {
				action = new ScoreAction(mostValuableStraightScore, Weight.VERY_HIGH);
			}
		}

		return action;
	}
}
