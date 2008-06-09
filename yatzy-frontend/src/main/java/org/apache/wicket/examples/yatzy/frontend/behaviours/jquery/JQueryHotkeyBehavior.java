package org.apache.wicket.examples.yatzy.frontend.behaviours.jquery;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class JQueryHotkeyBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_JQUERY_HOTKEYS = new ResourceReference(
			JQueryHotkeyBehavior.class, "jquery.hotkeys.js");

	private Component<?> component;
	private final IModel<String> shortcut;
	private final IModel<String> callback;
	private final boolean disableInInput;

	public JQueryHotkeyBehavior(IModel<String> shortcut, IModel<String> callback) {
		this(shortcut, callback, false);
	}

	public JQueryHotkeyBehavior(IModel<String> shortcut, IModel<String> callback,
			boolean disableInInput) {
		this.callback = callback;
		this.shortcut = shortcut;
		this.disableInInput = disableInInput;
	}

	@Override
	public void bind(Component<?> component) {
		this.component = component;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		if (isEnabled(component)) {
			super.renderHead(response);

			response.renderJavascriptReference(JS_JQUERY_HOTKEYS);

			StringBuilder b = new StringBuilder();
			b.append("jQuery.hotkeys.add('").append(shortcut.getObject()).append("', ");
			if (disableInInput) {
				b.append("{disableInInput:true}, ");
			}
			b.append("function() {").append(callback.getObject()).append("});");

			response.renderOnDomReadyJavascript(b.toString());
		}
	}

}
