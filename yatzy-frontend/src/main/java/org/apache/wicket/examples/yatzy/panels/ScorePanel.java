package org.apache.wicket.examples.yatzy.panels;

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

public abstract class ScorePanel extends Panel {

	public ScorePanel(String id, final IModel turnModel, final IModel scoreModel) {
		super(id, scoreModel);

		AjaxFallbackLink combinationLink = new AjaxFallbackLink("combinationLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ITurn turn = (ITurn) turnModel.getObject();
				if (turn.hasValue()) {
					ITurnScore turnScore = (ITurnScore) scoreModel.getObject();

					turnScore.setTurn(turn);

					combinationSelected(target, scoreModel);
				}
			}

			@Override
			public boolean isEnabled() {
				ITurnScore turnScore = (ITurnScore) scoreModel.getObject();
				ITurn turn = (ITurn) turnModel.getObject();
				return turn != null && turnScore.hasScore(turn.getPlayer()) == false;
			}
		};
		add(combinationLink);
		Label combinationlabel = new Label("combinationLabel", ScoreResourceModelFactory.createModel(scoreModel));
		combinationlabel.setRenderBodyOnly(true);
		combinationLink.add(combinationlabel);

		add(new ListView("players", new PropertyModel(scoreModel, "players")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final IPlayer player = (IPlayer) item.getModelObject();

				item.add(new AttributeAppender("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						ITurn turn = (ITurn) turnModel.getObject();
						return turn != null && turn.getPlayer() == player ? "currentPlayer" : null;
					}
				}, " "));

				AbstractReadOnlyModel model = new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						IScore score = (IScore) scoreModel.getObject();
						return score.getScore(player);
					}
				};
				Label scoreLabel = new Label("score", model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						IScore score = (IScore) scoreModel.getObject();
						return score.hasScore(player);
					}
				};
				scoreLabel.setRenderBodyOnly(true);
				item.add(scoreLabel);
			}
		});
	}

	protected abstract void combinationSelected(AjaxRequestTarget target, IModel scoreModel);

}
