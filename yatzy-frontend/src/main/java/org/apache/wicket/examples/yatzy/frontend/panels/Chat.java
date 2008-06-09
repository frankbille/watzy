package org.apache.wicket.examples.yatzy.frontend.panels;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.examples.yatzy.IPlayer;

public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final class Message implements Serializable {
		private static final long serialVersionUID = 1L;

		private final IPlayer player;
		private final Date timestamp;
		private final String message;

		private Message(IPlayer player, String message) {
			this.message = message;
			this.player = player;
			this.timestamp = new Date();
		}

		public IPlayer getPlayer() {
			return player;
		}

		public String getMessage() {
			return message;
		}

		public Date getTimestamp() {
			return timestamp;
		}

	}

	private final List<Message> messages = new CopyOnWriteArrayList<Message>();
	private final List<IPlayer> players = new CopyOnWriteArrayList<IPlayer>();

	public List<Message> getMessages() {
		return messages;
	}

	public void addMessage(IPlayer player, String message) {
		if (players.contains(player) == false) {
			players.add(player);
		}
		messages.add(new Message(player, message));
	}

	public int getPlayerNumber(IPlayer player) {
		return players.indexOf(player) + 1;
	}

}
