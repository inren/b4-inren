package de.inren.frontend.auth;

import org.apache.wicket.Component;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.templates.DefaultPage;

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
