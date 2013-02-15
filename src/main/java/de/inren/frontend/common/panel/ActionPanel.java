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
package de.inren.frontend.common.panel;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public class ActionPanel extends Panel {
    
    public ActionPanel(String id, List<AActionLink> links) {
        super(id);
        add(new ActionListView("listview", links));
    }

    private static final class ActionListView extends ListView<AActionLink> {
        
        private ActionListView(String id, List<? extends AActionLink> list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem<AActionLink> item) {
            AActionLink link = item.getModelObject();
            item.add(link.setRenderBodyOnly(true));
        }
    }
}
