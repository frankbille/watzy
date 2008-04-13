package org.apache.wicket.examples.yatzy.pages.multi;

import org.apache.wicket.examples.yatzy.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.YatzySession;
import org.apache.wicket.examples.yatzy.pages.AuthenticatedBasePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class MultiPlayerGameSetupPage extends AuthenticatedBasePage {

	public MultiPlayerGameSetupPage() {
		final WebMarkupContainer multiPlayerListContainer = new WebMarkupContainer("multiPlayerListContainer");
		multiPlayerListContainer.setOutputMarkupId(true);
		add(multiPlayerListContainer);

		IModel multiPlayerListModel = new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				return YatzyApplication.get().getMultiPlayerGames();
			}
		};

		multiPlayerListContainer.add(new ListView("multiPlayerList", multiPlayerListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final MultiPlayerGame multiPlayerGame = (MultiPlayerGame) item.getModelObject();

				Link link = new Link("link") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						multiPlayerGame.addPlayer(YatzySession.get().getPlayer());

						getRequestCycle().setResponsePage(new MultiPlayerGamePage(multiPlayerGame));
					}
				};
				item.add(link);

				link.add(new Label("game", new StringResourceModel("game.${class.simpleName}", this, new PropertyModel(
						multiPlayerGame, "innerGame"))));

				item.add(new Label("playerCount", new PropertyModel(multiPlayerGame, "numberOfPlayers")));

				item.add(new Label("maxPlayers", new PropertyModel(multiPlayerGame, "maxPlayers")));
			}
		});
	}

	@Override
	protected IModel getPageTitleModel() {
		return new Model("");
	}

}
