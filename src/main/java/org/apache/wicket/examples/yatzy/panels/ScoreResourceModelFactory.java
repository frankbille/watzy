package org.apache.wicket.examples.yatzy.panels;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.examples.yatzy.domain.score.IScore;
import org.apache.wicket.examples.yatzy.domain.score.SumScore;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

public abstract class ScoreResourceModelFactory {

	public static IModel createModel(IModel scoreModel) {
		String resourceKey = "";

		IScore score = (IScore) scoreModel.getObject();

		resourceKey = score.getClass().getSimpleName();

		if (score instanceof SumScore) {
			SumScore sumScore = (SumScore) score;
			resourceKey += "." + sumScore.getValue();
		}

		return new StringResourceModel(resourceKey, null, null) {
			private static final long serialVersionUID = 1L;

			@Override
			public Localizer getLocalizer() {
				return Application.get().getResourceSettings().getLocalizer();
			}
		};
	}

	private ScoreResourceModelFactory() {
	}

}