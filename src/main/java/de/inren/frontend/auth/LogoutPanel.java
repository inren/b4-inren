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
package de.inren.frontend.auth;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
public class LogoutPanel extends ABasePanel {

    public LogoutPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        Form<Void> form = new Form<Void>("form");
        form.add(new AjaxButton("cancel") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                setResponsePage(getApplication().getHomePage());
            }
        });
        form.add(new AjaxButton("logout") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                getB4WebSession().invalidate();
                setResponsePage(getApplication().getHomePage());
            }
        });
        add(form);
    }
}
