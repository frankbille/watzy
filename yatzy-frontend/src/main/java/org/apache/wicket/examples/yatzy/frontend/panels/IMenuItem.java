package org.apache.wicket.examples.yatzy.frontend.panels;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

public interface IMenuItem extends Serializable {

	MarkupContainer<?> createLink(String wicketId);

	Component<?> createLabelComponent(String wicketId);

}
