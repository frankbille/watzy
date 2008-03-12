package org.apache.wicket.examples.yatzy.pages;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.examples.yatzy.components.menu.AbstractSimpleLabelMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.BookmarkableMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.IMenuItem;
import org.apache.wicket.examples.yatzy.panels.GameResultPanel;
import org.apache.wicket.examples.yatzy.panels.ScoreCardPanel;
import org.apache.wicket.examples.yatzy.panels.TurnPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IRound;
import org.examples.yatzy.ITurn;

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
					round = null;
					turn = null;

					GameResultPanel gameResultPanel = new GameResultPanel("turnPanel", GamePage.this.getModel());
					gameResultPanel.setOutputMarkupId(true);
					turnPanel.replaceWith(gameResultPanel);

					if (target != null) {
						target.addComponent(gameResultPanel);

						target.addComponent(this);
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

	@Override
	protected void addMenuItems(List<IMenuItem> menuItems) {
		final IBehavior endGameConfirm = new AttributeModifier("onclick", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				IModel confirmText = new StringResourceModel("confirmQuitExistingGame", GamePage.this, null);

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
						IGame game = (IGame) GamePage.this.getModelObject();

						getRequestCycle().setResponsePage(new GamePage(game.newGame()));
					}
				};
				link.add(endGameConfirm);
				return link;
			}
		});
	}

}
