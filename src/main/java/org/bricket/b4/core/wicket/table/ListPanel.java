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
package org.bricket.b4.core.wicket.table;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public class ListPanel extends Panel {
    public ListPanel(String id, PropertyModel<List<?>> listModel, final String itemExpression) {
        super(id, listModel);
        List<?> list = (List<?>) listModel.getObject();
        add(new ListPanelView("itemlist", list, itemExpression));
    }

    private static final class ListPanelView extends ListView<Object> {
        private final String itemExpression;

        private ListPanelView(String id, List<? extends Object> list, String itemExpression) {
            super(id, list);
            this.itemExpression = itemExpression;
        }

        @Override
        protected void populateItem(ListItem<Object> item) {
            item.add(new Label("item", new PropertyModel<Object>(item.getModelObject(), itemExpression)));
        }
    }
}
