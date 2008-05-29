package org.apache.wicket.examples.yatzy.frontend;

import java.util.Date;

import org.examples.yatzy.IGame;

public class Highscore implements Comparable<Highscore> {
	private final Class<? extends IGame> gameType;
	private final String name;
	private final int score;
	private final Date timestamp;

	public Highscore(Class<? extends IGame> gameType, String name, int score) {
		this.gameType = gameType;
		this.name = name;
		this.score = score;
		timestamp = new Date();
	}

	public Class<? extends IGame> getGameType() {
		return gameType;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int compareTo(Highscore o) {
		int compare = 0;

		if (o != null) {
			compare = gameType.getName().compareTo(o.getGameType().getName());

			if (compare == 0) {
				compare = -1 * new Integer(score).compareTo(o.getScore());

				if (compare == 0) {
					compare = timestamp.compareTo(o.getTimestamp());
				}
			}
		} else {
			compare = -1;
		}

		return compare;
	}
}