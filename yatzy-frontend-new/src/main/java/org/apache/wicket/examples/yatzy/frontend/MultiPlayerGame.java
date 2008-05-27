package org.apache.wicket.examples.yatzy.frontend;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Session;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.score.IScoreCard;

public class MultiPlayerGame implements IGame {
	private static final long serialVersionUID = 1L;

	public static enum GameStatus {
		SETTING_UP, STARTED, COMPLETE
	}

	private static class Player implements IPlayer, Serializable {
		private static final long serialVersionUID = 1L;

		private final IPlayer player;
		private final Session session;

		public Player(IPlayer player, Session session) {
			this.player = player;
			this.session = session;
		}

		public IPlayer getPlayer() {
			return player;
		}

		public Session getSession() {
			return session;
		}

		public String getName() {
			return player.getName();
		}
	}

	private final IGame game;

	private MultiPlayerRound currentRound;

	public MultiPlayerGame(IGame game) {
		this.game = game;
	}

	public IGame getInnerGame() {
		return game;
	}

	public void addPlayer(IPlayer player) {
		synchronized (game) {
			Player localPlayer = new Player(player, Session.get());
			game.addPlayer(localPlayer);
		}
	}

	public void removePlayer(IPlayer player) {
		synchronized (game) {
			game.removePlayer(player);
		}
	}

	public int getNumberOfPlayers() {
		synchronized (game) {
			return game.getPlayers().size();
		}
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
		return new MultiPlayerGame(game.newGame());
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

	public boolean isPlayingFromThisSeat(IPlayer player) {
		boolean playingFromSeat = false;

		Player innerPlayer = getPlayer(player);

		if (innerPlayer != null) {
			if (innerPlayer.getSession() == Session.get()) {
				playingFromSeat = true;
			}
		}

		return playingFromSeat;
	}

	public GameStatus getGameStatus() {
		if (currentRound == null || getPlayers().isEmpty()) {
			return GameStatus.SETTING_UP;
		} else if (isComplete()) {
			return GameStatus.COMPLETE;
		} else {
			return GameStatus.STARTED;
		}
	}

	private Player getPlayer(IPlayer player) {
		Player innerPlayer = null;

		synchronized (game) {
			for (IPlayer p : getPlayers()) {
				Player localPlayer = (Player) p;
				if (player == localPlayer) {
					innerPlayer = localPlayer;
					break;
				}
			}
		}

		return innerPlayer;
	}

}
