package org.apache.wicket.examples.yatzy.pages.multi;

import java.util.List;

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

public class MultiPlayerGameSetupPage extends AuthenticatedBasePage<Void> {

	public MultiPlayerGameSetupPage() {
		final WebMarkupContainer<Object> multiPlayerListContainer = new WebMarkupContainer<Object>(
				"multiPlayerListContainer");
		multiPlayerListContainer.setOutputMarkupId(true);
		add(multiPlayerListContainer);

		IModel<List<MultiPlayerGame>> multiPlayerListModel = new AbstractReadOnlyModel<List<MultiPlayerGame>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<MultiPlayerGame> getObject() {
				return YatzyApplication.get().getMultiPlayerGames();
			}
		};

		ListView<MultiPlayerGame> multiPlayerList = new ListView<MultiPlayerGame>("multiPlayerList",
				multiPlayerListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MultiPlayerGame> item) {
				Link<MultiPlayerGame> link = new Link<MultiPlayerGame>("link", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						MultiPlayerGame multiPlayerGame = getModelObject();

						multiPlayerGame.addPlayer(YatzySession.get().getPlayer());

						getRequestCycle().setResponsePage(new MultiPlayerGamePage(multiPlayerGame));
					}
				};
				item.add(link);

				link.add(new Label<String>("game", new StringResourceModel("game.${class.simpleName}", this,
						new PropertyModel<String>(item.getModel(), "innerGame"))));

				item
						.add(new Label<String>("playerCount", new PropertyModel<String>(item.getModel(),
								"numberOfPlayers")));

				item.add(new Label<String>("maxPlayers", new PropertyModel<String>(item.getModel(), "maxPlayers")));
			}
		};
		multiPlayerListContainer.add(multiPlayerList);
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new Model<String>("");
	}

}
