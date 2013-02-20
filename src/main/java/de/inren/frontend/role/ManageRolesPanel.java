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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.securityinren.entity.Role;
import org.bricket.b4.securityinren.repository.RoleRepository;
import org.bricket.b4.securityinren.service.RoleService;

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonGroup;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonSize;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedAjaxLink;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.table.TableBehavior;
import de.inren.frontend.common.dataprovider.RepositoryDataProvider;
import de.inren.frontend.common.manage.IWorktopManageDelegate;
import de.inren.frontend.common.panel.ActionPanelBuilder;
import de.inren.frontend.common.panel.EditActionLink;
import de.inren.frontend.common.panel.IAdminPanel;
import de.inren.frontend.common.panel.ManagePanel;
import de.inren.frontend.common.table.AjaxFallbackDefaultDataTableBuilder;

/**
 * @author Ingo Renner
 * 
 */
public class ManageRolesPanel extends ManagePanel implements IAdminPanel {
    @SpringBean
    private RoleService roleService;

    @SpringBean
    private RoleRepository roleRepository;

    private IWorktopManageDelegate<Role> delegate;

    public ManageRolesPanel(String id, IWorktopManageDelegate<Role> delegate) {
        super(id);
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Component getTable(final String id) {
        AjaxFallbackDefaultDataTableBuilder<Role> builder = new AjaxFallbackDefaultDataTableBuilder<Role>(ManageRolesPanel.this);

        Component table = builder.addDataProvider(new RepositoryDataProvider<Role>(roleRepository))
                .add(new AbstractColumn<Object, Object>(new StringResourceModel("actions.label", ManageRolesPanel.this, null)) {
                    @Override
                    public void populateItem(Item<ICellPopulator<Object>> cellItem, String componentId, IModel<Object> rowModel) {

                        final ActionPanelBuilder linkBuilder = ActionPanelBuilder.getBuilder();
                        final Role role = (Role) rowModel.getObject();
                        // edit link
                        linkBuilder.add(new EditActionLink(true) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                delegate.switchToComponent(target, delegate.getEditPanel(new Model<Role>(role)));
                            }
                        });

                        // TODO only delete roles not in use
                        ButtonGroup bg = new ButtonGroup(componentId);
                        TypedAjaxLink<String> edit = new TypedAjaxLink<String>("button", ButtonType.Menu) {

                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                delegate.switchToComponent(target, delegate.getEditPanel(new Model<Role>(role)));

                            }
                        };
                        edit.setIconType(IconType.pencil);
                        edit.setSize(ButtonSize.Mini);
                        edit.setInverted(false);
                        bg.addButton(edit);

                        // bg.add(new ToolbarBehavior());
                        cellItem.add(bg);
                    }
                }).addPropertyColumn("id", true).addPropertyColumn("name", true).setNumberOfRows(10).build(id);
        TableBehavior tableBehavior = new TableBehavior().bordered().condensed();
        table.add(tableBehavior);
        return table;
    }

    @Override
    protected Component getActionPanel(String id) {
        // create link
        StringResourceModel srm = new StringResourceModel("actions.create.role", ManageRolesPanel.this, null);
        ButtonGroup bg = new ButtonGroup(id);
        TypedAjaxLink<String> create = new TypedAjaxLink<String>("button", srm, ButtonType.Primary) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                delegate.switchToComponent(target, delegate.getEditPanel(null));
            }
        };
        create.setIconType(IconType.plussign);
        bg.addButton(create);
        return bg;
    }
}
