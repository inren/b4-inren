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
package org.bricket.b4.core.wicket.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public abstract class AActionLink extends Panel {
    
    private final StringResourceModel label;
    private final StringResourceModel message;

    public AActionLink(String id) {
        this(id, null, null);
    }

    public AActionLink(String id, StringResourceModel message) {
        super(id);
        this.message = message;
        this.label = null;
    }
    
    public AActionLink(String id, StringResourceModel message, StringResourceModel label) {
        super(id);
        this.message = message;
        this.label = label;
    }
    
    public StringResourceModel getMessage() {
        return message;
    }

    public StringResourceModel getLabel() {
        return label;
    }
}
