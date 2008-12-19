package org.examples.yatzy.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.examples.yatzy.GamePrinter;
import org.examples.yatzy.IDice;
import org.examples.yatzy.IGame;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.ai.IAction.Weight;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreGroup;
import org.examples.yatzy.score.ITurnScore;

abstract class AbstractAIPlayer implements AIPlayer {
	private static final long serialVersionUID = 1L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected abstract List<IRule> getRules();

	public void handleTurn(ITurn turn, IGame game) {
		if (handles(game) == false) {
			throw new IllegalArgumentException("This AI player can't handle game of type: "
					+ game.getClass().getSimpleName());
		}

		boolean scoreSet = false;

		while (turn.mayRoll() && scoreSet == false) {
			turn.roll();
			System.out.println("ROLLED: " + GamePrinter.dumpTurn(turn));

			IAction action = determineAction(turn, game);

			if (action instanceof RollAction && turn.mayRoll() == false) {
				action = null;
			}

			// 99. If there are no action, then select the first turn score as a
			// last result.
			if (action == null) {
				ITurnScore turnScore = getTurnScore(game.getScoreCard());
				action = new ScoreAction(turnScore, Weight.VETO);
			}

			if (action instanceof RollAction) {
				RollAction rollAction = (RollAction) action;

				// Set correct hold
				List<IDice> diceList = turn.getDiceList();
				for (IDice dice : diceList) {
					// Reset to unholded
					if (turn.shouldHold(dice)) {
						turn.changeHold(dice);
					}

					// Hold those dices that needs to be holded
					for (IDice holdDice : rollAction.getDicesToHold()) {
						if (dice == holdDice) {
							turn.changeHold(dice);
							break;
						}
					}
				}

				System.out.println("ROLL (W: " + action.getWeight() + "): " + GamePrinter.dumpTurn(turn));
			} else if (action instanceof ScoreAction) {
				ScoreAction scoreAction = (ScoreAction) action;
				ITurnScore turnScore = scoreAction.getTurnScore();
				turnScore.setTurn(turn);
				scoreSet = true;

				System.out.println("SELECT SCORE (W: " + action.getWeight() + "): "
						+ GamePrinter.dumpScore(turnScore, this));
			} else if (action instanceof NoAction) {
				// Do nothing
			} else {
				throw new IllegalStateException("Unknown rule result: " + action);
			}
		}
	}

