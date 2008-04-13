package org.examples.yatzy.score;

import java.io.Serializable;
import java.util.List;

import org.examples.yatzy.IPlayer;

public interface IScore extends Serializable {

	void addPlayer(IPlayer player);

	void removePlayer(IPlayer player);

	List<IPlayer> getPlayers();

	int getScore(IPlayer player);

	boolean hasScore(IPlayer player);

	/**
	 * @return True if all players has a score
	 */
	boolean isComplete();

}
