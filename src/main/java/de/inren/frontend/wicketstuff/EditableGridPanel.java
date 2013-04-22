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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.egrid.EditableGrid;
import org.wicketstuff.egrid.column.AbstractEditablePropertyColumn;
import org.wicketstuff.egrid.column.EditableCellPanel;
import org.wicketstuff.egrid.column.EditableRequiredDropDownCellPanel;
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn;
import org.wicketstuff.egrid.provider.EditableListDataProvider;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 * 
 */
public class EditableGridPanel extends ABasePanel {

    public EditableGridPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        add(new EditableGrid<EG_Person, String>("grid", getColumns(), new EditableListDataProvider<EG_Person, String>(getPersons()), 5, EG_Person.class) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(getFeedback());
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                target.add(getFeedback());
            }

            @Override
            protected void onDelete(AjaxRequestTarget target, IModel<EG_Person> rowModel) {
                target.add(getFeedback());
            }

            @Override
            protected void onSave(AjaxRequestTarget target, IModel<EG_Person> rowModel) {
                target.add(getFeedback());
            }
        }
        );
    }

    private List<EG_Person> getPersons() {
        List<EG_Person> res = new ArrayList<EG_Person>();
        res.add(new EG_Person("Alpha", "a", "1"));
        res.add(new EG_Person("Bravo", "b", "2"));
        res.add(new EG_Person("Charly", "c", "3"));
        res.add(new EG_Person("Delta", "d", "4"));
        res.add(new EG_Person("Echo", "e", "5"));
        res.add(new EG_Person("Foxtrott", "f", "6"));
        return res;
    }

    private List<AbstractEditablePropertyColumn<EG_Person, String>> getColumns() {
        List<AbstractEditablePropertyColumn<EG_Person, String>> columns = new ArrayList<AbstractEditablePropertyColumn<EG_Person, String>>();
        columns.add(new RequiredEditableTextFieldColumn<EG_Person, String>(new Model<String>("Name"), "name"));
        columns.add(new RequiredEditableTextFieldColumn<EG_Person, String>(new Model<String>("Address"), "address"));
        columns.add(new AbstractEditablePropertyColumn<EG_Person, String>(new Model<String>("Age"), "age") {

            private static final long serialVersionUID = 1L;

            public EditableCellPanel getEditableCellPanel(String componentId) {
                return new EditableRequiredDropDownCellPanel<EG_Person, String>(componentId, this, Arrays.asList("10", "11", "12", "13", "14", "15"));
            }


        });
        return columns;
    }
}
