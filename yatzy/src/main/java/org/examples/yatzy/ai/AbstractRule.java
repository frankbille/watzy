package org.examples.yatzy.ai;

import java.util.ArrayList;
import java.util.List;

import org.examples.yatzy.IDice;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreGroup;

abstract class AbstractRule implements IRule {

	protected static interface IScoreVisitor {
		void visit(IScore score);
	}

	protected static class DiceStatistics {
		private final List<IDice> diceList;

		public DiceStatistics(ITurn turn) {
			this(turn.getDiceList());
		}

		public DiceStatistics(List<IDice> diceList) {
			this.diceList = diceList;
		}

		public List<IDice> getUniqueDiceList() {
			List<IDice> uniqueDiceList = new ArrayList<IDice>();

			for (IDice dice : diceList) {
				boolean contains = false;
				for (IDice dice2 : uniqueDiceList) {
					if (dice.equals(dice2)) {
						contains = true;
						break;
					}
				}
				if (contains == false) {
					uniqueDiceList.add(dice);
				}
			}

			return uniqueDiceList;
		}

		public int getDiceCount(IDice selectedDice) {
			int count = 0;

			for (IDice dice : diceList) {
				if (dice.equals(selectedDice)) {
					count++;
				}
			}

			return count;
		}

		public int getDiceCount(final int diceValue) {
			return getDiceCount(new IDice() {
				private static final long serialVersionUID = 1L;

				public int getValue() {
					return diceValue;
				}

				public boolean hasValue() {
					return true;
				}

				public void roll() {

				}
			});
		}

		public List<IDice> getDiceList(int diceValue) {
			List<IDice> filteredDiceList = new ArrayList<IDice>();

			for (IDice dice : diceList) {
				if (dice.hasValue() && dice.getValue() == diceValue) {
					filteredDiceList.add(dice);
				}
			}

			return filteredDiceList;
		}

		public List<IDice> getDiceListByDiceCount(int diceCount) {
			List<IDice> filteredDiceList = new ArrayList<IDice>();

			for (IDice dice : getUniqueDiceList()) {
				if (getDiceCount(dice) == diceCount) {
					filteredDiceList.add(dice);
				}
			}

			return filteredDiceList;
		}

	}

	protected void traverse(IScore score, IScoreVisitor scoreVisitor) {
		scoreVisitor.visit(score);

		if (score instanceof IScoreGroup) {
			IScoreGroup scoreGroup = (IScoreGroup) score;
			for (IScore childScore : scoreGroup.getScores()) {
				traverse(childScore, scoreVisitor);
			}
		}
	}

	protected <S extends IScore> List<S> getScores(IScore score, final Class<S> scoreType) {
		final List<S> scores = new ArrayList<S>();

		traverse(score, new IScoreVisitor() {
			@SuppressWarnings("unchecked")
			public void visit(IScore score) {
				if (scoreType.isInstance(score)) {
					scores.add((S) score);
				}
			}
		});

		return scores;
	}

	protected DiceStatistics createDiceStatistics(ITurn turn) {
		DiceStatistics statistics = new DiceStatistics(turn);

		return statistics;
	}

	protected <S extends IScore> void filterSelectedScores(IPlayer player, List<S> scores) {
		List<S> filteredScores = new ArrayList<S>();

		for (S score : scores) {
			if (score.hasScore(player) == false) {
				filteredScores.add(score);
			}
		}

		scores.clear();
		scores.addAll(filteredScores);
	}

}
