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
package org.bricket.b4.core.wicket.panel;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Worktop is british for workplace.
 * 
 * You should use this panel in a module, to create your workflow.
 * For example: You add a managementPanel (with a table and edit button for rows) and a create button.
 * When doing edit or create, you replace the managementPanel with an etditOrCreatePanel and an edit 
 * and save button. When these buttons are pressed, the action is performed by the current panel and 
 * than the current panel is replaced again by the managementPanel, with the updated data. 
 * 
 * This way, the application just has to add the specific implementation of this panel to an application
 * specific page. This way, the modules are independent from the application pages. 
 * 
 * @author Ingo Renner
 *
 */
public class WorktopPanel extends Panel {

    public static final String COMPONENT_ID = "WorktopPanelId";

    public WorktopPanel(String id) {	
	super(id);
    }
}
