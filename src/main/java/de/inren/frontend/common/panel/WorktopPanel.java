/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.inren.frontend.common.panel;

import org.apache.wicket.markup.html.panel.Panel;

import de.inren.frontend.common.manage.IWorktopManageDelegate;

/**
 * Worktop is british for workplace.
 * 
 * @author Ingo Renner
 *
 */
public class WorktopPanel extends Panel {

    private final String COMPONENT_ID = "WorktopPanelId";
    private IWorktopManageDelegate delegate;

    public WorktopPanel(String id) {	
	super(id);
    }

    public String getComponentId() {
        return COMPONENT_ID;
    }
    
    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (!hasBeenRendered()) {
            add(delegate.getManagePanel());
        }
    }

    public void setDelegate(IWorktopManageDelegate delegate) {
        this.delegate = delegate;
    }
}
