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

import java.util.Collections;
import java.util.List;

import de.inren.frontend.jqplot.IJqplotDefinition;

/**
 * @author Ingo Renner
 *
 */
public class HealthJqplotDefinition implements IJqplotDefinition {

    @Override
    public String getPlotConfiguration() {
        return "{ title:'Exponential Line', axes:{yaxis:{min:-10, max:240}}, series:[{color:'#5FAB78'}]}";
    }

    @Override
    public String getPlotData() {
        return "[[[1, 2],[3,5.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]]";
    }

    @Override
    public List<String> getAdditionalResources() {
        return Collections.emptyList();
    }

}
