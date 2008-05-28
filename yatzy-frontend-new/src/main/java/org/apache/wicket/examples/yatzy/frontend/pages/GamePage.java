package org.apache.wicket.examples.yatzy.frontend.pages;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.apache.wicket.examples.yatzy.frontend.panels.EnterNamePanel;
import org.apache.wicket.examples.yatzy.frontend.panels.GameAdminPanel;
import org.apache.wicket.examples.yatzy.frontend.panels.GameResultPanel;
import org.apache.wicket.examples.yatzy.frontend.panels.ScoreCardPanel;
import org.apache.wicket.examples.yatzy.frontend.panels.TurnPanel;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.ITurnScore;

public final class GamePage extends BasePage<MultiPlayerGame> {
	private static final String CONTENT = "content";

	protected IPlayer modifyingPlayer;
	private ScoreCardPanel scoreCardPanel;
	private WebMarkupContainer<MultiPlayerGame> mainActionPanel;

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

		mainActionPanel = new WebMarkupContainer<MultiPlayerGame>("mainActionPanel", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();

				Component<?> content = null;

				if (modifyingPlayer != null) {
					content = createEnterNamePanel(CONTENT);
				} else if (getModelObject().getGameStatus() == GameStatus.SETTING_UP) {
					content = new GameAdminPanel(CONTENT, new Model<IGame>(getModelObject())) {
						private static final long serialVersionUID = 1L;

						@Override
						protected void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player) {
							modifyingPlayer = player;
							replaceMainActionPanel(target);
						}

						@Override
						protected void onGameStarted(AjaxRequestTarget target) {
							replaceMainActionPanel(target);
							target.addComponent(scoreCardPanel);
						}
					};
				} else if (getModelObject().getGameStatus() == GameStatus.STARTED) {
					IModel<ITurn> turnModel = new PropertyModel<ITurn>(getModel(),
							"currentRound.currentTurn");
					content = new TurnPanel(CONTENT, turnModel);
				} else if (getModelObject().getGameStatus() == GameStatus.COMPLETE) {
					content = new GameResultPanel(CONTENT, new Model<IGame>(getModelObject()));
				} else {
					content = new WebComponent<Void>(CONTENT);
				}

				content.setOutputMarkupId(true);

				replace(content);
			}
		};
		mainActionPanel.setOutputMarkupId(true);
		mainActionPanel.add(new WebComponent<Void>(CONTENT));
		add(mainActionPanel);

		IModel<ITurn> turnModel = new PropertyModel<ITurn>(getModel(), "currentRound.currentTurn");
		IModel<IScoreCard> scoreCardModel = new PropertyModel<IScoreCard>(getModel(), "scoreCard");
		scoreCardPanel = new ScoreCardPanel("scoreCardPanel", turnModel, scoreCardModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void combinationSelected(AjaxRequestTarget target,
					IModel<ITurnScore> scoreModel) {
				MultiPlayerGame game = GamePage.this.getModelObject();
				IRound currentRound = game.getCurrentRound();
				if (currentRound.hasMoreTurns() == false) {
					currentRound = game.newRound();
				}
				currentRound.nextTurn();

				replaceMainActionPanel(target);
			}
		};
		scoreCardPanel.setOutputMarkupId(true);
		add(scoreCardPanel);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		modifyingPlayer = null;

		MultiPlayerGame game = getModelObject();
		List<IPlayer> players = game.getPlayers();
		for (IPlayer player : players) {
			if (game.isPlayingFromThisSeat(player)) {
				if (Strings.isEmpty(player.getName())) {
					modifyingPlayer = player;
					break;
				}
			}
		}
	}

	private void replaceMainActionPanel(AjaxRequestTarget target) {
		target.addComponent(mainActionPanel);
	}

	private EnterNamePanel createEnterNamePanel(String wicketId) {
		return new EnterNamePanel(wicketId, new PropertyModel<IPlayer>(this, "modifyingPlayer")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNameChanged(AjaxRequestTarget target, IPlayer player) {
				modifyingPlayer = null;
				replaceMainActionPanel(target);
				target.addComponent(scoreCardPanel);
			}
		};
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("game.${innerGame.class.simpleName}", this,
				new PropertyModel<MultiPlayerGame>(this, "modelObject"));
	}

}
