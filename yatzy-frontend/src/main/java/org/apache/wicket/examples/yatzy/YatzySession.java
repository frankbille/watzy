package org.apache.wicket.examples.yatzy;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IPlayer;

public class YatzySession extends WebSession {
	private static final long serialVersionUID = 1L;

	public static YatzySession get() {
		return (YatzySession) WebSession.get();
	}

	private IPlayer player;

	public YatzySession(Request request) {
		super(request);
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public void createAdhocPlayer(String name) {
		setPlayer(new AdhocPlayer(name));
	}

	public void setPlayer(IPlayer player) {
		if (player == null) {
			throw new IllegalArgumentException("Player can not be null");
		}

		this.player = player;
	}

	public IPlayer getPlayer() {
		return player;
	}

}
