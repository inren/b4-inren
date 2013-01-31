package org.bricket.b4.inren.wicket.pages;

import org.apache.wicket.Component;
import org.bricket.b4.authentication.wicket.LoginPanel;
import org.bricket.b4.inren.wicket.templates.DefaultPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Ingo Renner
 * 
 */
@MountPath(value = "/login")
public class LoginPage extends DefaultPage<Object> {

    @Override
    public Component createPanel(String wicketId) {
	return new LoginPanel(wicketId);
    }

}
