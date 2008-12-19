package org.examples.yatzy.ai;

import java.util.List;

import org.examples.yatzy.IDice;

public class RollAction extends AbstractAction {

	private final List<IDice> dicesToHold;

	public RollAction(List<IDice> dicesToHold, Weight weight) {
		super(weight);

		this.dicesToHold = dicesToHold;
	}

	public List<IDice> getDicesToHold() {
		return dicesToHold;
	}

}
