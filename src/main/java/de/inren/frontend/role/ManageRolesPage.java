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
package de.inren.frontend.role;

import org.apache.wicket.Component;
import org.bricket.b4.securityinren.entity.Role;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.panel.WorktopPanel;
import de.inren.frontend.common.templates.SecuredPage;

/**
 * @author Ingo Renner
 *
 */
@MountPath(value = "/roles")
public class ManageRolesPage  extends SecuredPage<Role> {

    @Override
    public Component createPanel(String wicketId) {
        final WorktopPanel w = new WorktopPanel (wicketId);
        w.setDelegate(new RoleWorktopManageDelegate(w));
        return w;
    }
}
