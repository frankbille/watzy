package org.examples.yatzy;

import java.text.NumberFormat;
import java.util.List;

import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.MaxiGame;
import org.examples.yatzy.StandardGame;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreGroup;
import org.examples.yatzy.score.ITurnScore;
import org.examples.yatzy.score.SumScore;
import org.junit.Test;

public class TestDice {

	private static interface ScoreFactory {
		ITurnScore createScore();
	}

	private static class SimpleScoreFactory implements ScoreFactory {
		private final Class<? extends ITurnScore> scoreClass;

		public SimpleScoreFactory(Class<? extends ITurnScore> scoreClass) {
			this.scoreClass = scoreClass;
		}

		public ITurnScore createScore() {
			ITurnScore score = null;

			try {
				score = createInstance(scoreClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return score;
		}

		protected ITurnScore createInstance(Class<? extends ITurnScore> scoreClass) throws Exception {
			return scoreClass.newInstance();
		}
	}

	private static class SimpleSumScoreFactory extends SimpleScoreFactory {
		private final int value;

		public SimpleSumScoreFactory(int value) {
			super(SumScore.class);
			this.value = value;
		}

		@Override
		protected ITurnScore createInstance(Class<? extends ITurnScore> scoreClass) throws Exception {
			return (ITurnScore) scoreClass.getConstructors()[0].newInstance(value);
		}
	}

	@Test
	public void testStandardGame() {
		calcGame(new StandardGame());
	}

	@Test
	public void testMaxiGame() {
		calcGame(new MaxiGame());
	}

	private void calcGame(IGame game) {
		System.out.println("Calculating properbilities for the: " + game.getClass().getSimpleName());

		IPlayer player = new AdhocPlayer("Test Player");
		game.addPlayer(player);

		calcScores(game.getScoreCard().getScores(), game);
		
		System.out.println("--");
	}

	private void calcScores(List<IScore> scores, IGame game) {
		for (IScore score : scores) {

			if (score instanceof ITurnScore) {
				ITurnScore turnScore = (ITurnScore) score;

				if (score instanceof SumScore) {
					SumScore sumScore = (SumScore) score;
					System.out.println("SumScore: " + sumScore.getValue());
					calcProperbilities(100000, game, new SimpleSumScoreFactory(sumScore.getValue()));
				} else {
					System.out.println(score.getClass().getSimpleName());
					calcProperbilities(100000, game, new SimpleScoreFactory(turnScore.getClass()));
				}
			} else if (score instanceof IScoreGroup) {
				IScoreGroup scoreGroup = (IScoreGroup) score;
				calcScores(scoreGroup.getScores(), game);
			}
		}
	}

	private void calcProperbilities(int rollCount, IGame game, ScoreFactory scoreFactory) {
		IPlayer player = game.getPlayers().get(0);

		int scoreCount = 0;

		for (int i = 0; i < rollCount; i++) {
			ITurnScore score = scoreFactory.createScore();
			score.addPlayer(player);

			ITurn turn = game.newRound().nextTurn();

			turn.roll();
			score.setTurn(turn);

			if (score.getScore(player) > 0) {
				scoreCount++;
			}
		}

		double prop = (double) scoreCount / (double) rollCount;

		System.out.println(NumberFormat.getNumberInstance().format(prop * 100) + "%");
	}

}
