package org.apache.wicket.examples.yatzy.pages;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.examples.yatzy.domain.IGame;
import org.apache.wicket.examples.yatzy.domain.IRound;
import org.apache.wicket.examples.yatzy.domain.ITurn;
import org.apache.wicket.examples.yatzy.panels.GameResultPanel;
import org.apache.wicket.examples.yatzy.panels.ScoreCardPanel;
import org.apache.wicket.examples.yatzy.panels.TurnPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class GamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private IRound round;

	private ITurn turn;

	public GamePage() {
		throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
	}

	public GamePage(IGame game) {
		super(new Model(game));

		PropertyModel turnModel = new PropertyModel(this, "turn");

		final TurnPanel turnPanel = new TurnPanel("turnPanel", turnModel);
		turnPanel.setOutputMarkupId(true);
		add(turnPanel);

		add(new ScoreCardPanel("scoreCard", turnModel, new PropertyModel(getModel(), "scoreCard")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void combinationSelected(AjaxRequestTarget target, IModel scoreModel) {
				IGame game = (IGame) GamePage.this.getModelObject();

				if (game.isComplete()) {
					GameResultPanel gameResultPanel = new GameResultPanel("turnPanel", GamePage.this.getModel());
					gameResultPanel.setOutputMarkupId(true);
					turnPanel.replaceWith(gameResultPanel);

					if (target != null) {
						target.addComponent(gameResultPanel);
					}
				} else {
					if (round.hasMoreTurns() == false) {
						newRound();
					}

					turn = round.nextTurn();

					if (target != null) {
						target.addComponent(turnPanel);
					}
				}
			}
		});

		newRound();

		if (round.hasMoreTurns()) {
			turn = round.nextTurn();
		}
	}

	@Override
	protected IModel getPageTitleModel() {
		return new StringResourceModel("game.${class.simpleName}", this, getModel());
	}

	private void newRound() {
		IGame game = (IGame) getModelObject();
		round = game.newRound();
	}

	public IRound getRound() {
		return round;
	}

	public ITurn getTurn() {
		return turn;
	}

}
