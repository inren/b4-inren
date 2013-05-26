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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.bricket.b4.health.entity.HealthSettings;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;

import de.inren.frontend.health.HealthCalculator.BmiMeaning;
import de.inren.frontend.jqplot.AJqplotDefinition;
import de.inren.frontend.jqplot.ChartEntry;

/**
 * @author Ingo Renner
 *
 */
@Slf4j
public class BmiJqplotDefinition extends AJqplotDefinition {


    private HealthSettings healthSettings;
    
    public BmiJqplotDefinition(MeasurementRepository measurementRepository, HealthSettings s, String fieldname, long uid) {
        this.healthSettings = s;
        
        ArrayList<ChartEntry> data = new ArrayList<ChartEntry>();
        List<Measurement> d = measurementRepository.findByUid(uid);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Measurement m : d) {
            IModel<Double> pm = new PropertyModel<Double>(m, fieldname);
            Double mass = pm.getObject();
            final String x = sd.format(m.getDate());
            final String y = String.valueOf(HealthCalculator.calculateBmi(healthSettings.getHeight(), mass));
            data.add(new ChartEntry(x, y));
        }
        setEntries(data);
    }

    @Override
    public String getPlotConfiguration() {
        return new StringBuffer()
            .append("{")
                .append("title:'").append("BMI").append("'")        
            .append(",")
                .append("axes:{")
                        .append("xaxis:{")
                            .append("renderer:$.jqplot.DateAxisRenderer")
                            .append(",")
                            .append("tickRenderer: $.jqplot.CanvasAxisTickRenderer")
                            .append(",")
                            .append("tickOptions: {angle: -30} ")
                        .append("}")
                .append("}")
            .append(",")
                .append("canvasOverlay: {")
                    .append("show: true").append(",")
                    .append("objects: [")
                        .append(createCategorieLines())
                    .append("]")
                .append("}")
            .append(",")
                .append("series:[{lineWidth:1, markerOptions:{style:'square'}}]")
            .append(",")
                .append("cursor:{show: true, zoom: true }")
            .append("}")
            .toString(); 
    }

    private String createCategorieLines() {
        StringBuffer sb = new StringBuffer();
        int l = 0;
        for (BmiMeaning m : BmiMeaning.values()) {
            l++;
            sb.append("{horizontalLine: {name: '")
                .append(m.name())
                .append("', y: ")
                .append(m.getbMax())
                .append(", lineWidth: 1, color: '")
                .append(m.getColor())
                .append("', shadow: false}}");
            if (l < BmiMeaning.values().length) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public List<String> getAdditionalResources() {
        return Arrays.asList("jquery.jqplot/plugins/jqplot.dateAxisRenderer.min.js",
                "jquery.jqplot/plugins/jqplot.canvasOverlay.min.js",
                "jquery.jqplot/plugins/jqplot.cursor.min.js",
                "jquery.jqplot/plugins/jqplot.canvasTextRenderer.min.js",
                "jquery.jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"
                );
    }
}
