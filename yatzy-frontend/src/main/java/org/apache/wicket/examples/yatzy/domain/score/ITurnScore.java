package org.apache.wicket.examples.yatzy.domain.score;

import org.apache.wicket.examples.yatzy.domain.ITurn;

public interface ITurnScore extends IScore {

	void setTurn(ITurn turn);

}
