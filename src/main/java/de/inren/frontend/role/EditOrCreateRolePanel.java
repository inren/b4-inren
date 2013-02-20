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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.securityinren.entity.Role;
import org.bricket.b4.securityinren.service.RoleService;

import de.inren.frontend.common.manage.IWorktopManageDelegate;
import de.inren.frontend.common.panel.ABasePanel;
import de.inren.frontend.common.panel.IAdminPanel;

/**
 * @author Ingo Renner
 *
 */
public class EditOrCreateRolePanel extends ABasePanel implements IAdminPanel {
    @SpringBean
    private RoleService roleService;

    private IWorktopManageDelegate<Role> delegate;
    
    final Role role;

    public EditOrCreateRolePanel(String componentId, IModel<Role> m, IWorktopManageDelegate<Role> delegate) {
        super(componentId);
        if (m!=null) {
            role = m.getObject();
        } else {
            role = new Role();
        }
        this.delegate = delegate;
    }

    @Override
    protected void initGui() {

        Form<Role> form = new Form<Role>("form", new CompoundPropertyModel<Role>(role));

        StringResourceModel lName = new StringResourceModel("name.label", EditOrCreateRolePanel.this, null);
        form.add(new Label("name.label", lName));
        form.add(new TextField<String>("name", String.class).setRequired(true).setLabel(lName)
                .setRequired(false).setLabel(lName));


        form.add(new AjaxLink<Void>("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getSession().getFeedbackMessages().clear();
                delegate.switchToComponent(target, delegate.getManagePanel());
            }
        });

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    Role u = roleService.saveRole((Role) form.getModelObject());
                    form.info(new StringResourceModel("feedback.success", EditOrCreateRolePanel.this, null).getString());
                    delegate.switchToComponent(target, delegate.getManagePanel());
                } catch (B4ServiceException e) {
                    form.error(new StringResourceModel(e.getKey(), EditOrCreateRolePanel.this, null).getString());
                    target.add(getFeedback());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                FeedbackPanel f = getFeedback();
                if (target!=null && f!=null) {
                    target.add(f);
                }
            }
        });

        add(form);
    }
}
