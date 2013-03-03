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
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import de.inren.frontend.application.ApplicationSettingsUtil;
import de.inren.frontend.common.panel.ABasePanel;

/**
 * Login the user an apply his settings.
 * 
 * @author Ingo Renner
 * 
 */
public class LoginPanel extends ABasePanel {

    public LoginPanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        final SignInForm signInForm = new SignInForm("form");

        signInForm.add(new AjaxLink<Void>("signup") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                // TODO Change to signup form
            }
        });

        signInForm.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (getB4WebSession().signIn(signInForm.getEmail(), signInForm.getPassword())) {
                    ApplicationSettingsUtil.applySettings(getB4WebSession().getUserSettings());
                    
                    continueToOriginalDestination();
                    setResponsePage(getApplication().getHomePage());
                } else {
                    form.error(new StringResourceModel("signInFailed", LoginPanel.this, null).getString());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                // target.add(getFeedbackMessages());
            }
        });

        add(signInForm);
    }

    private static final class SignInForm extends Form<Void> {
        private final ValueMap properties = new ValueMap();

        private TextField<String> email;
        private PasswordTextField password;

        public SignInForm(final String id) {
            super(id);
        }

        @Override
        protected void onBeforeRender() {
            if (!hasBeenRendered()) {
                initForm();
            }
            super.onBeforeRender();
        }

        private void initForm() {
            StringResourceModel lEmail = new StringResourceModel("email.label", SignInForm.this, null);
            add(new Label("email.label", lEmail));
            email = new RequiredTextField<String>("email", new PropertyModel<String>(properties, "email"), String.class);
            add(email.setLabel(lEmail));

            StringResourceModel lPass = new StringResourceModel("password.label", SignInForm.this, null);
            add(new Label("password.label", lPass));
            password = new PasswordTextField("password", new PropertyModel<String>(properties, "password"));
            add(password.setType(String.class).setLabel(lPass));
        }

        /**
         * Convenience method to access the password.
         * 
         * @return The password
         */
        public String getPassword() {
            return password.getInput();
        }

        /**
         * Convenience method to access the email.
         * 
         * @return The email
         */
        public String getEmail() {
            return email.getDefaultModelObjectAsString();
        }
    }
}
