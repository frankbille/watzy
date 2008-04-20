package org.apache.wicket.examples.yatzy.pages.multi;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.examples.yatzy.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.MultiPlayerTurn;
import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.MultiPlayerGame.Player;
import org.apache.wicket.examples.yatzy.behaviours.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.examples.yatzy.behaviours.HeartBeatBehavior;
import org.apache.wicket.examples.yatzy.pages.AuthenticatedBasePage;
import org.apache.wicket.examples.yatzy.panels.GameResultPanel;
import org.apache.wicket.examples.yatzy.panels.ScoreCardPanel;
import org.apache.wicket.examples.yatzy.panels.TurnPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.time.Duration;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;

public class MultiPlayerGamePage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public MultiPlayerGamePage() {
		throw new RestartResponseException(getApplication().getHomePage());
	}

	public MultiPlayerGamePage(IGame game) {
		super(new Model(game));

		if (game instanceof MultiPlayerGame == false) {
			throw new RestartResponseException(MultiPlayerGameSetupPage.class);
		}

		MultiPlayerGame multiPlayerGame = (MultiPlayerGame) game;

		Player me = multiPlayerGame.getMe();
		add(new HeartBeatBehavior(Duration.ONE_SECOND, multiPlayerGame.getHeartBeatForPlayer(me)));

		final PropertyModel turnModel = new PropertyModel(getModel(), "currentRound.currentTurn");

		final WebMarkupContainer turnPanelWrapper = new WebMarkupContainer("turnPanelWrapper") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();

				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();
				if (multiPlayerGame.isComplete()) {
					// Show the result
					GameResultPanel gameResultPanel = new GameResultPanel("turnPanel", MultiPlayerGamePage.this
							.getModel());
					gameResultPanel.setOutputMarkupId(true);
					replace(gameResultPanel);
				}
			}
		};
		turnPanelWrapper.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object getStateObject() {
				Object stateObject = null;

				MultiPlayerTurn turn = (MultiPlayerTurn) turnModel.getObject();
				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();

				if (multiPlayerGame.isReady() == false || multiPlayerGame.isComplete()) {
					stateObject = multiPlayerGame.getInnerGame();
				} else if (turn != null) {
					stateObject = turn.getInnerTurn();
				}

				return stateObject;
			}

			@Override
			public boolean isEnabled(Component component) {
				boolean behaviourEnabled = selfUpdateEnabled();
				return behaviourEnabled;
			}
		});
		add(turnPanelWrapper);

		final TurnPanel turnPanel = new TurnPanel("turnPanel", turnModel);
		turnPanel.setOutputMarkupId(true);
		turnPanelWrapper.add(turnPanel);

		ScoreCardPanel scoreCardPanel = new ScoreCardPanel("scoreCard", turnModel, new PropertyModel(getModel(),
				"scoreCard")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void combinationSelected(AjaxRequestTarget target, IModel scoreModel) {
				MultiPlayerGame game = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();

				if (game.isComplete()) {
					// Register the highscores
					List<IPlayer> players = game.getPlayers();
					for (IPlayer player : players) {
						int score = game.getScoreCard().getScore(player);
						YatzyApplication.get().registerHighscore(game.getInnerGame().getClass(), player.getName(),
								score);
					}

					if (target != null) {
						target.addComponent(turnPanelWrapper);

						target.addComponent(this);
					}
				} else {
					if (game.getCurrentRound().hasMoreTurns() == false) {
						newRound();
					}

					game.getCurrentRound().nextTurn();

					if (target != null) {
						target.addComponent(turnPanelWrapper);
					}
				}
			}

			@Override
			protected boolean combinationSelectable(IModel scoreModel) {
				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();
				return multiPlayerGame.isPlaying();
			}
		};
		scoreCardPanel.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				return selfUpdateEnabled();
			}
		});
		add(scoreCardPanel);

		IRound round = newRound();

		if (round.hasMoreTurns()) {
			round.nextTurn();
		}
	}

	@Override
	protected IModel getPageTitleModel() {
		return new StringResourceModel("game.${innerGame.class.simpleName}", this, getModel());
	}

	private IRound newRound() {
		IGame game = (IGame) getModelObject();
		return game.newRound();
	}

	private boolean selfUpdateEnabled() {
		boolean enabled = false;

		MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();

		if (multiPlayerGame.isComplete() == false) {
			if (multiPlayerGame.isReady() == false) {
				enabled = true;
			} else if (multiPlayerGame.isPlaying() == false) {
				enabled = true;
			}
		}
		return enabled;
	}

}
