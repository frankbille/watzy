package org.apache.wicket.examples.yatzy.pages;

import org.apache.wicket.examples.yatzy.YatzyApplication;
import org.apache.wicket.examples.yatzy.YatzySession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.examples.yatzy.AdhocPlayer;

public class EnterNamePage extends BasePage {

	public EnterNamePage() {
		final AdhocPlayer player = new AdhocPlayer();

		Form form = new Form("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				YatzySession.get().setPlayer(player);

				if (continueToOriginalDestination() == false) {
					getRequestCycle().setResponsePage(YatzyApplication.get().getHomePage());
				}
			}
		};
		add(form);

		TextField nameField = new TextField("name", new PropertyModel(player, "name"));
		nameField.setRequired(true);
		form.add(nameField);

		form.add(new Button("continue", new StringResourceModel("continue", this, null)));
	}

	@Override
	protected IModel getPageTitleModel() {
		return new StringResourceModel("enterName", this, null);
	}

}
