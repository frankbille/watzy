package org.apache.wicket.examples.yatzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.score.IScoreCard;

public class MultiPlayerGame implements IGame {
	private static final long serialVersionUID = 1L;

	public static class Player implements Serializable {
		private static final long serialVersionUID = 1L;

		private final IPlayer player;
		private final YatzySession session;

		public Player(IPlayer player, YatzySession session) {
			this.player = player;
			this.session = session;
		}

		public IPlayer getPlayer() {
			return player;
		}

		public YatzySession getSession() {
			return session;
		}
	}

	private final List<Player> players = new ArrayList<Player>();

	private final int maxPlayers;

	private final IGame game;

	private MultiPlayerRound currentRound;

	public MultiPlayerGame(int maxPlayers, IGame game) {
		this.maxPlayers = maxPlayers;
		this.game = game;
	}

	public boolean isCurrent(IPlayer player) {
		boolean current = false;

		synchronized (players) {
			for (Player localPlayer : players) {
				if (player == localPlayer.getPlayer()) {
					if (YatzySession.get() == localPlayer.getSession()) {
						current = true;
					}

					break;
				}
			}
		}

		return current;
	}

	public boolean isPlaying() {
		boolean playing = false;

		synchronized (currentRound) {
			playing = isCurrent(currentRound.getCurrentPlayer());
		}

		return playing;
	}

	public IGame getInnerGame() {
		return game;
	}

	public boolean isAvailable() {
		return getNumberOfPlayers() < getMaxPlayers();
	}

	public void addPlayer(IPlayer player) {
		synchronized (players) {
			synchronized (game) {
				if (players.size() >= maxPlayers) {
					throw new IllegalArgumentException("Can't add more players");
				}

				players.add(new Player(player, YatzySession.get()));

				game.addPlayer(player);
			}
		}
	}

	public void removePlayer(IPlayer player) {
		synchronized (players) {
			synchronized (game) {
				if (hasPlayer(player) == false) {
					throw new IllegalArgumentException("The player hasn't been added");
				}

				players.remove(player);

				game.removePlayer(player);
			}
		}
	}

	private boolean hasPlayer(IPlayer player) {
		boolean hasPlayer = false;

		for (Player localPlayer : players) {
			if (player == localPlayer.getPlayer()) {
				hasPlayer = true;
				break;
			}
		}

		return hasPlayer;
	}

	public int getNumberOfPlayers() {
		synchronized (players) {
			return players.size();
		}
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public List<IPlayer> getPlayers() {
		return game.getPlayers();
	}

	public IScoreCard getScoreCard() {
		return game.getScoreCard();
	}

	public boolean isComplete() {
		return game.isComplete();
	}

	public IGame newGame() {
		return null;
	}

	public IRound newRound() {
		MultiPlayerRound round = null;

		if (currentRound != null) {
			if (currentRound.hasMoreTurns()) {
				round = currentRound;
			}
		}

		if (round == null) {
			round = new MultiPlayerRound(game.newRound(), this);
		}

		currentRound = round;

		return round;
	}

	public MultiPlayerRound getCurrentRound() {
		return currentRound;
	}
}
