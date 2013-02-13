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
package org.bricket.b4.securityinren.wicket.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.core.wicket.dataprovider.RepositoryDataProvider;
import org.bricket.b4.core.wicket.panel.ActionPanelBuilder;
import org.bricket.b4.core.wicket.panel.B4ManagePanel;
import org.bricket.b4.core.wicket.panel.CreateActionLink;
import org.bricket.b4.core.wicket.panel.DeleteActionLink;
import org.bricket.b4.core.wicket.panel.EditActionLink;
import org.bricket.b4.core.wicket.panel.IAdminPanel;
import org.bricket.b4.core.wicket.table.AjaxFallbackDefaultDataTableBuilder;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.repository.UserRepository;
import org.bricket.b4.securityinren.service.UserService;

/**
 * @author Ingo Renner
 */
public class ManageUsersPanel extends B4ManagePanel implements IAdminPanel {
    
    @SpringBean
    private UserService userService;
    
    @SpringBean
    private UserRepository userRepository;
    
    private IWorktopManageDelegate<User> delegate;

    public ManageUsersPanel(String id, IWorktopManageDelegate<User> delegate) {
        super(id);
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Component getTable(final String id) {
        AjaxFallbackDefaultDataTableBuilder<User> builder = new AjaxFallbackDefaultDataTableBuilder<User>(ManageUsersPanel.this);
        return 
                builder.addDataProvider(new RepositoryDataProvider<User>(userRepository))
                .add(new AbstractColumn<Object, Object>(new StringResourceModel("actions.label", ManageUsersPanel.this, null)) {
                    @Override
                    public void populateItem(Item<ICellPopulator<Object>> cellItem, String componentId, IModel<Object> rowModel) {

                        final ActionPanelBuilder linkBuilder = ActionPanelBuilder.getBuilder();
                        final User user = (User) rowModel.getObject();
                        // edit link
                        linkBuilder.add(new EditActionLink(true) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                delegate.switchToComponent(target, delegate.getEditPanel(new Model<User>(user)));
                            }
                        });
                    
                        // only delete other users
                        if (!getUser().getUsername().equals(user.getEmail())) {
                            DeleteActionLink removeLink = new DeleteActionLink(new StringResourceModel(
                                    "actions.delete.confirm", ManageUsersPanel.this, null), true) {
                                @Override
                                public void onClick(AjaxRequestTarget target) {
                                    try {
                                        // feedback
                                        getSession().getFeedbackMessages().clear();
                                        target.add(getFeedback());
                                        // delete
                                        userRepository.delete(user.getId());
                                        // manage
                                        Component table = getTable(id);
                                        ManageUsersPanel.this.addOrReplace(table);
                                        target.add(table);
                                    } catch (Exception e) {
                                        error(e.getMessage());
                                        target.add(getFeedback());
                                    }
                                }
                            };
                            linkBuilder.add(removeLink);
                        }
                        cellItem.add(linkBuilder.build(componentId));
                    }
                }).addPropertyColumn("id", true).addPropertyColumn("email", true).addPropertyColumn("firstname", true)
                .addPropertyColumn("lastname", true).addBooleanPropertyColumn("enabled", true)
                .addPropertyColumn("activationKey", true).setNumberOfRows(10).build(id);
    }

    @Override
    protected Component getActionPanel(String id) {
        final ActionPanelBuilder linkBuilder = ActionPanelBuilder.getBuilder();
        // create link
        StringResourceModel srm = new StringResourceModel("actions.create.user", ManageUsersPanel.this, null);
        linkBuilder.add(new CreateActionLink(true, null, srm) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                delegate.switchToComponent(target, delegate.getEditPanel(null));
            }
        });
        return linkBuilder.build(id);
    }
    
}
