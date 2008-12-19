package org.apache.wicket.examples.yatzy.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.CompoundAjaxTimerBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.StateBasedSelfUpdatingListener;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior.ButtonColor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.MaxiGame;
import org.examples.yatzy.StandardGame;
import org.examples.yatzy.ai.MaxiYatzyAIPlayer;

public class NewGamePage extends BasePage<Void> {

	protected IGame game;

	public NewGamePage() {
		CompoundAjaxTimerBehavior timerBehavior = new CompoundAjaxTimerBehavior(Duration.ONE_SECOND);
		add(timerBehavior);

		createNewGameForm();

		add(new Link<MultiPlayerGame>("testAiGame") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				IGame game = new MaxiGame();
				for (int i = 1; i <= 10; i++) {
					MaxiYatzyAIPlayer player = new MaxiYatzyAIPlayer();
					player.setName("P" + i);
					game.addPlayer(player);
				}
				getRequestCycle().setResponsePage(new GamePage(game));
			}
		});

		createGameList(timerBehavior);
	}

	private void createGameList(CompoundAjaxTimerBehavior timerBehavior) {
		IModel<List<MultiPlayerGame>> gamesModel = new LoadableDetachableModel<List<MultiPlayerGame>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<MultiPlayerGame> load() {
				return YatzyApplication.get().getAvailableGames();
			}
		};

		WebMarkupContainer<List<MultiPlayerGame>> gameList = new WebMarkupContainer<List<MultiPlayerGame>>("gameList",
				gamesModel);
		timerBehavior.addListener(new StateBasedSelfUpdatingListener<WebMarkupContainer<List<MultiPlayerGame>>>(
				gameList));
		add(gameList);

		gameList.add(new ListView<MultiPlayerGame>("games", gamesModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<MultiPlayerGame> item) {
				item.add(new Label<Integer>("number", new Model<Integer>(item.getIndex() + 1)));

				item.add(new Label<String>("game", new StringResourceModel("game.${innerGame.class.simpleName}", this,
						item.getModel())));

				IModel<String> playersModel = new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						StringBuilder b = new StringBuilder();

						List<IPlayer> players = item.getModelObject().getPlayers();
						for (IPlayer player : players) {
							if (b.length() > 0) {
								b.append(", ");
							}

							if (Strings.isEmpty(player.getName())) {
								b.append("-");
							} else {
								b.append(player.getName());
							}
						}

						return b.toString();
					}
				};
				item.add(new Label<String>("players", playersModel));

				item.add(new Link<MultiPlayerGame>("join", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new GamePage(getModelObject()));
					}
				});
			}
		});
	}

	private void createNewGameForm() {
		Form<Void> newGameForm = new Form<Void>("newGame");
		add(newGameForm);

		List<IGame> games = new ArrayList<IGame>();
		games.add(new StandardGame());
		games.add(new MaxiGame());

		IChoiceRenderer<IGame> cr = new ChoiceRenderer<IGame>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(IGame object) {
				IModel<String> displayModel = new StringResourceModel("game.${class.simpleName}", NewGamePage.this,
						new Model<IGame>(object));
				return displayModel.getObject();
			}
		};
		RadioChoice<IGame> game = new RadioChoice<IGame>("game", new PropertyModel<IGame>(this, "game"), games, cr);
		game.setRequired(true);
		newGameForm.add(game);

		AjaxSubmitLink<Void> createGame = new AjaxSubmitLink<Void>("createGame") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				getRequestCycle().setResponsePage(new GamePage(NewGamePage.this.game));
			}
		};
		createGame.add(new JQueryButtonBehavior(ButtonColor.GREEN));
		newGameForm.add(createGame);
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("title", this, null);
	}

}
