package org.apache.wicket.examples.yatzy.frontend.behaviours.jquery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;

public class JQueryScrollToBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY_SCROLLTO = new ResourceReference(
			JQueryScrollToBehavior.class, "jquery.scrollTo.js");

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavascriptReference(JS_JQUERY_SCROLLTO);
	}

}
