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
package de.inren.frontend.jqplot;

import java.util.List;


/**
 * @author Ingo Renner
 *
 */
public abstract class AJqplotDefinition implements IJqplotDefinition {

    private List<ChartEntry> entries;

    @Override
    public String getPlotData() {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        sb.append("[[");
        for ( ChartEntry e: getEntries()) {
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
        return sb.toString();
    }

    @Override
    public List<ChartEntry> getEntries() {
        return entries;
    }

    @Override
    public void setEntries(List<ChartEntry> entries) {
        this.entries = entries;
    }
}
