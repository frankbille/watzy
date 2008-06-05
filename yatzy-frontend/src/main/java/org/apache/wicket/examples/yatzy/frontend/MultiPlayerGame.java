package org.apache.wicket.examples.yatzy.frontend;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

	private static class Seat implements Serializable {
		private static final long serialVersionUID = 1L;

		private final Session session;
		private final List<IPlayer> players = new CopyOnWriteArrayList<IPlayer>();
		private boolean ready = false;

		public Seat() {
			session = Session.get();
		}

		public boolean isReady() {
			return ready;
		}

		public void ready() {
			ready = true;
		}

		public Session getSession() {
			return session;
		}

		public List<IPlayer> getPlayers() {
			return players;
		}

		public void addPlayer(IPlayer player) {
			players.add(player);
		}

		public void removePlayer(IPlayer player) {
			players.remove(player);
		}
	}

	private final IGame game;

	private MultiPlayerRound currentRound;

	private final List<Seat> seats = new CopyOnWriteArrayList<Seat>();

	public MultiPlayerGame(IGame game) {
		this.game = game;
	}

	public IGame getInnerGame() {
		return game;
	}

	public void addPlayer(IPlayer player) {
		synchronized (game) {
			game.addPlayer(player);
			getSeat().addPlayer(player);
		}
	}

	public void removePlayer(IPlayer player) {
		synchronized (game) {
			game.removePlayer(player);
			getSeat().removePlayer(player);
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

	public boolean isSeatReady() {
		return getSeat().isReady();
	}

	public void seatReady() {
		getSeat().ready();
	}

	public boolean isAllSeatsReady() {
		boolean ready = true;

		for (Seat seat : seats) {
			if (seat.isReady() == false) {
				ready = false;
				break;
			}
		}

		return ready;
	}

	public boolean isPlayingFromThisSeat(IPlayer player) {
		boolean playingFromSeat = false;

		Seat seat = getSeat();
		for (IPlayer p : seat.getPlayers()) {
			if (p == player) {
				playingFromSeat = true;
				break;
			}
		}

		return playingFromSeat;
	}

	public GameStatus getGameStatus() {
		if (isComplete() && getPlayers().isEmpty() == false) {
			return GameStatus.COMPLETE;
		} else if (currentRound == null) {
			return GameStatus.SETTING_UP;
		} else {
			return GameStatus.STARTED;
		}
	}

	public boolean isPlaying() {
		boolean playing = false;

		if (currentRound != null) {
			MultiPlayerTurn currentTurn = currentRound.getCurrentTurn();
			if (currentTurn != null) {
				IPlayer player = currentTurn.getPlayer();
				playing = isPlayingFromThisSeat(player);
			}
		}

		return playing;
	}

	private Seat getSeat() {
		Seat foundSeat = null;

		for (Seat seat : seats) {
			if (seat.getSession() == Session.get()) {
				foundSeat = seat;
				break;
			}
		}

		if (foundSeat == null) {
			foundSeat = new Seat();
			seats.add(foundSeat);
		}

		return foundSeat;
	}

}
