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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import de.inren.frontend.common.panel.ExternalLinkWithMarkup;
import de.inren.frontend.health.HealthCalculator.BmiMeaning;

/**
 * 
 * Display the definition of the BMI values with links to wikipedia for more information.
 * 
 * @author Ingo Renner
 *
 */
public class BmiDefinitionPanel extends Panel {

    public BmiDefinitionPanel(String id) {
        super(id);
    }

    private IDataProvider<BmiMeaning> getDataProvider() {
        return new IDataProvider<BmiMeaning>() {

            @Override
            public void detach() {
            }

            @Override
            public Iterator<? extends BmiMeaning> iterator(long first, long count) {
                List<BmiMeaning> l = Arrays.asList(BmiMeaning.values())
                        .subList((int)first, (int)(first+count));
                Collections.reverse(l);
                return l.iterator();
            }

            @Override
            public long size() {
                return BmiMeaning.values().length;
            }

            @Override
            public IModel<BmiMeaning> model(BmiMeaning object) {
                return new Model<BmiMeaning>(object);
            }
            
        };
    }
    @Override
    protected void onInitialize() {
        super.onInitialize();
        
        add(new Label("category.label", new StringResourceModel("category.label", BmiDefinitionPanel.this, null)));
        add(new Label("range.label", new StringResourceModel("range.label", BmiDefinitionPanel.this, null)));
        add(new Label("info.label", new StringResourceModel("info.label", BmiDefinitionPanel.this, null)));
        
        final AttributeAppender center = new AttributeAppender("align", Model.of("center"));
        final AttributeAppender newTab = new AttributeAppender("target", Model.of("_blank"));
        final AttributeAppender border = new AttributeAppender("style", Model.of("border-top: 1px solid #aaa;"));
        
        add(new DataView<BmiMeaning>("rows", getDataProvider()) {

            @Override
            protected void populateItem(Item<BmiMeaning> item) {
                BmiMeaning bmi = item.getModelObject();
                item.add(new Label("color", "").add(new AttributeAppender("bgcolor", Model.of(bmi.getColor()))));
                
                item.add(new Label("category", new StringResourceModel(bmi.name()+".label", BmiDefinitionPanel.this, null)));
                
                if (bmi.getbMax()!=null) {
                    if (bmi.getbMin()!=0) {
                        item.add(new Label("range", String.valueOf(bmi.getbMin()) + "-" + String.valueOf(bmi.getbMax())).add(center));
                    }
                    else {
                        item.add(new Label("range", String.valueOf("≤ " + String.valueOf(bmi.getbMax()))).add(center));
                    }
                } else {
                    item.add(new Label("range", String.valueOf("≥ " + String.valueOf(bmi.getbMin()))).add(center));
                }
                
                // Add wiki links
                switch (bmi) {
                case Normal:
                    item.add(new ExternalLinkWithMarkup("info", 
                            new StringResourceModel(bmi.name()+".info.url", BmiDefinitionPanel.this, null),
                            new StringResourceModel(bmi.name()+".info.label", BmiDefinitionPanel.this, null),
                            newTab)
                        .add(center));
                    break;
                case Severely_Underweight:
                    item.add(new ExternalLinkWithMarkup("info",
                            new StringResourceModel(bmi.name()+".info.url", BmiDefinitionPanel.this, null),
                            new StringResourceModel(bmi.name()+".info.label", BmiDefinitionPanel.this, null),
                            newTab)
                        .add(center));
                    break;
                case Overweight:
                    item.add(new ExternalLinkWithMarkup("info", 
                            new StringResourceModel(bmi.name()+".info.url", BmiDefinitionPanel.this, null),
                            new StringResourceModel(bmi.name()+".info.label", BmiDefinitionPanel.this, null),
                            newTab)
                        .add(center));
                    break;
                case Obese_Class_II:
                    item.add(new ExternalLinkWithMarkup("info", 
                            new StringResourceModel(bmi.name()+".info.url", BmiDefinitionPanel.this, null),
                            new StringResourceModel(bmi.name()+".info.label", BmiDefinitionPanel.this, null),
                            newTab)
                        .add(center));
                    break;
                default:
                    item.add(new Label("info",""));
                }
                // Add borders to mark the groups
                switch (bmi) {
                case Normal:
                    item.add(border);
                    break;
                case Overweight:
                    item.add(border);
                    break;
                case Underweight:
                    item.add(border);
                    break;
                }
            }
        });
    }
}
