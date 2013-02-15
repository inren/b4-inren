package de.inren.frontend.common.templates;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * @author Ingo Renner
 * 
 * @param <T>
 */

public class SecuredPage<T> extends TemplatePage<T> implements B4Secured{

    private final static String WICKET_ID = "panel";

    @Override
    protected void onInitialize() {
	super.onInitialize();
	if (!hasBeenRendered()) {
	    add(createPanel(WICKET_ID));
	}
    }

    public Component createPanel(String wicketId) {
	return new Label(wicketId,
		"Please override 'createPanel(String wicketId)' in "
			+ getClass().getSimpleName());
    }
}
