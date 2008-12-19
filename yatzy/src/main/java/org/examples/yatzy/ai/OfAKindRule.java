package org.examples.yatzy.ai;

import java.util.List;

import org.examples.yatzy.IDice;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.IAction.Weight;
import org.examples.yatzy.score.AbstractOfAKindScore;

public class OfAKindRule extends AbstractRule {

	public IAction vote(IPlayer player, ITurn turn, IGame game) {
		IAction action = NoAction.INSTANCE;

		List<AbstractOfAKindScore> ofAKindScores = getScores(game.getScoreCard(), AbstractOfAKindScore.class);
		filterSelectedScores(player, ofAKindScores);

		if (ofAKindScores.isEmpty() == false) {
			DiceStatistics statistics = createDiceStatistics(turn);

			// See if there are of the dices that matches the expected number of
			// dices
			List<IDice> uniqueDiceList = statistics.getUniqueDiceList();
			int highestScore = 0;
			AbstractOfAKindScore highestValueScore = null;

			for (AbstractOfAKindScore ofAKindScore : ofAKindScores) {
				for (IDice dice : uniqueDiceList) {
					if (statistics.getDiceCount(dice) >= ofAKindScore.getValueCount()) {
						AbstractOfAKindScore copyScore = ofAKindScore.copy();
						copyScore.setTurn(turn);

						if (copyScore.getScore(player) > highestScore) {
							highestScore = copyScore.getScore(player);
							highestValueScore = ofAKindScore;
						}
					}
				}
			}

			if (highestValueScore != null) {
				action = new ScoreAction(highestValueScore, Weight.VERY_HIGH);
			}
		}

		return action;
	}

}
