package org.apache.wicket.examples.yatzy.domain;

public abstract class AbstractPlayer implements IPlayer {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}