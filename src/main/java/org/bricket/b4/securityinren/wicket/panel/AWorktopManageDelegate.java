package org.bricket.b4.securityinren.wicket.panel;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.bricket.b4.core.wicket.panel.WorktopPanel;

/**
 * @author Ingo Renner
 *
 */
public abstract class AWorktopManageDelegate<T extends Serializable> implements IWorktopManageDelegate<T> {

    private WorktopPanel panel;

    public AWorktopManageDelegate(WorktopPanel panel) {
        this.panel = panel;
    }

    public void switchToComponent(AjaxRequestTarget target, Component replacement) {
        if (target!=null) {
            target.add(replacement);
        }
        panel.addOrReplace(replacement);
    }

    public WorktopPanel getPanel() {
        return panel;
    }
}