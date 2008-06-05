package org.apache.wicket.examples.yatzy.frontend;

import java.io.Serializable;
import java.util.Date;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;

public class Highscore implements Comparable<Highscore>, Serializable {
	private static final long serialVersionUID = 1L;

	private final IGame game;
	private final IPlayer player;
	private final int score;
	private final Date timestamp;

	public Highscore(IGame game, IPlayer player, int score) {
		this.game = game;
		this.player = player;
		this.score = score;
		timestamp = new Date();
	}

	public Class<? extends IGame> getGameType() {
		return game.getClass();
	}

	public IGame getGame() {
		return game;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public String getName() {
		return player.getName();
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
			compare = getGameType().getSimpleName().compareTo(o.getGameType().getSimpleName());

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