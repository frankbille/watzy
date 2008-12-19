package org.examples.yatzy.ai;

abstract class AbstractAction implements IAction {

	private final Weight weight;

	public AbstractAction(Weight weight) {
		this.weight = weight;
	}

	public Weight getWeight() {
		return weight;
	}

}
