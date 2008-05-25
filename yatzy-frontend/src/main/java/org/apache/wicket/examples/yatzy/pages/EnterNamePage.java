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

public class EnterNamePage extends BasePage<Void> {

	public EnterNamePage() {
		final AdhocPlayer player = new AdhocPlayer();

		Form<Object> form = new Form<Object>("form") {
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

		TextField<String> nameField = new TextField<String>("name", new PropertyModel<String>(player, "name"));
		nameField.setRequired(true);
		form.add(nameField);

		form.add(new Button<String>("continue", new StringResourceModel("continue", this, null)));
	}

	@Override
	protected IModel<String> getPageTitleModel() {
		return new StringResourceModel("enterName", this, null);
	}

}
