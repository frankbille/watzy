package org.apache.wicket.examples.yatzy.frontend.behaviours.jquery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class JQueryBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY = new ResourceReference(JQueryBehavior.class,
			"jquery.js");

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JS_JQUERY);
	}

}
