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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Ingo Renner
 *
 */
public class MeasurementTabEntryPanel<Measurement> extends Panel {

    private final String property;

    public MeasurementTabEntryPanel(String id, IModel<Measurement> model, IModel<String> displayModel) {
        super(id, model);
        this.property = displayModel.getObject();
        add(new Label("value", new PropertyModel<Long>(model.getObject(), property)));
        add(new Label("delta", new PropertyModel<Long>(model.getObject(), property + "Delta")));
    }

}
