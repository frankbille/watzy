package org.examples.yatzy.ai;

import java.util.List;

import org.examples.yatzy.IDice;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.IAction.Weight;
import org.examples.yatzy.score.AbstractYatzyScore;

public class YatzyScoreRule extends AbstractRule {

	public IAction vote(IPlayer player, ITurn turn, IGame game) {
		IAction action = NoAction.INSTANCE;

		List<AbstractYatzyScore> yatzyScores = getScores(game.getScoreCard(), AbstractYatzyScore.class);
		filterSelectedScores(player, yatzyScores);

		if (yatzyScores.isEmpty() == false) {
			// Just select the first one. It doesn't matter which one we focus
			// on.
			AbstractYatzyScore yatzyScore = yatzyScores.get(0);

			// Don't follow the yatzy unless we are very close to getting it
			int diceCount = turn.getDiceList().size();

			DiceStatistics statistics = createDiceStatistics(turn);

			List<IDice> almostYatzyDiceList = statistics.getDiceListByDiceCount(diceCount - 1);
			if (turn.mayRoll() && almostYatzyDiceList.isEmpty() == false) {
				action = new RollAction(statistics.getDiceList(almostYatzyDiceList.get(0).getValue()), Weight.HIGH);
			} else if (statistics.getDiceListByDiceCount(diceCount).isEmpty() == false) {
				action = new ScoreAction(yatzyScore, Weight.VETO);
			}
		}

		return action;
	}

}
