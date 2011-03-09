package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame.GameStatus;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;

public abstract class MainActionPanel extends GenericPanel<MultiPlayerGame> {
	private static final long serialVersionUID = 1L;

	private static final String CONTENT = "content";

	public static enum Action {
		ENTER_NAME, ADMINISTRATION, PLAYING, COMPLETE
	}

	protected IPlayer modifyingPlayer;

	public MainActionPanel(String id, IModel<MultiPlayerGame> model) {
		super(id, model);

		setOutputMarkupId(true);

		add(new WebComponent(CONTENT));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Component content = null;

		Action action = getCurrentAction();

		if (action == Action.ENTER_NAME) {
			content = createEnterNamePanel(CONTENT);
		} else if (action == Action.ADMINISTRATION) {
			content = new GameAdminPanel(CONTENT, getModel()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player) {
					modifyingPlayer = player;
					replaceMainActionPanel(target);
					MainActionPanel.this.onNewPlayerAdded(target, player);
				}

				@Override
				protected void onGameStarted(AjaxRequestTarget target) {
					replaceMainActionPanel(target);
					MainActionPanel.this.onGameStarted(target);
				}
			};
		} else if (action == Action.PLAYING) {
			IModel<ITurn> turnModel = new PropertyModel<ITurn>(getModel(),
					"currentRound.currentTurn");
			content = new TurnPanel(CONTENT, turnModel);
		} else if (action == Action.COMPLETE) {
			content = new GameResultPanel(CONTENT, new Model<IGame>(getModelObject()));
		} else {
			content = new WebComponent(CONTENT);
		}

		content.setOutputMarkupId(true);

		replace(content);
	}

	public Object getStateObject() {
		Action action = getCurrentAction();

		Object stateObject = null;

		if (getModelObject().isPlaying()) {
			stateObject = null;
		} else if (action == Action.PLAYING) {
			stateObject = get(CONTENT).getDefaultModelObject();
		} else {
			stateObject = action;
		}

		return stateObject;
	}

	protected abstract void onNameChanged(AjaxRequestTarget target, IPlayer player);

	protected abstract void onGameStarted(AjaxRequestTarget target);

	protected abstract void onNewPlayerAdded(AjaxRequestTarget target, IPlayer player);

	private void replaceMainActionPanel(AjaxRequestTarget target) {
		target.addComponent(this);
	}

	private Action getCurrentAction() {
		Action action = null;

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

		if (modifyingPlayer != null) {
			action = Action.ENTER_NAME;
		} else if (getModelObject().getGameStatus() == GameStatus.SETTING_UP) {
			action = Action.ADMINISTRATION;
		} else if (getModelObject().getGameStatus() == GameStatus.STARTED) {
			action = Action.PLAYING;
		} else if (getModelObject().getGameStatus() == GameStatus.COMPLETE) {
			action = Action.COMPLETE;
		}

		return action;
	}

	private EnterNamePanel createEnterNamePanel(String wicketId) {
		return new EnterNamePanel(wicketId, new PropertyModel<IPlayer>(this, "modifyingPlayer")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNameChanged(AjaxRequestTarget target, IPlayer player) {
				modifyingPlayer = null;
				replaceMainActionPanel(target);
				MainActionPanel.this.onNameChanged(target, player);
			}
		};
	}

}
