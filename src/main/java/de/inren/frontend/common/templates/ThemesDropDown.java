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
package de.inren.frontend.common.templates;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.button.DropDownAutoOpen;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.settings.IBootstrapSettings;
import de.agilecoders.wicket.settings.ITheme;

/**
 * @author Ingo Renner
 *
 */
public class ThemesDropDown extends NavbarDropDownButton {

    public ThemesDropDown() {
        super(Model.of("Themes"));
    }

    @Override
    public boolean isActive(Component item) {
        return false;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!hasBeenRendered()) {
            initGui();
        }
    }

    private void initGui() {
        add(new DropDownAutoOpen());
        addButton(new MenuHeader(Model.of("all available themes:")));
        addButton(new MenuDivider()).setIconType(IconType.book);
        IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
        List<ITheme> themes = settings.getThemeProvider().available();

        for (ITheme theme : themes) {
            PageParameters params = new PageParameters();
            params.set("theme", theme.name());
            addButton(new MenuBookmarkablePageLink<Page>(getPage().getPageClass(), params, Model.of(theme.name())));
        }
        
    }

    
}
