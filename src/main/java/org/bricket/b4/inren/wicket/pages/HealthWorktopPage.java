package org.bricket.b4.inren.wicket.pages;

import org.apache.wicket.Component;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.wicket.HealthWorktopPanel;
import org.bricket.b4.inren.wicket.templates.DefaultPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Ingo Renner
 * 
 */
@MountPath(value = "/health")
public class HealthWorktopPage extends DefaultPage<Measurement> {

    @Override
    public Component createPanel(String wicketId) {
	return new HealthWorktopPanel(wicketId);
    }

}
