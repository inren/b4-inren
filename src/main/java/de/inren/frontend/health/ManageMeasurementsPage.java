package de.inren.frontend.health;

import org.apache.wicket.Component;
import org.bricket.b4.health.entity.Measurement;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.panel.WorktopPanel;
import de.inren.frontend.common.templates.SecuredPage;

/**
 * @author Ingo Renner
 * 
 */
@MountPath(value = "/measurements")
public class ManageMeasurementsPage extends SecuredPage<Measurement> {

    @Override
    public Component createPanel(String wicketId) {
        final WorktopPanel w = new WorktopPanel (wicketId);
        w.setDelegate(new MeasurementWorktopManageDelegate(w));
        return w;
    }

}
