package org.apache.wicket.examples.yatzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.examples.yatzy.behaviours.HeartBeatBehavior.HeartBeat;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.score.IScoreCard;

public class MultiPlayerGame implements IGame {
	private static final long serialVersionUID = 1L;

	public static class Player implements IPlayer, Serializable {
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

		public String getName() {
			return player.getName();
		}
	}

	public static class PlayerHeartBeat implements HeartBeat {
		private static final long serialVersionUID = 1L;

		private Date lastPing;

		public PlayerHeartBeat() {
			ping();
		}

		public void ping() {
			lastPing = new Date();
		}

		public long getTimeSinceLastPing() {
			return new Date().getTime() - lastPing.getTime();
		}
	}

	private final int maxPlayers;

	private final IGame game;

	private final HashMap<Player, PlayerHeartBeat> playerHeartBeats;

	private MultiPlayerRound currentRound;

	public MultiPlayerGame(int maxPlayers, IGame game) {
		this.maxPlayers = maxPlayers;
		this.game = game;
		playerHeartBeats = new HashMap<Player, PlayerHeartBeat>();
	}

	public boolean isCurrent(IPlayer player) {
		boolean current = false;

		synchronized (game) {
			for (IPlayer p : game.getPlayers()) {
				Player localPlayer = (Player) p;
				if (player == localPlayer) {
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
		synchronized (game) {
			if (game.getPlayers().size() >= maxPlayers) {
				throw new IllegalArgumentException("Can't add more players");
			}

			Player localPlayer = new Player(player, YatzySession.get());
			game.addPlayer(localPlayer);

			playerHeartBeats.put(localPlayer, new PlayerHeartBeat());
		}
	}

	public void removePlayer(IPlayer player) {
		synchronized (game) {
			game.removePlayer(player);
			playerHeartBeats.remove(player);
		}
	}

	public int getNumberOfPlayers() {
		synchronized (game) {
			return game.getPlayers().size();
		}
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public boolean isReady() {
		return getNumberOfPlayers() == getMaxPlayers();
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

	public Player getMe() {
		Player player = null;

		synchronized (game) {
			for (IPlayer p : game.getPlayers()) {
				Player localPlayer = (Player) p;
				if (localPlayer.getSession() == YatzySession.get()) {
					player = localPlayer;
					break;
				}
			}
		}

		return player;
	}

	public PlayerHeartBeat getHeartBeatForPlayer(Player player) {
		return playerHeartBeats.get(player);
	}

	public List<PlayerHeartBeat> getHeartBeats() {
		return new ArrayList<PlayerHeartBeat>(playerHeartBeats.values());
	}
}
