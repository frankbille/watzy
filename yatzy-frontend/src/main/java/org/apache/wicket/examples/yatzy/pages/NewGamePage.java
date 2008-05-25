package org.apache.wicket.examples.yatzy.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.AdhocPlayer;
import org.examples.yatzy.IGame;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.MaxiGame;
import org.examples.yatzy.StandardGame;

public class NewGamePage extends BasePage<Void> {
	private static final long serialVersionUID = 1L;

	private IGame game;

	public NewGamePage() {
		final List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(new AdhocPlayer());
		players.add(new AdhocPlayer());
		players.add(new AdhocPlayer());
		players.add(new AdhocPlayer());
		players.add(new AdhocPlayer());
		players.add(new AdhocPlayer());

		Form<Object> playersForm = new Form<Object>("playersForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				for (IPlayer player : players) {
					if (player.getName() != null) {
						game.addPlayer(player);
					}
				}

				getRequestCycle().setResponsePage(new GamePage(game));
			}
		};
		add(playersForm);

		playersForm.add(new FeedbackPanel("feedback"));

		final ListView<IPlayer> playerList = new ListView<IPlayer>("players", players) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IPlayer> item) {
				item.add(new Label<String>("playerLabel", new StringResourceModel("playerLabel", NewGamePage.this,
						null, new Object[] { item.getIndex() + 1 })));
				item.add(new TextField<String>("name", new PropertyModel<String>(item.getModel(), "name")));
			}
		};
		playerList.setReuseItems(true);
		playersForm.add(playerList);

		playersForm.add(new IFormValidator() {
			private static final long serialVersionUID = 1L;

			public FormComponent<?>[] getDependentFormComponents() {
				final List<TextField<String>> playerFields = new ArrayList<TextField<String>>();

				playerList.visitChildren(TextField.class, new IVisitor<TextField<String>>() {
					public Object component(TextField<String> component) {
						playerFields.add(component);
						return CONTINUE_TRAVERSAL;
					}
				});

				return playerFields.toArray(new TextField[playerFields.size()]);
			}

			public void validate(Form<?> form) {
				boolean hasPlayer = false;

				for (FormComponent<?> formComponent : getDependentFormComponents()) {
					if (formComponent.getConvertedInput() != null) {
						hasPlayer = true;
						break;
					}
				}

				if (hasPlayer == false) {
					form.error("You need to specify at least one player");
				}
			}

		});

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
		playersForm.add(game);

		playersForm.add(new Button<String>("startGame", new StringResourceModel("startGame", this, null)));
	}

	public void setGame(IGame game) {
		this.game = game;
	}

	public IGame getGame() {
		return game;
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("newGame", this, null);
	}

}
