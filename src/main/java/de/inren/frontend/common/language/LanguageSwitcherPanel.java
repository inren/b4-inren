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
package de.inren.frontend.common.language;

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
public class LanguageSwitcherPanel extends ABasePanel {
    private final ResourceReference IMG_UK = new PackageResourceReference(LanguageSwitcherPanel.class, "flags/uk.gif");
    private final ResourceReference IMG_DE = new PackageResourceReference(LanguageSwitcherPanel.class, "flags/germany.gif");
    
    public LanguageSwitcherPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        final Locale curLocale = getSession().getLocale();
        if ( !(Locale.ENGLISH.equals(curLocale)) && !(Locale.GERMAN.equals(curLocale)) ){
            getSession().setLocale(Locale.GERMAN ); //Default festlegen TODO besser aus Browser request.
        }
        if(Locale.ENGLISH.equals(getSession().getLocale())){
            add(getSwitchToDeRef());
        } else {
            add(getSwitchToEnRef());
        }
        }

    private Link<String> getSwitchToEnRef() {
        final Link<String> link = new Link<String>("lngref") { 
            @Override
                        public void onClick()
            {
                getSession().setLocale(Locale.ENGLISH );
                LanguageSwitcherPanel.this.replace(getSwitchToDeRef());
            }
        };
        link.add(new Image("lngflag", IMG_UK));
        link.add(new Label("lnglabel", new Model<String>("English")));
        return link;
    }

    private Link<String> getSwitchToDeRef() {
        final Link<String> link = new Link<String>("lngref") { 
            @Override
                        public void onClick()
            {
                getSession().setLocale(Locale.GERMAN );
                LanguageSwitcherPanel.this.replace(getSwitchToEnRef());
            }
        };
        link.add(new Image("lngflag", IMG_DE));
        link.add(new Label("lnglabel", new Model<String>("Deutsch")));
        return link;
    }
}
