package de.inren.frontend.application;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.templates.TemplatePage;

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