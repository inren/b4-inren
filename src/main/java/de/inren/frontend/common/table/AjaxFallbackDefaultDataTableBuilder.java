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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import de.inren.frontend.common.dataprovider.RepositoryDataProvider;

/**
 * Sammlung von diversen Spaltentpen um auf einfache Weise eine Tabelle zu
 * erstellen. Die Tabellenspalten Überschriften werden als propertyname.label in
 * den Sprachdateien gesucht.
 * 
 * @author Ingo Renner
 * @author Henning Teek
 */
public final class AjaxFallbackDefaultDataTableBuilder<T extends Serializable> implements Serializable {

    private static final String LABEL = ".label";

    private final List<ICellPopulator<?>> columns;
    private final Component component;
    private ISortableDataProvider<T, String> dataProvider;
    private int numberOfRows = 10;

    public AjaxFallbackDefaultDataTableBuilder(Component component) {
        this.component = component;
        columns = new ArrayList<ICellPopulator<?>>();
    }

    /**
     * Fügt eine individuelle Spalte ein
     * 
     * @param abstractColumn
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder add(AbstractColumn<Object, Object> abstractColumn) {
        columns.add(abstractColumn);
        return this;
    }

    /**
     * Fügt eine normale Spalte ein. Spaltenueberschrift wird ein
     * StringRescourceModel mit Key = property.label Inhalt ist ein
     * PropertyModel mit dem property
     * 
     * @param property
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addPropertyColumn(String property) {
        return addPropertyColumn(property, false);
    }

    /**
     * Fügt eine normale Spalte ein. Spaltenueberschrift wird ein
     * StringRescourceModel mit Key = property.label Inhalt ist ein
     * PropertyModel mit dem property, bei sortable true, kann nach dem Property
     * sortiert werden.
     * 
     * @param property
     * @param sortable
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addPropertyColumn(String property, boolean sortable) {
        columns.add(createPropertyColumn(property, sortable));
        return this;
    }

    /**
     * Die Datenquelle für die tabelle.
     * 
     * @param repositoryDataProvider
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addDataProvider(RepositoryDataProvider<T> repositoryDataProvider) {
        this.dataProvider = repositoryDataProvider;
        return this;
    }

    /**
     * Diese Property muß eine Liste referenzieren, diese wird als ui List
     * gerendert.
     * 
     * @param listProperty
     * @param itemProperty
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addListProperty(String listProperty, String itemProperty) {
        ICellPopulator<?> listColumn = new ListColumn<List<?>>(new StringResourceModel(listProperty + LABEL, component,
                null), listProperty, itemProperty);
        columns.add(listColumn);
        return this;
    }

    /**
     * Zeigt einen * an, wenn der Propertywert true ist.
     * 
     * @param property
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addBooleanPropertyColumn(String property) {
        return addBooleanPropertyColumn(property, false);
    }

    /**
     * Zeigt einen * an, wenn der Propertywert true ist.
     * 
     * @param property
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder addBooleanPropertyColumn(String property, boolean sortable) {
        columns.add(createBooleanPropertyColumn(property, sortable));
        return this;
    }

    /**
     * Wieviele Zeilen sollen dargestellt werden.
     * 
     * @param numberOfRows
     * @return this for chaining
     */
    public AjaxFallbackDefaultDataTableBuilder setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Component build(String id) {
        return new AjaxFallbackDefaultDataTable(id, columns, dataProvider, numberOfRows).setOutputMarkupId(true);
    }

    private ICellPopulator<?> createPropertyColumn(String property, boolean sortable) {
        if (sortable) {
            return new PropertyColumn<Object, Object>(new StringResourceModel(property + LABEL, component, null), property,
                    property);
        } else {
            return new PropertyColumn<Object, Object>(new StringResourceModel(property + LABEL, component, null), property);
        }
    }

    private ICellPopulator<?> createBooleanPropertyColumn(final String property, boolean sortable) {
        if (sortable) {
            return new BooleanPropertyColumn<Object>(new StringResourceModel(property + LABEL, component, null),
                    property, property, property);
        } else {
            return new BooleanPropertyColumn<Object>(new StringResourceModel(property + LABEL, component, null),
                    property, property);
        }
    }

    private static final class BooleanPropertyColumn<T> extends PropertyColumn<T, Object> {
        private final String property;

        private BooleanPropertyColumn(IModel<String> displayModel, String propertyExpression, String property) {
            super(displayModel, propertyExpression);
            this.property = property;
        }

        private BooleanPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression,
                String property) {
            super(displayModel, sortProperty, propertyExpression);
            this.property = property;
        }

        @Override
        public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
            PropertyModel<Boolean> model = new PropertyModel<Boolean>(rowModel, property);
            Boolean bool = model.getObject();
            // We tread null as false
            if (bool==null) {
                bool = Boolean.FALSE;
            }
            item.add(bool ? new Label(componentId, "<span class=\"ui-icon ui-icon-check\"></span>")
                    .setEscapeModelStrings(false) : new Label(componentId, "<span></span>")
                    .setEscapeModelStrings(false));
        }
    }
}
