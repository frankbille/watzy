package org.apache.wicket.examples.yatzy.frontend.behaviours.jquery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;

public class JQueryDimensionsBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY_DIMENSIONS = new ResourceReference(
			JQueryDimensionsBehavior.class, "jquery.dimensions.js");

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavascriptReference(JS_JQUERY_DIMENSIONS);
	}

}
