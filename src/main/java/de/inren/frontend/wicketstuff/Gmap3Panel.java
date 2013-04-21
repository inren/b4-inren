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

import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
public class Gmap3Panel extends ABasePanel {

    public Gmap3Panel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        GMap map = new GMap("map");
        map.setStreetViewControlEnabled(false);
        map.setScaleControlEnabled(true);
        map.setScrollWheelZoomEnabled(true);
        map.setCenter(new GLatLng(52.47649, 13.228573));
        add(map);
    }

}
