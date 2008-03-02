package org.apache.wicket.examples.yatzy.components.menu;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.examples.yatzy.behaviours.jquery.JQueryBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public abstract class ExpandableContentMenuItem extends AbstractSimpleLabelMenuItem implements
		IExpandableContentMenuItem {
	private ContentHolderMarkupIdProvider markupIdProvider;

	public ExpandableContentMenuItem(String label) {
		super(label);
	}

	public ExpandableContentMenuItem(IModel labelModel) {
		super(labelModel);
	}

	public void setContentHolderMarkupIdProvider(ContentHolderMarkupIdProvider markupIdProvider) {
		this.markupIdProvider = markupIdProvider;
	}

	public MarkupContainer createLink(String wicketId) {
		WebMarkupContainer link = new WebMarkupContainer(wicketId);
		link.add(new JQueryBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);

				String lf = System.getProperty("line.separator");

				StringBuilder js = new StringBuilder();
				js.append("function displayContent(content) {").append(lf);
				js.append("if ($('.expandableMenu .content').hasClass('displayed')) {").append(lf);
				js.append("$('.expandableMenu .content').hide();").append(lf);
				js.append("$('#' + content).show();").append(lf);
				js.append("} else {").append(lf);
				js.append("$('#' + content).slideDown().addClass('displayed');").append(lf);
				js.append("}").append(lf);
				js.append("}").append(lf);
				response.renderJavascript(js, "expandableContent");

				StringBuilder jsConf = new StringBuilder();
				jsConf.append("$('.expandableMenu').width('100%');").append(lf);
				jsConf.append("$('.expandableMenu').bind('mouseleave', function() {").append(lf);
				jsConf.append("$(this).find('.content').slideUp().removeClass('displayed');").append(lf);
				jsConf.append("});").append(lf);
				response.renderOnDomReadyJavascript(jsConf.toString());
			}
		});
		link.add(new AttributeModifier("onclick", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				return "displayContent('" + markupIdProvider.getMarkupId() + "'); return false;";
			}
		}));
		return link;
	}
}
