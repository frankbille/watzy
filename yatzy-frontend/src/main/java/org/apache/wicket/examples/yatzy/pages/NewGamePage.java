package org.apache.wicket.examples.yatzy.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
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

public class NewGamePage extends BasePage {
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

		Form playersForm = new Form("playersForm") {
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

		final ListView playerList = new ListView("players", players) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				item.add(new Label("playerLabel", new StringResourceModel("playerLabel", NewGamePage.this, null,
						new Object[] { item.getIndex() + 1 })));
				item.add(new TextField("name", new PropertyModel(item.getModel(), "name")));
			}
		};
		playerList.setReuseItems(true);
		playersForm.add(playerList);

		playersForm.add(new IFormValidator() {
			private static final long serialVersionUID = 1L;

			public FormComponent[] getDependentFormComponents() {
				final List<TextField> playerFields = new ArrayList<TextField>();

				playerList.visitChildren(TextField.class, new IVisitor() {
					public Object component(Component component) {
						playerFields.add((TextField) component);
						return CONTINUE_TRAVERSAL;
					}
				});

				return playerFields.toArray(new TextField[playerFields.size()]);
			}

			public void validate(Form form) {
				boolean hasPlayer = false;

				for (FormComponent formComponent : getDependentFormComponents()) {
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

		IChoiceRenderer cr = new ChoiceRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Object object) {
				IModel displayModel = new StringResourceModel("game.${class.simpleName}", NewGamePage.this, new Model(
						(Serializable) object));
				return displayModel.getObject();
			}
		};
		RadioChoice game = new RadioChoice("game", new PropertyModel(this, "game"), games, cr);
		game.setRequired(true);
		playersForm.add(game);

		playersForm.add(new Button("startGame", new StringResourceModel("startGame", this, null)));
	}

	public void setGame(IGame game) {
		this.game = game;
	}

	public IGame getGame() {
		return game;
	}

	@Override
	protected IModel getPageTitleModel() {
		return new StringResourceModel("newGame", this, null);
	}

}
