package org.apache.wicket.examples.yatzy.frontend.components.menu;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.examples.yatzy.frontend.behaviours.jquery.JQueryBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public abstract class ExpandableContentMenuItem extends AbstractSimpleLabelMenuItem implements
		IExpandableContentMenuItem {

	public static final ResourceReference JS_EXPANDABLE_CONTENT = new JavascriptResourceReference(
			ExpandableContentMenuItem.class, "expandable-content.js");

	private ContentHolderMarkupIdProvider markupIdProvider;

	public ExpandableContentMenuItem(String label) {
		super(label);
	}

	public ExpandableContentMenuItem(IModel<String> labelModel) {
		super(labelModel);
	}

	public void setContentHolderMarkupIdProvider(ContentHolderMarkupIdProvider markupIdProvider) {
		this.markupIdProvider = markupIdProvider;
	}

	public MarkupContainer<Object> createLink(String wicketId) {
		WebMarkupContainer<Object> link = new WebMarkupContainer<Object>(wicketId);
		link.add(new SimpleAttributeModifier("href", "#"));
		link.add(new JQueryBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);

				String lf = System.getProperty("line.separator");

				response.renderJavascriptReference(JS_EXPANDABLE_CONTENT);

				StringBuilder jsConf = new StringBuilder();
				jsConf.append("$('.expandableMenu').width('100%');").append(lf);
				jsConf.append("$('.expandableMenu').bind('mouseleave', function() {").append(lf);
				jsConf.append("$(this).find('.content').slideUp().removeClass('displayed');").append(lf);
				jsConf.append("});").append(lf);
				response.renderOnDomReadyJavascript(jsConf.toString());
			}
		});
		link.add(new AttributeModifier("onclick", true, new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "ExpandableContent.displayContent('" + markupIdProvider.getMarkupId() + "'); return false;";
			}
		}));
		return link;
	}
}
