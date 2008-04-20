package org.examples.yatzy;

import java.io.Serializable;
import java.util.List;

import org.examples.yatzy.score.IScoreCard;

public interface IGame extends Serializable {

	List<IPlayer> getPlayers();

	void addPlayer(IPlayer player);

	void removePlayer(IPlayer player);

	boolean isComplete();

	IGame newGame();

	IScoreCard getScoreCard();

	IRound newRound();

}
