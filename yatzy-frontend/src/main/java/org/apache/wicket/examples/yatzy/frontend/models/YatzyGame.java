package org.apache.wicket.examples.yatzy.frontend.models;

import java.io.Serializable;

import javax.persistence.Id;

import org.examples.yatzy.IGame;

import com.googlecode.objectify.annotation.Serialized;

public class YatzyGame implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;
	@Serialized
	private IGame game;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setGame(IGame game) {
		this.game = game;
	}

	public IGame getGame() {
		return game;
	}
	
}
