package de.inren.frontend.health;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.bricket.b4.health.entity.Measurement;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.templates.DefaultPage;

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
