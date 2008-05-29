package org.apache.wicket.examples.yatzy.frontend.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior.ButtonColor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;

public abstract class GameAdminPanel extends Panel<MultiPlayerGame> {
	private static final long serialVersionUID = 1L;

	public GameAdminPanel(String id, IModel<MultiPlayerGame> model) {
		super(id, model);

		AjaxLink<MultiPlayerGame> addNewPlayerLink = new AjaxLink<MultiPlayerGame>("addNewPlayer",
				model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IPlayer player = new AdhocPlayer();
				getModelObject().addPlayer(player);

				onNewPlayerAdded(target, player);
			}
		};
		addNewPlayerLink.add(new JQueryButtonBehavior());
		add(addNewPlayerLink);

		AjaxLink<MultiPlayerGame> startGame = new AjaxLink<MultiPlayerGame>("startGame", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getModelObject().seatReady();

				if (getModelObject().isAllSeatsReady()) {
					IRound round = getModelObject().newRound();
					round.nextTurn();
				}

				onGameStarted(target);
			}

			@Override
			public boolean isVisible() {
				return getModelObject().getPlayers().isEmpty() == false
						&& getModelObject().isSeatReady() == false;
			}
		};
		startGame.add(new JQueryButtonBehavior(ButtonColor.GREEN));
		add(startGame);
	}

	protected abstract void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player);

	protected abstract void onGameStarted(AjaxRequestTarget target);

}
