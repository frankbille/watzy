package org.examples.yatzy.ai;

public class NoAction implements IAction {

	public static final NoAction INSTANCE = new NoAction();

	public Weight getWeight() {
		return Weight.NONE;
	}

	private NoAction() {
	}

}
