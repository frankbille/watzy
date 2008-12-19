package org.examples.yatzy.ai;

public interface IAction {

	public static enum Weight {
		NONE(-1), LOW(1), MEDIUM(4), HIGH(16), VERY_HIGH(128), VETO(1024);

		private final double weight;

		private Weight(double weight) {
			this.weight = weight;
		}

		public double getWeight() {
			return weight;
		}
	}

	Weight getWeight();

}
