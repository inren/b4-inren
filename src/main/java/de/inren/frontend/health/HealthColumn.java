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

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.CssResourceReference;

import de.agilecoders.wicket.markup.html.bootstrap.behavior.CssClassNameModifier;

/**
 * @author Ingo Renner
 *
 */
@Slf4j
public class HealthColumn<Measurement> extends PropertyColumn<Measurement, String> {

    public HealthColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Measurement>> item, String componentId, IModel<Measurement> rowModel) {
        log.debug("adding item");
        item.add(new MeasurementTabEntryPanel<Measurement>(componentId, rowModel, getDisplayModel()));
        PropertyModel<Double> pm = new PropertyModel<Double>(rowModel, getDisplayModel().getObject() + "Delta");
        item.add(getHealthBehavior());
        item.add(getCss(pm.getObject()));
    }

    protected CssClassNameModifier getCss(double d) {
        final String css;
        if (d==0) {
            css = "health-same";
        } else {
            css = d<0 ? "health-less" :  "health-more";
        }
        return new CssClassNameModifier(css);
    }
    
    private Behavior getHealthBehavior() {
        return new Behavior() {

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(CssHeaderItem.forReference(new CssResourceReference(HealthColumn.class, "HealthColumn.css")));
            }

        };
    }
    
}
