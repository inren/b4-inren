package de.inren.frontend.common.manage;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

import de.inren.frontend.common.panel.WorktopPanel;

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