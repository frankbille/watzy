package org.apache.wicket.examples.yatzy.frontend.behaviours.jquery;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;

public class JQueryButtonBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;

	public static enum ButtonColor {
		GRAY("btn"), BLUE("btn blue"), GREEN("btn green"), PINK("btn pink");

		private final String cssClass;

		private ButtonColor(String cssClass) {
			this.cssClass = cssClass;
		}

		public String getCssClass() {
			return cssClass;
		}
	}

	public static final ResourceReference JS_JQUERY_BUTTON = new ResourceReference(
			JQueryBehavior.class, "jquery.button.js");

	private final ButtonColor buttonColor;

	public JQueryButtonBehavior() {
		this(ButtonColor.BLUE);
	}

	public JQueryButtonBehavior(ButtonColor buttonColor) {
		this.buttonColor = buttonColor;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavascriptReference(JS_JQUERY_BUTTON);

		response.renderOnDomReadyJavascript("initializeButtons()");
	}

	@Override
	public void bind(Component component) {
		super.bind(component);

		component.add(new AttributeAppender("class", true, new Model<String>(buttonColor
				.getCssClass()), " "));
	}

}
