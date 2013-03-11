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

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.health.entity.HealthSettings;
import org.bricket.b4.health.service.HealthSettingsService;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.service.UserService;
import org.joda.time.DateTime;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import de.inren.frontend.common.panel.ABasePanel;
import de.inren.frontend.common.panel.IAdminPanel;
import de.inren.frontend.common.session.B4WebSession;

/**
 * @author Ingo Renner
 *
 */
public class EditOrCreateHealthSettingsPanel extends ABasePanel implements IAdminPanel {
    @SpringBean
    private UserService userService;

    @SpringBean
    private HealthSettingsService healthSettingsService;
    
    private HealthSettings healthSettings;

    private final User user;

    public EditOrCreateHealthSettingsPanel(String id) {
        super(id);
        user = userService.loadUserByEmail(((B4WebSession) getSession()).getUser().getUsername());
        initSettings();
    }

    private void initSettings() {
        try {
            healthSettings = healthSettingsService.loadByUser(user.getId());
            if (healthSettings==null) {
                healthSettings = new HealthSettings();
                healthSettings.setUid(user.getId());
            }
        } catch (B4ServiceException e) {
            // TODO Feedbackpannel Meldung
        }
    }

    @Override
    protected void initGui() {

        final Form<HealthSettings> form = new Form<HealthSettings>("form", new CompoundPropertyModel<HealthSettings>(healthSettings));
        
        StringResourceModel lBirthday = new StringResourceModel("birthday.label", EditOrCreateHealthSettingsPanel.this, null);
        form.add(new Label("birthday.label", lBirthday));
        form.add(new DateTextField("birthday",
                new DateTextFieldConfig()
                .autoClose(true)
                .withView(DateTextFieldConfig.View.Decade)
                .showTodayButton(true)
                .withFormat("dd.MM.yyyy")
                .withStartDate(new DateTime().withYear(1900))).setRequired(true).setLabel(lBirthday));
        
        // form.add(new TextField<Date>("birthday", Date.class).setRequired(true).setLabel(lBirthday));

        StringResourceModel lHeight = new StringResourceModel("height.label", EditOrCreateHealthSettingsPanel.this, null);
        form.add(new Label("height.label", lHeight));
        form.add(new TextField<String>("height", String.class).setRequired(true).setLabel(lHeight));

        StringResourceModel lGender = new StringResourceModel("gender.label", EditOrCreateHealthSettingsPanel.this, null);
        form.add(new Label("gender.label", lGender));

        form.add(new RadioChoice<Boolean>("male", Arrays.asList(new Boolean[] { Boolean.TRUE, Boolean.FALSE }), new IChoiceRenderer<Boolean>() {

            @Override
            public Object getDisplayValue(Boolean object) {
                return object ? new StringResourceModel("male.label", EditOrCreateHealthSettingsPanel.this, null).getString() : new StringResourceModel("female.label",
                        EditOrCreateHealthSettingsPanel.this, null).getString();
            }

            @Override
            public String getIdValue(Boolean object, int index) {
                return String.valueOf(object);
            }
        }).setRequired(true).setLabel(lGender));
        
        form.add(new AjaxLink<Void>("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getSession().getFeedbackMessages().clear();
                initSettings();
                target.add(form);
            }
        });

        form.add(new Button("submit") {
            
            @Override
            public void onSubmit() {
                try {
                    healthSettingsService.save((HealthSettings) form.getModelObject());
                    getFeedback().info(new StringResourceModel("feedback.success", EditOrCreateHealthSettingsPanel.this, null).getString());
                } catch (B4ServiceException e) {
                    getFeedback().error(new StringResourceModel(e.getKey(), EditOrCreateHealthSettingsPanel.this, null).getString());
                }
            }

            @Override
            public void onError() {
                FeedbackPanel f = getFeedback();
                f.info("Error");
            }
        });

        add(form);
    }

}
