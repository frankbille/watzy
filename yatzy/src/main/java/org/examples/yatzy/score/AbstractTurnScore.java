package org.examples.yatzy.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.examples.yatzy.IDice;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;

public abstract class AbstractTurnScore implements ITurnScore {

	private final Map<IPlayer, Integer> playerScores = new LinkedHashMap<IPlayer, Integer>();

	public void addPlayer(IPlayer player) {
		if (playerScores.containsKey(player) == false) {
			playerScores.put(player, null);
		}
	}

	public void removePlayer(IPlayer player) {
		playerScores.remove(player);
	}

	public List<IPlayer> getPlayers() {
		return new ArrayList<IPlayer>(playerScores.keySet());
	}

	public void setTurn(ITurn turn) {
		checkPlayer(turn.getPlayer());

		int score = calculateScore(turn);

		playerScores.put(turn.getPlayer(), score);
	}

	public int getScore(IPlayer player) {
		checkPlayer(player);

		if (playerScores.get(player) == null) {
			throw new IllegalStateException(
					"Don't try to get the score if it hasn't been set. Use the hasScore to check before getting the score.");
		}

		return playerScores.get(player);
	}

	public boolean hasScore(IPlayer player) {
		checkPlayer(player);

		return playerScores.get(player) != null;
	}

	public boolean isComplete() {
		boolean complete = true;

		for (IPlayer player : getPlayers()) {
			if (hasScore(player) == false) {
				complete = false;
				break;
			}
		}

		return complete;
	}

	/**
	 * Get values sorted byt natural order so smallest is first and largest is
	 * last
	 */
	protected List<Integer> getValues(ITurn turn) {
		List<Integer> values = new ArrayList<Integer>();

		for (IDice dice : turn.getDiceList()) {
			if (dice.hasValue()) {
				int value = dice.getValue();
				if (values.contains(value) == false) {
					values.add(value);
				}
			}
		}

		Collections.sort(values);

		return values;
	}

	protected int[] getValueCount(ITurn turn, int... values) {
		int[] counts = new int[values.length];

		for (IDice dice : turn.getDiceList()) {
			if (dice.hasValue()) {
				for (int i = 0; i < values.length; i++) {
					int value = values[i];
					if (dice.getValue() == value) {
						counts[i]++;
					}
				}
			}
		}

		return counts;
	}

	protected int getValueCount(ITurn turn, int value) {
		int count = 0;

		for (IDice dice : turn.getDiceList()) {
			if (dice.hasValue()) {
				if (dice.getValue() == value) {
					count++;
				}
			}
		}

		return count;
	}

	protected abstract int calculateScore(ITurn turn);

	private void checkPlayer(IPlayer player) {
		if (playerScores.containsKey(player) == false) {
			throw new IllegalArgumentException("Player not found in the score list. Perhaps it hasn't been added?");
		}
	}

}
