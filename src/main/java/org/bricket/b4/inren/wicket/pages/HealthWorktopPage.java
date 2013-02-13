package org.bricket.b4.inren.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.inren.wicket.templates.SecuredPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Ingo Renner
 * 
 */
@MountPath(value = "/health")
public class HealthWorktopPage extends SecuredPage<Measurement> {

    @Override
    public Component createPanel(String wicketId) {
	return new Label(wicketId, "to be fixed");
    }

}
