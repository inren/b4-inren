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
package de.inren.frontend.common.table;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Ingo Renner
 * @author Henning Teek
 * 
 * @param <T>
 */
public class ListColumn<T extends List<?>> extends AbstractColumn<T, Object> {
    private final String propertyExpression;
    private final String itemExpression;

    public ListColumn(IModel<String> displayModel, String listProperty, String itemProperty) {
        super(displayModel, null);
        this.propertyExpression = listProperty;
        this.itemExpression = itemProperty;
    }

    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new ListPanel(componentId, new PropertyModel(rowModel, propertyExpression), itemExpression));
    }

    /**
     * @return wicket property expression
     */
    public String getPropertyExpression() {
        return propertyExpression;
    }
}
