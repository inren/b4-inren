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

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.wicketstuff.annotation.mount.MountPath;

import de.inren.frontend.common.templates.SecuredPage;
import de.inren.frontend.jqplot.IJqplotDefinition;
import de.inren.frontend.jqplot.JqplotPanel;

/**
 * @author Ingo Renner
 *
 */
@MountPath(value = "/plotBmi")
public class PlotBmiPage extends SecuredPage<IJqplotDefinition> {
    @SpringBean
    private MeasurementRepository measurementRepository;

    @Override
    public Component createPanel(String wicketId) {
        return new JqplotPanel(wicketId, createJqplotModel());
    }

    final IModel<IJqplotDefinition> createJqplotModel() {
        return new Model<IJqplotDefinition>(new HealthJqplotDefinition(measurementRepository, "weight", getUid()));
    }
}
