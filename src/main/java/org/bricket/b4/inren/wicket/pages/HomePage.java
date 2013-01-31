package org.bricket.b4.inren.wicket.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.bricket.b4.inren.wicket.templates.TemplatePage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * 
 * @author Ingo Renner
 * 
 */

@MountPath(value = "/h", alt = "/home")
public class HomePage<T> extends TemplatePage<T> {
    public HomePage(IModel<T> model) {
        super(model);
    }

    public HomePage(PageParameters parameters) {
        super(parameters);
    }

    public HomePage() {
	//super();
    }
}