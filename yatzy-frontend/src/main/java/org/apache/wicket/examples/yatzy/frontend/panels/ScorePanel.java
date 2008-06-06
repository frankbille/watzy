package org.apache.wicket.examples.yatzy.frontend.panels;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.examples.yatzy.IPlayer;
import org.examples.yatzy.ITurn;
import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.ITurnScore;

public abstract class ScorePanel extends Panel<ITurnScore> {
	private static final long serialVersionUID = 1L;

	public ScorePanel(String id, final IModel<ITurn> turnModel, final IModel<ITurnScore> scoreModel) {
		super(id, scoreModel);

		AjaxFallbackLink<Object> combinationLink = new AjaxFallbackLink<Object>("combinationLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ITurn turn = turnModel.getObject();
				if (turn.hasValue()) {
					ITurnScore turnScore = scoreModel.getObject();

					turnScore.setTurn(turn);

					combinationSelected(target, scoreModel);
				}
			}

			@Override
			public boolean isEnabled() {
				ITurnScore turnScore = scoreModel.getObject();
				ITurn turn = turnModel.getObject();
				return turn != null && turnScore.hasScore(turn.getPlayer()) == false
						&& combinationSelectable(scoreModel);
			}
		};
		add(combinationLink);
		Label<String> combinationlabel = new Label<String>("combinationLabel",
				ScoreResourceModelFactory.createModel(scoreModel));
		combinationlabel.setRenderBodyOnly(true);
		combinationLink.add(combinationlabel);

		add(new ListView<IPlayer>("players",
				new PropertyModel<List<IPlayer>>(scoreModel, "players")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IPlayer> item) {
				final IPlayer player = item.getModelObject();

				item.add(new AttributeAppender("class", true, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						ITurn turn = turnModel.getObject();
						return turn != null && turn.getPlayer() == player ? "currentPlayer" : null;
					}
				}, " "));

				AbstractReadOnlyModel<String> model = new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						IScore score = scoreModel.getObject();
						if (score.hasScore(player)) {
							return "" + score.getScore(player);
						} else {
							return "&nbsp;";
						}
					}
				};
				Label<String> scoreLabel = new Label<String>("score", model);
				scoreLabel.setEscapeModelStrings(false);
				scoreLabel.setRenderBodyOnly(true);
				item.add(scoreLabel);
			}
		});
	}

	protected abstract void combinationSelected(AjaxRequestTarget target,
			IModel<ITurnScore> scoreModel);

	protected boolean combinationSelectable(IModel<ITurnScore> scoreModel) {
		return true;
	}

}
