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

import de.inren.frontend.jqplot.IJqplotDefinition;

/**
 * @author Ingo Renner
 *
 */
public class HealthJqplotDefinition implements IJqplotDefinition {

    private class Entry {
        private final String x;
        private final String y;
        
        public Entry(String x, String y) {
            this.x = x;
            this.y = y;
        }
        
        public String getX() {
            return x;
        }
        
        public String getY() {
            return y;
        }        
    }
    
    private String fieldname;
    
    private List<Entry> entries;
    
    public HealthJqplotDefinition(MeasurementRepository measurementRepository, String fieldname, long uid) {
        this.fieldname = fieldname;
        
        entries = new ArrayList<Entry>();
        List<Measurement> d = measurementRepository.findByUid(uid);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Measurement m : d) {
            IModel<Measurement> pm = new PropertyModel<Measurement>(m, fieldname);
            entries.add(new Entry(sd.format(m.getDate()), String.valueOf(pm.getObject())));
        }
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
    public String getPlotData() {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        sb.append("[[");
        for ( Entry e: entries) {
            sb.append(sep)
            .append("[")
            .append("'")
            .append(e.getX())
            .append("'")
            .append(",")
            .append(e.getY())
            .append("]");
            sep=",";
        }
        sb.append("]]");
        return sb.toString(); // "[[[1, 2],[3,5.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]]";
    }

    @Override
    public List<String> getAdditionalResources() {
        String plugin1= "jquery.jqplot/plugins/jqplot.dateAxisRenderer.min.js";
        String plugin2= "jquery.jqplot/plugins/jqplot.canvasOverlay.min.js";
        return Arrays.asList(plugin1, plugin2);
    }
}
