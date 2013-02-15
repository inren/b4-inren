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

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.bricket.b4.core.wicket.B4WebSession;
import org.bricket.b4.core.wicket.IB4Application;
import org.bricket.b4.securityinren.service.RoleService.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Base panel with common utility methods.
 * 
 * @author Ingo Renner
 * @author Henning Teek
 */
public abstract class ABasePanel extends Panel {
    
    private FeedbackPanel feedback;
    
    public ABasePanel(String id) {
        super(id);
    }
    
    public ABasePanel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        this.feedback = getApplication() instanceof IB4Application ? ((IB4Application) getApplication())
                .getFeedbackPanel(getPage()) : null;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!hasBeenRendered()) {
            initGui();
        }
    }

    protected abstract void initGui();

    /**
     * @return the feedback panel
     */
    public FeedbackPanel getFeedback() {
        return feedback;
    }

    /**
     * @return the logged in user
     */
    public UserDetails getUser() {
        return ((B4WebSession) getSession()).getUser();
    }


    /**
     * @return true if a user is signed in. false otherwise
     */
    public boolean isSignedIn() {
        return ((B4WebSession) getSession()).isSignedIn();
    }


    /**
     * @return current authenticated wicket web session
     */
    public B4WebSession getB4WebSession() {
        return (B4WebSession) B4WebSession.get();
    }

    public boolean isSuperuser() {
        return hasRole(Roles.ROLE_ADMIN.name());
    }

    public boolean hasRole(String role) {
        for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
