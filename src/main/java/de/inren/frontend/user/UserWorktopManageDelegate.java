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
package de.inren.frontend.user;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.bricket.b4.securityinren.entity.User;

import de.inren.frontend.common.manage.AWorktopManageDelegate;
import de.inren.frontend.common.panel.WorktopPanel;

/**
 * @author Ingo Renner
 *
 */
public class UserWorktopManageDelegate extends AWorktopManageDelegate<User> {

    public UserWorktopManageDelegate(WorktopPanel panel) {
        super(panel);
    }
    
    public Panel getManagePanel() {
        final ManageUsersPanel p = new ManageUsersPanel(getPanel().getComponentId(), this);
        p.setOutputMarkupId(true);
        return p;
    }
    
    public Panel getEditPanel(IModel<User> m) {
        final EditOrCreateUserPanel p = new EditOrCreateUserPanel(getPanel().getComponentId(), m, this);
        p.setOutputMarkupId(true);
        return p;
    }
}
