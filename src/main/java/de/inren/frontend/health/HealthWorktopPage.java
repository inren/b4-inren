package de.inren.frontend.health;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.bricket.b4.health.entity.Measurement;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.templates.SecuredPage;

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
