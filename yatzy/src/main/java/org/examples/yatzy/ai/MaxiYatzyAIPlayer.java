package org.examples.yatzy.ai;

import java.util.ArrayList;
import java.util.List;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IGameWrapper;
import org.examples.yatzy.MaxiGame;

public class MaxiYatzyAIPlayer extends AbstractAIPlayer {
	private static final long serialVersionUID = 1L;

	private static final List<IRule> rules;

	static {
		rules = new ArrayList<IRule>();
		rules.add(new SumScoreRule(4));
		rules.add(new OfAKindRule());
		rules.add(new StraightRule());
		rules.add(new YatzyScoreRule());
	}

	@Override
	protected List<IRule> getRules() {
		return rules;
	}

	public boolean handles(IGame game) {
		return game instanceof MaxiGame
				|| (game instanceof IGameWrapper && ((IGameWrapper) game).getInnerGame() instanceof MaxiGame);
	}

}
