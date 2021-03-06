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
package de.inren.frontend.health;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.bricket.b4.health.service.MeasurementService;
import org.bricket.b4.securityinren.service.impl.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonGroup;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.table.TableBehavior;
import de.inren.frontend.common.dataprovider.RepositoryDataProvider;
import de.inren.frontend.common.manage.IWorktopManageDelegate;
import de.inren.frontend.common.panel.IAdminPanel;
import de.inren.frontend.common.panel.ManagePanel;
import de.inren.frontend.common.table.AjaxFallbackDefaultDataTableBuilder;

/**
 * @author Ingo Renner
 *
 */
public class ManageMeasurementsPanel extends ManagePanel implements IAdminPanel {

    @SpringBean
    private MeasurementService measurementService;
    
    @SpringBean
    private MeasurementRepository measurementRepository;
    
    private IWorktopManageDelegate<Measurement> delegate;

    public ManageMeasurementsPanel(String id, IWorktopManageDelegate<Measurement> delegate) {
        super(id);
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Component getTable(final String id) {
        AjaxFallbackDefaultDataTableBuilder<Measurement> builder = new AjaxFallbackDefaultDataTableBuilder<Measurement>(ManageMeasurementsPanel.this);
         
        Component table =  builder.addDataProvider(new RepositoryDataProvider<Measurement>(measurementRepository){

            @Override
            protected Page<Measurement> getPage(Pageable pageable) {
                return measurementRepository.findByUid(((UserDetailsImpl) getUser()).getUid(), pageable);
            }

            @Override
            public long size() {
                return measurementRepository.findByUid(((UserDetailsImpl) getUser()).getUid()).size();
            }
            
        })
                .add(new AbstractColumn<Object, Object>(new StringResourceModel("actions.label", ManageMeasurementsPanel.this, null)) {
                    @Override
                    public void populateItem(Item<ICellPopulator<Object>> cellItem, String componentId, IModel<Object> rowModel) {

                        final Measurement measurement = (Measurement) rowModel.getObject();

                        ButtonGroup bg = new ButtonGroup(componentId){

                            @Override
                            protected List<AbstractLink> newButtons(String buttonMarkupId) {
                                List<AbstractLink> res = new ArrayList<AbstractLink>();
                                BootstrapAjaxLink<String> edit = new BootstrapAjaxLink<String>("button", Buttons.Type.Menu){
                                    
                                    @Override
                                    public void onClick(AjaxRequestTarget target) {
                                        delegate.switchToComponent(target, delegate.getEditPanel(new Model<Measurement>(measurement)));
                                        
                                    }
                                };
                                edit.setIconType(IconType.pencil);
                                edit.setSize(Buttons.Size.Mini);
                                edit.setInverted(false);
                                res.add(edit);
                                
                                BootstrapAjaxLink<String> delete = new BootstrapAjaxLink<String>("button", Buttons.Type.Menu){
                                    
                                    @Override
                                    public void onClick(AjaxRequestTarget target) {
                                        
                                        try {
                                            // feedback
                                            getSession().getFeedbackMessages().clear();
                                            target.add(getFeedback());
                                            // delete
                                            measurementRepository.delete(measurement.getId());
                                            // manage
                                            Component table = getTable(id);
                                            ManageMeasurementsPanel.this.addOrReplace(table);
                                            target.add(table);
                                        } catch (Exception e) {
                                            error(e.getMessage());
                                            target.add(getFeedback());
                                        }
                                    }
                                };
                                delete.setIconType(IconType.trash);
                                delete.setSize(Buttons.Size.Mini);
                                delete.setInverted(false);
                                res.add(delete);
                                return res;
                            }
                        };
                            
                            
                            // bg.add(new ToolbarBehavior());
                            cellItem.add(bg);
                    }
                }).addPropertyColumn("id", true)
                .addPropertyColumn("uid", true)
//                .addPropertyColumn("weight", true)
//                .addPropertyColumn("fat", true)
//                .addPropertyColumn("water", true)
                .add(new HealthColumn<Measurement>(new Model<String>("weight"), "weight", "weight"))
                .add(new HealthColumn<Measurement>(new Model<String>("fat"), "fat", "fat"))
                .add(new HealthColumn<Measurement>(new Model<String>("water"), "water","water"))
                .setNumberOfRows(10)
                
                .build(id);
        TableBehavior tableBehavior = new TableBehavior().bordered().condensed();
        table.add(tableBehavior);
        return table;
    }

    @Override
    protected Component getActionPanel(String id) {

        ButtonGroup bg = new ButtonGroup(id){

            @Override
            protected List<AbstractLink> newButtons(String buttonMarkupId) {
                List<AbstractLink> res = new ArrayList<AbstractLink>();
                StringResourceModel srm = new StringResourceModel("actions.create.measurement", ManageMeasurementsPanel.this, null);
                BootstrapAjaxLink<String> create = new BootstrapAjaxLink<String>("button", srm, Buttons.Type.Primary){
                    
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        delegate.switchToComponent(target, delegate.getEditPanel(null));
                    }};
                    create.setIconType(IconType.plussign);
                    res.add(create);
                return res;
            }
            };
        return bg;
    }
}
