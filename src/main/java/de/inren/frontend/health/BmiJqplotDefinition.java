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
import org.bricket.b4.health.entity.HealthSettings;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;

import de.inren.frontend.jqplot.IJqplotDefinition;

/**
 * @author Ingo Renner
 *
 */
public class BmiJqplotDefinition implements IJqplotDefinition {

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
    
    private MeasurementRepository measurementRepository;
    
    private String fieldname;
    
    private long uid;
    
    private List<Entry> entries;
    
    private HealthSettings healthSettings;
    
    public BmiJqplotDefinition(MeasurementRepository measurementRepository, HealthSettings s, String fieldname, long uid) {
        this.measurementRepository = measurementRepository;
        this.healthSettings = s;
        this.fieldname = fieldname;
        this.uid = uid;
        
        entries = new ArrayList<Entry>();
        List<Measurement> d = measurementRepository.findByUid(uid);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Measurement m : d) {
            IModel<Double> pm = new PropertyModel<Double>(m, fieldname);
            Double mass = pm.getObject();
            entries.add(new Entry(sd.format(m.getDate()), String.valueOf(HealthCalculator.calculateBmi(healthSettings.getHeight(), mass))));
        }
    }

    @Override
    public String getPlotConfiguration() {
        
        String c = "{ title:'"+ fieldname + "', " +
        		"axes:{xaxis:{renderer:$.jqplot.DateAxisRenderer}}," +
        		" series:[{lineWidth:4, markerOptions:{style:'square'}}]}";
        // "{ title:'Exponential Line', axes:{yaxis:{min:-10, max:240}}, series:[{color:'#5FAB78'}]}";
        return c; 
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
        String plugin = "jquery.jqplot.1.0.7/plugins/jqplot.dateAxisRenderer.min.js";
        return Arrays.asList(plugin);
    }

}
