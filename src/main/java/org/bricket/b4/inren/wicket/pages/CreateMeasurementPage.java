package org.bricket.b4.inren.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.wicket.EditOrCreateMeasurementPanel;
import org.bricket.b4.inren.wicket.templates.DefaultPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Ingo Renner
 * 
 */
@MountPath(value = "/CreateMeasurement")
public class CreateMeasurementPage extends DefaultPage<Measurement> {

    public CreateMeasurementPage() {
	super();
    }

    @Override
    public Component createPanel(String wicketId) {
	return new EditOrCreateMeasurementPanel(wicketId,
		new Model<Measurement>(null));
    }

}
