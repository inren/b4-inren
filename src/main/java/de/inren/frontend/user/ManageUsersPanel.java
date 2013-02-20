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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.repository.UserRepository;
import org.bricket.b4.securityinren.service.UserService;

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonGroup;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonSize;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedAjaxLink;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.table.TableBehavior;
import de.inren.frontend.common.dataprovider.RepositoryDataProvider;
import de.inren.frontend.common.manage.IWorktopManageDelegate;
import de.inren.frontend.common.panel.ActionPanelBuilder;
import de.inren.frontend.common.panel.ManagePanel;
import de.inren.frontend.common.panel.CreateActionLink;
import de.inren.frontend.common.panel.DeleteActionLink;
import de.inren.frontend.common.panel.EditActionLink;
import de.inren.frontend.common.panel.IAdminPanel;
import de.inren.frontend.common.table.AjaxFallbackDefaultDataTableBuilder;

/**
 * @author Ingo Renner
 */
public class ManageUsersPanel extends ManagePanel implements IAdminPanel {
    
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
         
        Component table =  builder.addDataProvider(new RepositoryDataProvider<User>(userRepository))
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
                        
//                         cellItem.add(linkBuilder.build(componentId));
                        
                        ButtonGroup bg = new ButtonGroup(componentId);
                        TypedAjaxLink<String> edit = new TypedAjaxLink<String>("button", ButtonType.Menu){

                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                delegate.switchToComponent(target, delegate.getEditPanel(new Model<User>(user)));
                                
                            }};
                            edit.setIconType(IconType.pencil);
                            edit.setSize(ButtonSize.Mini);
                            edit.setInverted(false);
                            bg.addButton(edit);
                            
                            // only delete other users
                            if (!getUser().getUsername().equals(user.getEmail())) {
                                TypedAjaxLink<String> delete = new TypedAjaxLink<String>("button", ButtonType.Menu){

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
                                delete.setIconType(IconType.trash);
                                delete.setSize(ButtonSize.Mini);
                                delete.setInverted(false);
                                bg.addButton(delete);
                            }
                            // bg.add(new ToolbarBehavior());
                            cellItem.add(bg);
                    }
                }).addPropertyColumn("id", true).addPropertyColumn("email", true).addPropertyColumn("firstname", true)
                .addPropertyColumn("lastname", true).addBooleanPropertyColumn("enabled", true)
                .addPropertyColumn("activationKey", true).setNumberOfRows(10).build(id);
        TableBehavior tableBehavior = new TableBehavior().bordered().condensed();
        table.add(tableBehavior);
        return table;
    }

    @Override
    protected Component getActionPanel(String id) {
        // create link
        StringResourceModel srm = new StringResourceModel("actions.create.user", ManageUsersPanel.this, null);
        CreateActionLink link = new CreateActionLink(true, null, srm) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                delegate.switchToComponent(target, delegate.getEditPanel(null));
            }
        };

        ButtonGroup bg = new ButtonGroup(id);
        TypedAjaxLink<String> create = new TypedAjaxLink<String>("button", srm, ButtonType.Primary){

            @Override
            public void onClick(AjaxRequestTarget target) {
                delegate.switchToComponent(target, delegate.getEditPanel(null));
            }};
            create.setIconType(IconType.plussign);
        bg.addButton(create);
        
        
        
        final ActionPanelBuilder linkBuilder = ActionPanelBuilder.getBuilder();
        linkBuilder.add(link);
        return bg; //linkBuilder.build(id);
    }
    
}
