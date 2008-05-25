package org.apache.wicket.examples.yatzy.pages.multi;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.examples.yatzy.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.MultiPlayerTurn;
import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.behaviours.AjaxSelfUpdatingTimerBehavior;
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
import org.examples.yatzy.ITurn;
import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.ITurnScore;

public class MultiPlayerGamePage extends AuthenticatedBasePage<MultiPlayerGame> {
	private static final long serialVersionUID = 1L;

	public MultiPlayerGamePage() {
		throw new RestartResponseException(getApplication().getHomePage());
	}

	public MultiPlayerGamePage(IGame game) {
		if (game instanceof MultiPlayerGame == false) {
			throw new RestartResponseException(MultiPlayerGameSetupPage.class);
		}

		setModel(new Model<MultiPlayerGame>((MultiPlayerGame) game));

		final PropertyModel<ITurn> turnModel = new PropertyModel<ITurn>(getModel(), "currentRound.currentTurn");

		final WebMarkupContainer<MultiPlayerGame> turnPanelWrapper = new WebMarkupContainer<MultiPlayerGame>(
				"turnPanelWrapper", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();

				if (getModelObject().isComplete()) {
					// Show the result
					GameResultPanel gameResultPanel = new GameResultPanel("turnPanel", new Model<IGame>(
							getModelObject()));
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

				if (getModelObject().isReady() == false || getModelObject().isComplete()) {
					stateObject = getModelObject().getInnerGame();
				} else if (turn != null) {
					stateObject = turn.getInnerTurn();
				}

				return stateObject;
			}

			@Override
			public boolean isEnabled(Component<?> component) {
				boolean behaviourEnabled = selfUpdateEnabled();
				return behaviourEnabled;
			}
		});
		add(turnPanelWrapper);

		final TurnPanel turnPanel = new TurnPanel("turnPanel", turnModel);
		turnPanel.setOutputMarkupId(true);
		turnPanelWrapper.add(turnPanel);

		ScoreCardPanel scoreCardPanel = new ScoreCardPanel("scoreCard", turnModel, new PropertyModel<IScoreCard>(
				getModel(), "scoreCard")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void combinationSelected(AjaxRequestTarget target, IModel<ITurnScore> scoreModel) {
				MultiPlayerGame multiPlayerGame = MultiPlayerGamePage.this.getModelObject();
				if (multiPlayerGame.isComplete()) {
					// Register the highscores
					List<IPlayer> players = multiPlayerGame.getPlayers();
					for (IPlayer player : players) {
						int score = multiPlayerGame.getScoreCard().getScore(player);
						YatzyApplication.get().registerHighscore(multiPlayerGame.getInnerGame().getClass(),
								player.getName(), score);
					}

					if (target != null) {
						target.addComponent(turnPanelWrapper);

						target.addComponent(this);
					}
				} else {
					if (multiPlayerGame.getCurrentRound().hasMoreTurns() == false) {
						newRound();
					}

					multiPlayerGame.getCurrentRound().nextTurn();

					if (target != null) {
						target.addComponent(turnPanelWrapper);
					}
				}
			}

			@Override
			protected boolean combinationSelectable(IModel<ITurnScore> scoreModel) {
				return MultiPlayerGamePage.this.getModelObject().isPlaying();
			}
		};
		scoreCardPanel.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component<?> component) {
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
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("game.${innerGame.class.simpleName}", this, new PropertyModel<MultiPlayerGame>(
				this, "game"));
	}

	private IRound newRound() {
		return getModelObject().newRound();
	}

	private boolean selfUpdateEnabled() {
		boolean enabled = false;

		if (getModelObject().isComplete() == false) {
			if (getModelObject().isReady() == false) {
				enabled = true;
			} else if (getModelObject().isPlaying() == false) {
				enabled = true;
			}
		}
		return enabled;
	}

}
