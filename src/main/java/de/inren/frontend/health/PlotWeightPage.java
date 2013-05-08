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

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.wicketstuff.jqplot.JqPlotChart;

import br.com.digilabs.jqplot.chart.LineChart;
import de.inren.frontend.common.templates.SecuredPage;

/**
 * @author Ingo Renner
 *
 */
public class PlotWeightPage extends SecuredPage<String> {
    @SpringBean
    private MeasurementRepository measurementRepository;

    public PlotWeightPage() {
        super();
    }

    @Override
    public Component createPanel(String wicketId) {
        List<Measurement> measurements = measurementRepository.findByUid(getUid());
        LineChart<Double> lineChart = new LineChart<Double>();
        for (Measurement m : measurements) {
            lineChart.addValue(m.getWeight());
        }
        lineChart.setLabelX("Date");
        lineChart.setLabelY("Kg");
        return new JqPlotChart(wicketId, lineChart);
    }
}
