package org.apache.wicket.examples.yatzy.frontend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;

@Entity
public class Highscore implements Comparable<Highscore>, Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private IGame game;
	private IPlayer player;
	private int score;
	private Date timestamp;
	
	/**
	 * Constructor to satisfy hibernate
	 */
	protected Highscore() {
	}

	public Highscore(IGame game, IPlayer player, int score) {
		this.game = game;
		this.player = player;
		this.score = score;
		timestamp = new Date();
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
	public Class<? extends IGame> getGameType() {
		return game.getClass();
	}

	@Basic
	@Column(length=1024*1024*1024)
	public IGame getGame() {
		return game;
	}
	
	protected void setGame(IGame game) {
		this.game = game;
	}

	@Basic
	@Column(length=1024*1024)
	public IPlayer getPlayer() {
		return player;
	}
	
	protected void setPlayer(IPlayer player) {
		this.player = player;
	}

	@Transient
	public String getName() {
		return player.getName();
	}

	public int getScore() {
		return score;
	}
	
	protected void setScore(int score) {
		this.score = score;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	protected void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int compareTo(Highscore o) {
		int compare = 0;

		if (o != null) {
			compare = getGameType().getSimpleName().compareTo(o.getGameType().getSimpleName());

			if (compare == 0) {
				compare = -1 * new Integer(score).compareTo(o.getScore());

				if (compare == 0) {
					compare = timestamp.compareTo(o.getTimestamp());
				}
			}
		} else {
			compare = -1;
		}

		return compare;
	}
}