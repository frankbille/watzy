package org.examples.yatzy.ai;

import java.util.List;

import org.examples.yatzy.IDice;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.IAction.Weight;
import org.examples.yatzy.score.SumScore;

public class SumScoreRule extends AbstractRule {

	private final int diceCountToGetBonus;

	public SumScoreRule(int diceCountToGetBonus) {
		this.diceCountToGetBonus = diceCountToGetBonus;
	}

	public IAction vote(IPlayer player, ITurn turn, IGame game) {
		IAction action = NoAction.INSTANCE;

		// 1. Determine if there are any available sum scores left
		List<SumScore> sumScores = getScores(game.getScoreCard(), SumScore.class);
		filterSelectedScores(player, sumScores);

		if (sumScores.isEmpty() == false) {
			// Find out if there is statistics for going after or selecting one
			// of the available sum scores based on which dices has been rolled.
			DiceStatistics statistics = createDiceStatistics(turn);

			SumScore sumScoreToBetOn = new SumScore(0);
			int sumScoreDiceCount = 0;
			for (SumScore sumScore : sumScores) {
				int diceCount = statistics.getDiceCount(sumScore.getValue());
				if (diceCount >= sumScoreDiceCount && sumScore.getValue() > sumScoreToBetOn.getValue()) {
					sumScoreToBetOn = sumScore;
					sumScoreDiceCount = diceCount;
				}
			}

			// Determine maximum number of dices can be used
			int maxNumberOfDices = turn.getDiceList().size();

			if (sumScoreDiceCount < maxNumberOfDices) {
				List<IDice> diceListToHold = statistics.getDiceList(sumScoreToBetOn.getValue());
				Weight weight = Weight.NONE;

				if (sumScoreDiceCount < diceCountToGetBonus - 1) {
					weight = Weight.LOW;
				} else if (sumScoreDiceCount == diceCountToGetBonus - 1) {
					weight = Weight.HIGH;
				} else if (sumScoreDiceCount >= diceCountToGetBonus) {
					weight = Weight.VERY_HIGH;
				}

				if (turn.mayRoll()) {
					action = new RollAction(diceListToHold, weight);
				} else {
					action = new ScoreAction(sumScoreToBetOn, weight);
				}
			} else if (sumScoreDiceCount == maxNumberOfDices) {
				action = new ScoreAction(sumScoreToBetOn, Weight.VETO);
			}
		}

		return action;
	}
}
