package org.apache.wicket.examples.yatzy.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.examples.yatzy.Highscore;
import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.components.menu.BookmarkableMenuItem;
import org.apache.wicket.examples.yatzy.components.menu.IMenuItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.IGame;

public class HighscorePage extends BasePage {

	private static class GameHighscoresModel extends LoadableDetachableModel {
		private static final long serialVersionUID = 1L;

		private final IModel highscoreModel;
		private final Class<? extends IGame> gameType;

		public GameHighscoresModel(IModel highscoreModel, Class<? extends IGame> gameType) {
			this.highscoreModel = highscoreModel;
			this.gameType = gameType;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object load() {
			List<Highscore> gameHighscores = new ArrayList<Highscore>();

			List<Highscore> highscores = (List<Highscore>) highscoreModel.getObject();

			for (Highscore highscore : highscores) {
				if (gameHighscores.size() < 10) {
					if (gameType.equals(highscore.getGameType())) {
						gameHighscores.add(highscore);
					}
				}
			}

			return gameHighscores;
		}
	}

	private static class GameTypesModel extends LoadableDetachableModel {
		private static final long serialVersionUID = 1L;

		private final IModel highscoreModel;

		public GameTypesModel(IModel highscoreModel) {
			this.highscoreModel = highscoreModel;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object load() {
			Set<Class<? extends IGame>> gameTypes = new HashSet<Class<? extends IGame>>();

			List<Highscore> highscores = (List<Highscore>) highscoreModel.getObject();
			for (Highscore highscore : highscores) {
				gameTypes.add(highscore.getGameType());
			}

			return new ArrayList<Class<? extends IGame>>(gameTypes);
		}
	}

	public HighscorePage() {
		final IModel highscoreModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return YatzyApplication.get().getHighscores();
			}
		};

		GameTypesModel gameTypesModel = new GameTypesModel(highscoreModel);
		add(new ListView("gameTypes", gameTypesModel) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(ListItem item) {
				Class<? extends IGame> gameType = (Class<? extends IGame>) item.getModelObject();

				IModel gameTypeModel = new StringResourceModel("game." + gameType.getSimpleName(), this, null);
				Label gameTypeLabel = new Label("gameType", gameTypeModel);
				item.add(gameTypeLabel);

				GameHighscoresModel gameHighscoresModel = new GameHighscoresModel(highscoreModel, gameType);

				item.add(new ListView("highscores", gameHighscoresModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem item) {
						item.add(new Label("rank", new AbstractReadOnlyModel() {
							private static final long serialVersionUID = 1L;

							@Override
							public Object getObject() {
								return item.getIndex() + 1;
							}
						}));
						item.add(new Label("score", new PropertyModel(item.getModelObject(), "score")));
						item.add(new Label("name", new PropertyModel(item.getModelObject(), "name")));
					}
				});

			}
		});
	}

	@Override
	protected IModel getPageTitleModel() {
		return new StringResourceModel("highscore", this, null);
	}

	@Override
	protected void addMenuItems(List<IMenuItem> menuItems) {
		menuItems.add(new BookmarkableMenuItem(new StringResourceModel("newGame", this, null), NewGamePage.class));
	}

}
