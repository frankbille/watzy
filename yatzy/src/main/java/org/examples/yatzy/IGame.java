package org.examples.yatzy;

import java.io.Serializable;
import java.util.List;

import org.examples.yatzy.score.IScoreCard;

public interface IGame extends Serializable {

	List<IPlayer> getPlayers();

	void addPlayer(IPlayer player);

	IScoreCard getScoreCard();

	IRound newRound();

	boolean isComplete();

	IGame newGame();

}
