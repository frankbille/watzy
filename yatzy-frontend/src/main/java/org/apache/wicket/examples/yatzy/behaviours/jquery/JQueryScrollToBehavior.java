package org.apache.wicket.examples.yatzy.behaviours.jquery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class JQueryScrollToBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY_SCROLLTO = new ResourceReference(JQueryScrollToBehavior.class,
			"jquery.scrollTo.js");

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JS_JQUERY_SCROLLTO);
	}

}
