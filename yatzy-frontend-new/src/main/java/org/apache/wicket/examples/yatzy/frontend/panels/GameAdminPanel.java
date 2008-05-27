package org.apache.wicket.examples.yatzy.frontend.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior.ButtonColor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.IRound;

public abstract class GameAdminPanel extends Panel<IGame> {
	public GameAdminPanel(String id, IModel<IGame> model) {
		super(id, model);

		AjaxLink<IGame> addNewPlayerLink = new AjaxLink<IGame>("addNewPlayer", model) {
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

		AjaxLink<IGame> startGame = new AjaxLink<IGame>("startGame", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IRound round = getModelObject().newRound();
				round.nextTurn();
				onGameStarted(target);
			}

			@Override
			public boolean isVisible() {
				return getModelObject().getPlayers().isEmpty() == false;
			}
		};
		startGame.add(new JQueryButtonBehavior(ButtonColor.GREEN));
		add(startGame);
	}

	protected abstract void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player);

	protected abstract void onGameStarted(AjaxRequestTarget target);

}
