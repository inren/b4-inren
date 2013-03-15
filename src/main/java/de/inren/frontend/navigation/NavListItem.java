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
package de.inren.frontend.navigation;

import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.util.Components;

/**
 * @author Ingo Renner
 *
 */
public class NavListItem<T> extends BootstrapBookmarkablePageLink<T> {

    /**
     * Constructor.
     *
     * @param pageClass  The class of page to link to
     * @param parameters The parameters to pass to the new page when the link is clicked
     * @param label      The component's label
     * @param <T>        type of the page class
     */
    public <T extends Page> NavListItem(String id, final Class<T> pageClass, final PageParameters parameters, final IModel<String> label) {
        super(id, pageClass, parameters, Buttons.Type.Menu);

        setLabel(label);
    }

    /**
     * Constructor.
     *
     * @param pageClass The class of page to link to
     * @param label     The component's label
     * @param <T>       type of the page class
     */
    public <T extends Page> NavListItem(String id, final Class<T> pageClass, final IModel<String> label) {
        this(id, pageClass, new PageParameters(), label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NavListItem<T> setIconType(final IconType icon) {
        super.setIconType(icon);

        return this;
    }


    @Override
    protected void onComponentTag(final ComponentTag tag) {
        if (!Components.hasTagName(tag, "a", "button", "input")) {
            tag.setName("a");
        }

        super.onComponentTag(tag);
    }
}
