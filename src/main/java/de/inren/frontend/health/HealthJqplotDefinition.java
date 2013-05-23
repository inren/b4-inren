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

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;

import de.inren.frontend.jqplot.AJqplotDefinition;
import de.inren.frontend.jqplot.ChartEntry;

/**
 * @author Ingo Renner
 *
 */
public class HealthJqplotDefinition extends AJqplotDefinition {
    
    private String fieldname;
    
    public HealthJqplotDefinition(MeasurementRepository measurementRepository, String fieldname, long uid) {
        this.fieldname = fieldname;
        
        ArrayList<ChartEntry> data = new ArrayList<ChartEntry>();
        List<Measurement> d = measurementRepository.findByUid(uid);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Measurement m : d) {
            IModel<Measurement> pm = new PropertyModel<Measurement>(m, fieldname);
            data.add(new ChartEntry(sd.format(m.getDate()), String.valueOf(pm.getObject())));
        }
        setEntries(data);
    }

    @Override
    public String getPlotConfiguration() {
        return new StringBuffer()
            .append("{")
            .append("title:'").append(fieldname).append("'")        
            .append(",")
            .append("axes:{xaxis:{renderer:$.jqplot.DateAxisRenderer}}")
            .append(",")
            .append("canvasOverlay: {")
                .append("show: true").append(",")
                .append("objects: [")
                    .append("{horizontalLine: {name: 'lower', y: 95, lineWidth: 1, color: 'rgb(255,193,37)', shadow: false}}")
                    .append(",")
                    .append("{horizontalLine: {name: 'normal', y: 100, lineWidth: 1, color: 'rgb(34,139,34)', shadow: true}}")
                    .append(",")
                    .append("{horizontalLine: {name: 'upper', y: 120, lineWidth: 1, color: 'rgb(205,55,0)', shadow: false}}")
                .append("]")
            .append("}")
            .append("}")
            .toString(); 
    }


    @Override
    public List<String> getAdditionalResources() {
        String plugin1= "jquery.jqplot/plugins/jqplot.dateAxisRenderer.min.js";
        String plugin2= "jquery.jqplot/plugins/jqplot.canvasOverlay.min.js";
        return Arrays.asList(plugin1, plugin2);
    }

}
