package org.apache.wicket.examples.yatzy.frontend.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryButtonBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.examples.yatzy.IPlayer;

public abstract class EnterNamePanel extends Panel<IPlayer> {
	private static final long serialVersionUID = 1L;

	public EnterNamePanel(String id, IModel<IPlayer> model) {
		super(id, model);

		Form<Void> nameForm = new Form<Void>("nameForm");
		add(nameForm);

		nameForm.add(new TextField<String>("name", new PropertyModel<String>(model, "name")));

		AjaxSubmitLink<Void> changeNameLink = new AjaxSubmitLink<Void>("changeName") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNameChanged(target, EnterNamePanel.this.getModelObject());
			}
		};
		changeNameLink.add(new JQueryButtonBehavior());
		nameForm.add(changeNameLink);
	}

	protected abstract void onNameChanged(AjaxRequestTarget target, IPlayer player);

}
