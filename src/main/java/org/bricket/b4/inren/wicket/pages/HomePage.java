package org.bricket.b4.inren.wicket.pages;

import org.bricket.b4.inren.wicket.templates.TemplatePage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * 
 * @author Ingo Renner
 * 
 */

@MountPath(value = "/h", alt = "/home")
public class HomePage<T> extends TemplatePage<T> {
    public HomePage() {
	super();
    }
}