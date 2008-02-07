package org.apache.wicket.examples.yatzy.domain.score;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.examples.yatzy.domain.IPlayer;

public abstract class AbstractScoreGroup implements IScoreGroup {

	private final List<IScore> scores = new ArrayList<IScore>();

	public List<IScore> getScores() {
		return scores;
	}

	public void addScore(IScore score) {
		scores.add(score);

		for (IPlayer player : getPlayers()) {
			for (IScore s : scores) {
				s.addPlayer(player);
			}
		}
	}

	public void addPlayer(IPlayer player) {
		for (IScore score : scores) {
			score.addPlayer(player);
		}
	}

	public List<IPlayer> getPlayers() {
		Set<IPlayer> players = new LinkedHashSet<IPlayer>();
		for (IScore score : scores) {
			players.addAll(score.getPlayers());
		}
		return new ArrayList<IPlayer>(players);
	}

	public int getScore(IPlayer player) {
		if (hasScore(player) == false) {
			throw new IllegalArgumentException("Don't get scores when there is no scores to show.");
		}

		int score = 0;

		for (IScore s : scores) {
			if (s.hasScore(player)) {
				score += s.getScore(player);
			}
		}

		return score;
	}

	public boolean hasScore(IPlayer player) {
		boolean hasScore = false;

		for (IScore score : scores) {
			if (score.hasScore(player)) {
				hasScore = true;
				break;
			}
		}

		return hasScore;
	}

	public boolean isComplete() {
		boolean complete = true;

		for (IScore score : scores) {
			if (score.isComplete() == false) {
				complete = false;
				break;
			}
		}

		return complete;
	}

}
