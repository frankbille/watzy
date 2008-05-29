package org.apache.wicket.examples.yatzy.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.CompoundAjaxTimerBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.ITimerListener;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.StateBasedSelfUpdatingListener;
import org.apache.wicket.examples.yatzy.frontend.components.menu.BookmarkableMenuItem;
import org.apache.wicket.examples.yatzy.frontend.components.menu.IMenuItem;
import org.apache.wicket.examples.yatzy.frontend.panels.MainActionPanel;
import org.apache.wicket.examples.yatzy.frontend.panels.ScoreCardPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
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

public final class GamePage extends BasePage<MultiPlayerGame> {
	private ScoreCardPanel scoreCardPanel;
	private MainActionPanel mainActionPanel;

	public GamePage() {
		throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
	}

	public GamePage(IGame game) {
		// Create model
		if (game instanceof MultiPlayerGame) {
			MultiPlayerGame multiPlayerGame = (MultiPlayerGame) game;
			setModel(new Model<MultiPlayerGame>(multiPlayerGame));
		} else {
			setModel(new Model<MultiPlayerGame>(new MultiPlayerGame(game)));
		}

		// Create the compound ajax timer behavior, to update all the UI,
		// simulating a push behavior.
		CompoundAjaxTimerBehavior timerBehavior = new CompoundAjaxTimerBehavior(Duration.ONE_SECOND);
		add(timerBehavior);

		// Create the main action panel
		mainActionPanel = new MainActionPanel("mainActionPanel", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onGameStarted(AjaxRequestTarget target) {
				target.addComponent(scoreCardPanel);
			}

			@Override
			protected void onNameChanged(AjaxRequestTarget target, IPlayer player) {
				target.addComponent(scoreCardPanel);
			}

			@Override
			protected void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player) {
				target.addComponent(scoreCardPanel);
			}
		};
		timerBehavior.addListener(new StateBasedSelfUpdatingListener<MainActionPanel>(
				mainActionPanel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object getStateObject(MainActionPanel component) {
				return component.getStateObject();
			}
		});
		add(mainActionPanel);

		IModel<ITurn> turnModel = new PropertyModel<ITurn>(getModel(), "currentRound.currentTurn");
		IModel<IScoreCard> scoreCardModel = new PropertyModel<IScoreCard>(getModel(), "scoreCard");
		scoreCardPanel = new ScoreCardPanel("scoreCardPanel", turnModel, scoreCardModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void combinationSelected(AjaxRequestTarget target,
					IModel<ITurnScore> scoreModel) {
				MultiPlayerGame game = GamePage.this.getModelObject();
				if (game.isComplete()) {
					// Register high scores
					IScoreCard scoreCard = game.getScoreCard();
					List<IPlayer> players = scoreCard.getPlayers();
					for (IPlayer player : players) {
						int score = scoreCard.getScore(player);
						YatzyApplication.get().registerHighscore(game.getInnerGame().getClass(),
								player.getName(), score);
					}
				} else {
					IRound currentRound = game.getCurrentRound();
					if (currentRound.hasMoreTurns() == false) {
						currentRound = game.newRound();
					}
					currentRound.nextTurn();
				}

				replaceMainActionPanel(target);
			}

			@Override
			protected boolean combinationSelectable(IModel<ITurnScore> scoreModel) {
				return GamePage.this.getModelObject().isPlaying();
			}
		};
		scoreCardPanel.setOutputMarkupId(true);
		ITimerListener listener = new StateBasedSelfUpdatingListener<ScoreCardPanel>(scoreCardPanel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object getStateObject(ScoreCardPanel component) {
				List<Object> objects = new ArrayList<Object>();
				objects.add(component.getModelObject());
				ITurn turn = component.getTurnModel().getObject();
				if (turn != null) {
					objects.add(turn.getPlayer());
				}
				return objects;
			}
		};
		timerBehavior.addListener(listener);
		add(scoreCardPanel);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("game.${innerGame.class.simpleName}", this,
				new PropertyModel<MultiPlayerGame>(this, "modelObject"));
	}

	@Override
	protected void addMenuItems(List<IMenuItem> menuItems) {
		final IBehavior endGameConfirm = new AttributeModifier("onclick", true,
				new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						IModel<String> confirmText = new StringResourceModel(
								"confirmQuitExistingGame", GamePage.this, null);

						return "return confirm('" + confirmText.getObject() + "');";
					}
				});

		menuItems.add(new BookmarkableMenuItem(new StringResourceModel("newGame", this, null),
				NewGamePage.class) {
			private static final long serialVersionUID = 1L;

			@Override
			public MarkupContainer<?> createLink(String wicketId) {
				MarkupContainer<?> link = super.createLink(wicketId);
				link.add(endGameConfirm);
				return link;
			}
		});
	}

	private void replaceMainActionPanel(AjaxRequestTarget target) {
		target.addComponent(mainActionPanel);
	}

}
