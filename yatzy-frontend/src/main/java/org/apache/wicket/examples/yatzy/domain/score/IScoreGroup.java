package org.apache.wicket.examples.yatzy.domain.score;

import java.util.List;

public interface IScoreGroup extends IScore {

	List<IScore> getScores();

}
