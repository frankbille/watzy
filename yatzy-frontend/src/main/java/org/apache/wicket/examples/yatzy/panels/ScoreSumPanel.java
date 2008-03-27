package org.apache.wicket.examples.yatzy.panels;

import java.io.Serializable;

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
import org.examples.yatzy.score.IScoreGroup;

public class ScoreSumPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static interface SumProvider extends Serializable {
		boolean hasSum(IScoreGroup scoreGroup, IPlayer player);

		int getSum(IScoreGroup scoreGroup, IPlayer player);
	}

	public ScoreSumPanel(String id, final IModel turnModel, IModel labelModel, IModel scoreModel,
			final SumProvider sumProvider) {
		super(id, scoreModel);

		add(new Label("sumLabel", labelModel));

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
						return turn != null && turn.getPlayer() == player ? "currentPlayerSum" : null;
					}
				}, " "));

				AbstractReadOnlyModel model = new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						IScoreGroup scoreGroup = (IScoreGroup) ScoreSumPanel.this.getModelObject();
						if (sumProvider.hasSum(scoreGroup, player)) {
							return sumProvider.getSum(scoreGroup, player);
						} else {
							return "&nbsp;";
						}
					}
				};

				Label sumLabel = new Label("sum", model);
				sumLabel.setEscapeModelStrings(false);
				sumLabel.setRenderBodyOnly(true);
				item.add(sumLabel);
			}
		});
	}

}
