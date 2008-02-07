package org.apache.wicket.examples.yatzy.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.examples.yatzy.domain.score.IScoreCard;

public interface IGame extends Serializable {

	List<IPlayer> getPlayers();

	void addPlayer(IPlayer player);

	IScoreCard getScoreCard();

	IRound newRound();

	IGame newGame();

}
