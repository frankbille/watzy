package org.apache.wicket.examples.yatzy.pages.multi;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.examples.yatzy.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.MultiPlayerTurn;
import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.YatzySession;
import org.apache.wicket.examples.yatzy.behaviours.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.examples.yatzy.components.menu.AbstractSimpleLabelMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.BookmarkableMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.IMenuItem;
import org.apache.wicket.examples.yatzy.pages.AuthenticatedBasePage;
import org.apache.wicket.examples.yatzy.pages.NewGamePage;
import org.apache.wicket.examples.yatzy.panels.GameResultPanel;
import org.apache.wicket.examples.yatzy.panels.ScoreCardPanel;
import org.apache.wicket.examples.yatzy.panels.TurnPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
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

		PropertyModel turnModel = new PropertyModel(getModel(), "currentRound.currentTurn");

		final TurnPanel turnPanel = new TurnPanel("turnPanel", turnModel);
		turnPanel.setOutputMarkupId(true);
		turnPanel.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object getStateObject() {
				MultiPlayerTurn turn = (MultiPlayerTurn) getComponent().getModelObject();
				return turn != null ? turn.getInnerTurn() : null;
			}

			@Override
			public boolean isEnabled(Component component) {
				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();
				return multiPlayerGame.isPlaying() == false;
			}
		});
		add(turnPanel);

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
						YatzyApplication.get().registerHighscore(game.getClass(), player.getName(), score);
					}

					// Show the result
					GameResultPanel gameResultPanel = new GameResultPanel("turnPanel", MultiPlayerGamePage.this
							.getModel());
					gameResultPanel.setOutputMarkupId(true);
					turnPanel.replaceWith(gameResultPanel);

					if (target != null) {
						target.addComponent(gameResultPanel);

						target.addComponent(this);
					}
				} else {
					if (game.getCurrentRound().hasMoreTurns() == false) {
						newRound();
					}

					game.getCurrentRound().nextTurn();

					if (target != null) {
						target.addComponent(turnPanel);
					}
				}
			}

			@Override
			protected boolean combinationSelectable(IModel scoreModel) {
				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();
				return multiPlayerGame.getCurrentRound().getCurrentTurn().getPlayer() == YatzySession.get().getPlayer();
			}
		};
		scoreCardPanel.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				MultiPlayerGame multiPlayerGame = (MultiPlayerGame) MultiPlayerGamePage.this.getModelObject();
				return multiPlayerGame.isPlaying() == false;
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

	@Override
	protected void addMenuItems(List<IMenuItem> menuItems) {
		final IBehavior endGameConfirm = new AttributeModifier("onclick", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				IModel confirmText = new StringResourceModel("confirmQuitExistingGame", MultiPlayerGamePage.this, null);

				return "return confirm('" + confirmText.getObject() + "');";
			}
		});

		menuItems.add(new BookmarkableMenuItem(new StringResourceModel("newGame", this, null), NewGamePage.class) {
			private static final long serialVersionUID = 1L;

			@Override
			public MarkupContainer createLink(String wicketId) {
				MarkupContainer link = super.createLink(wicketId);
				link.add(endGameConfirm);
				return link;
			}
		});
		menuItems.add(new AbstractSimpleLabelMenuItem(new StringResourceModel("restartGame", this, null)) {
			private static final long serialVersionUID = 1L;

			public MarkupContainer createLink(String wicketId) {
				Link link = new Link(wicketId) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						IGame game = (IGame) MultiPlayerGamePage.this.getModelObject();

						getRequestCycle().setResponsePage(new MultiPlayerGamePage(game.newGame()));
					}
				};
				link.add(endGameConfirm);
				return link;
			}
		});
	}

}
