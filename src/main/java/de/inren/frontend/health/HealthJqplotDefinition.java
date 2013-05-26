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
                    .append("show: true")
                .append("}")
            .append(",")
                .append("series:[{lineWidth:1, markerOptions:{style:'square'}}]")
            .append(",")
                .append("cursor:{show: true, zoom: true }")
            .append("}")
            .toString(); 
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