	private IAction determineAction(ITurn turn, IGame game) {
		IAction endAction = null;

		List<IAction> actions = new ArrayList<IAction>();

		/*
		 * Let the rules vote on what action to take
		 */
		for (IRule rule : getRules()) {
			IAction action = rule.vote(this, turn, game);
			if (action != null) {
				actions.add(action);
			}
		}

		/*
		 * Determine based on the action what to do.
		 */
		// 1. Eliminate those results that doesn't make sense
		filterActions(turn, actions);

		if (actions.isEmpty() == false) {
			// 2. First look at the actions with VETO weight.
			List<ScoreAction> vetoActions = new ArrayList<ScoreAction>();
			for (IAction action : actions) {
				if (action.getWeight() == Weight.VETO) {
					// Also only consider score actions, because we don't won't
					// to honor vetoed roll actions.
					if (action instanceof ScoreAction) {
						vetoActions.add((ScoreAction) action);
					}
				}
			}

			if (vetoActions.isEmpty() == false) {
				// Find the veto action which produces the highest score
				ScoreAction highestScoreAction = null;
				int highestScore = 0;
				for (ScoreAction vetoAction : vetoActions) {
					ITurnScore turnScore = vetoAction.getTurnScore();
					ITurnScore scoreCopy = turnScore.copy();
					scoreCopy.setTurn(turn);

					if (scoreCopy.getScore(this) > highestScore) {
						highestScoreAction = vetoAction;
						highestScore = scoreCopy.getScore(this);
					}
				}

				// The highest score action is the one we go for.
				endAction = highestScoreAction;
			}

			// 3. Go through the results and find out which action group has
			// most weight
			if (endAction == null) {
				Map<Class<? extends IAction>, Double> actionTypeWeights = new HashMap<Class<? extends IAction>, Double>();
				for (IAction action : actions) {
					Class<? extends IAction> actionType = action.getClass();

					double weight = 0;
					if (actionTypeWeights.containsKey(actionType)) {
						weight = actionTypeWeights.get(actionType);
					}

					actionTypeWeights.put(actionType, action.getWeight().getWeight() + weight);
				}

				// Find the result group with the most weight
				Class<? extends IAction> heaviestActionType = null;
				double actionTypeWeight = 0;
				for (Class<? extends IAction> actionType : actionTypeWeights.keySet()) {
					Double weight = actionTypeWeights.get(actionType);
					if (weight > actionTypeWeight) {
						heaviestActionType = actionType;
						actionTypeWeight = weight;
					}
				}

				// 4. Go through all the actions of that type, and find the ones
				// with most weight
				filterActions(heaviestActionType, actions);

				// Find the heaviest weight
				actionTypeWeight = 0;
				for (IAction action : actions) {
					if (action.getWeight().getWeight() > actionTypeWeight) {
						actionTypeWeight = action.getWeight().getWeight();
					}
				}

				// Filter the actions only keeping the ones with the heaviest
				// weight
				List<IAction> heaviestActions = new ArrayList<IAction>();

				for (IAction action : actions) {
					if (action.getWeight().getWeight() == actionTypeWeight) {
						heaviestActions.add(action);
					}
				}

				// If the action type is a ScoreAction, then we find the action
				// which causes the biggest score
				if (heaviestActionType.equals(ScoreAction.class)) {
					IAction heaviestMostValuableAction = null;
					int highestScore = 0;
					for (IAction action : heaviestActions) {
						ScoreAction scoreAction = (ScoreAction) action;
						ITurnScore turnScore = scoreAction.getTurnScore();
						ITurnScore copyScore = turnScore.copy();
						copyScore.setTurn(turn);

						if (copyScore.getScore(this) > highestScore) {
							heaviestMostValuableAction = action;
						}
					}

					endAction = heaviestMostValuableAction;
				} else if (heaviestActionType.equals(RollAction.class)) {
					// We just take the first roll action, there is really
					// nothing else we can do
					if (heaviestActions.isEmpty() == false) {
						endAction = heaviestActions.get(0);
					}
				} else {
					// We don't know how to handle this?!?
				}
			}
		}

		return endAction;
	}

	private void filterActions(Class<? extends IAction> actionType, List<IAction> actions) {
		List<IAction> filteredActions = new ArrayList<IAction>();
		for (IAction action : actions) {
			if (actionType.isInstance(action)) {
				filteredActions.add(action);
			}
		}
		actions.clear();
		actions.addAll(filteredActions);
	}

	private void filterActions(ITurn turn, List<IAction> actions) {
		List<IAction> filteredActions = new ArrayList<IAction>();
		for (IAction action : actions) {
			boolean filter = false;

			// If a rule is a "roll" rule, but there are no more rolls left in
			// the turn, eliminate those.
			if (action instanceof RollAction && turn.mayRoll() == false) {
				filter = true;
			} else if (action instanceof NoAction) {
				filter = true;
			}

			if (filter == false) {
				filteredActions.add(action);
			}
		}
		actions.clear();
		actions.addAll(filteredActions);
	}

	private ITurnScore getTurnScore(IScore score) {
		ITurnScore turnScore = null;

		if (score instanceof ITurnScore) {
			turnScore = (ITurnScore) score;
		} else if (score instanceof IScoreGroup) {
			IScoreGroup scoreGroup = (IScoreGroup) score;
			List<IScore> scores = scoreGroup.getScores();
			for (IScore childScore : scores) {
				ITurnScore ts = getTurnScore(childScore);
				if (ts != null) {
					if (ts.hasScore(this) == false) {
						turnScore = ts;
						break;
					}
				}
			}
		}

		return turnScore;
	}

}
