package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.examples.yatzy.frontend.MultiPlayerGame;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.CompoundAjaxTimerBehavior;
import org.apache.wicket.examples.yatzy.frontend.behaviours.ajax.timer.StateBasedSelfUpdatingListener;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryScrollToBehavior;
import org.apache.wicket.examples.yatzy.frontend.pages.BasePage.ILeftMenuBlock;
import org.apache.wicket.examples.yatzy.frontend.panels.Chat.Message;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.examples.yatzy.IPlayer;

public class ChatPanel extends Panel<MultiPlayerGame> {
	private static final long serialVersionUID = 1L;

	public static class ChatBlock implements ILeftMenuBlock {
		private static final long serialVersionUID = 1L;

		private final IModel<MultiPlayerGame> model;
		private final CompoundAjaxTimerBehavior timerBehavior;

		public ChatBlock(IModel<MultiPlayerGame> model, CompoundAjaxTimerBehavior timerBehavior) {
			this.model = model;
			this.timerBehavior = timerBehavior;
		}

		public Component<?> createMenuBlock(String wicketId) {
			return new ChatPanel(wicketId, model, timerBehavior);
		}

		public CompoundAjaxTimerBehavior getTimerBehavior() {
			return timerBehavior;
		}

	}

	private IPlayer player;
	private String message;
	private final TextField<String> messageField;
	private final WebMarkupContainer<Chat> messagesContainer;

	private ChatPanel(String id, IModel<MultiPlayerGame> model,
			CompoundAjaxTimerBehavior timerBehavior) {
		super(id, model);

		add(new SimpleAttributeModifier("class", "chat"));

		messagesContainer = new WebMarkupContainer<Chat>("messagesContainer",
				new PropertyModel<Chat>(model, "chat"));
		messagesContainer.setOutputMarkupId(true);
		timerBehavior.addListener(new StateBasedSelfUpdatingListener<WebMarkupContainer<Chat>>(
				messagesContainer));
		messagesContainer.add(new JQueryScrollToBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);

				StringBuilder b = new StringBuilder();
				b.append("var mc = $('#").append(messagesContainer.getMarkupId()).append("');");
				b.append("mc.scrollTo(mc.find('.message:last'));");

				response.renderOnDomReadyJavascript(b.toString());
			}
		});
		add(messagesContainer);

		messagesContainer.add(new ListView<Message>("messages", new PropertyModel<List<Message>>(
				model, "chat.messages")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Message> item) {
				IModel<String> playerColor = new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						Chat chat = ChatPanel.this.getModelObject().getChat();
						IPlayer player = item.getModelObject().getPlayer();
						return "p" + chat.getPlayerNumber(player);
					}
				};
				item.add(new AttributeAppender("class", true, playerColor, " "));

				item.add(new Label<String>("name", item.getModelObject().getPlayer().getName()));

				item.add(new Label<String>("message", item.getModelObject().getMessage()));
			}
		});

		Form<Void> inputForm = new Form<Void>("inputForm");
		inputForm.add(new AjaxFormSubmitBehavior(inputForm, "onsubmit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target) {

			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				Chat chat = getModelObject().getChat();

				chat.addMessage(player, message);

				message = null;
				target.addComponent(messageField);
				target.addComponent(messagesContainer);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxCallDecorator() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence decorateScript(CharSequence script) {
						StringBuilder b = new StringBuilder();
						b.append(script);
						b.append("return false");
						return b;
					}
				};
			}
		});
		add(inputForm);

		final IModel<List<? extends IPlayer>> playersModel = new LoadableDetachableModel<List<? extends IPlayer>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends IPlayer> load() {
				List<IPlayer> players = new ArrayList<IPlayer>();

				List<IPlayer> allPlayers = getModelObject().getPlayers();
				for (IPlayer player : allPlayers) {
					if (getModelObject().isPlayingFromThisSeat(player)
							&& Strings.isEmpty(player.getName()) == false) {
						players.add(player);
					}
				}

				return players;
			}
		};

		IModel<IPlayer> playerModel = new IModel<IPlayer>() {
			private static final long serialVersionUID = 1L;

			public IPlayer getObject() {
				if (player == null) {
					List<? extends IPlayer> players = playersModel.getObject();
					if (players.isEmpty() == false) {
						player = players.get(0);
					}
				}

				return player;
			}

			public void setObject(IPlayer object) {
				player = object;
			}

			public void detach() {
			}
		};

		DropDownChoice<IPlayer> players = new DropDownChoice<IPlayer>("players", playerModel,
				playersModel, new ChoiceRenderer<IPlayer>("name")) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return playersModel.getObject().size() > 1;
			}
		};
		players.setOutputMarkupId(true);
		players.setOutputMarkupPlaceholderTag(true);
		players.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		timerBehavior.addListener(new StateBasedSelfUpdatingListener<DropDownChoice<IPlayer>>(
				players) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object getStateObject(DropDownChoice<IPlayer> component) {
				return component.getChoices().size();
			}
		});
		players.setNullValid(false);
		players.setRequired(true);
		inputForm.add(players);

		messageField = new TextField<String>("message", new PropertyModel<String>(this, "message"));
		messageField.setOutputMarkupId(true);
		messageField.setRequired(true);
		inputForm.add(messageField);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
