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

import org.wicketstuff.jqplot.JqPlotChart;

import br.com.digilabs.jqplot.chart.LineChart;
import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
public class JqPlotPanel extends ABasePanel {

    public JqPlotPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        LineChart<Integer> lineChart = new LineChart<Integer>();
        lineChart.addValues(1,2,3,4,5);
        add(new JqPlotChart("chart1", lineChart));
    }

}
