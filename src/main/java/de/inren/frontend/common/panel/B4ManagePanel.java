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

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author Ingo Renner
 * @author Henning Teek
 * 
 */
public abstract class B4ManagePanel extends ABasePanel {
    
    public B4ManagePanel(String id) {
        super(id);
    }

    public B4ManagePanel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void initGui() {
        add(getTable("table"));
        add(getActionPanel("create"));
    }

    protected abstract Component getTable(final String id);

    protected Component getActionPanel(final String id) {
        return new Label(id).setVisible(false);
    }
}
