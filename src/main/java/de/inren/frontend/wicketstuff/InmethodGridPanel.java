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
package de.inren.frontend.wicketstuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;

import com.inmethod.grid.DataProviderAdapter;
import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.column.PropertyColumn;
import com.inmethod.grid.datagrid.DataGrid;
import com.inmethod.grid.datagrid.DefaultDataGrid;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
public class InmethodGridPanel extends ABasePanel {
    final ListDataProvider<IG_Person> listDataProvider = new ListDataProvider<IG_Person>(createPersonList());

    public InmethodGridPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {

        List<IGridColumn> cols = (List) Arrays.asList(
            new PropertyColumn(new Model<String>("First Name"), "firstName"),
            new PropertyColumn(new Model<String>("Last Name"), "lastName"));
        DataGrid grid = new DefaultDataGrid("grid", new DataProviderAdapter(listDataProvider), cols);
        add(grid);
    }

    private List<IG_Person> createPersonList() {
        List<IG_Person> res = new ArrayList<IG_Person>();
        res.add(new IG_Person("Sonnen", "Blume"));
        res.add(new IG_Person("Donal", "Duck"));
        res.add(new IG_Person("Micky", "Mouse"));
        return res ;
    }
    
}
