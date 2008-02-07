package org.apache.wicket.examples.yatzy.domain.score;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.examples.yatzy.domain.IPlayer;

public interface IScore extends Serializable {

	void addPlayer(IPlayer player);

	List<IPlayer> getPlayers();

	int getScore(IPlayer player);

	boolean hasScore(IPlayer player);

	/**
	 * @return True if all players has a score
	 */
	boolean isComplete();

}
