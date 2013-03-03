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
package de.inren.frontend.usersettings;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.service.UserService;
import org.bricket.b4.usersettings.entity.UserSettings;
import org.bricket.b4.usersettings.service.UserSettingsService;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.ITheme;
import de.inren.frontend.application.ApplicationSettingsUtil;
import de.inren.frontend.common.panel.ABasePanel;
import de.inren.frontend.common.panel.IAdminPanel;
import de.inren.frontend.common.session.B4WebSession;

/**
 * @author Ingo Renner
 *
 */
public class EditOrCreateUserSettingsPanel extends ABasePanel implements IAdminPanel {
    @SpringBean
    private UserService userService;

    @SpringBean
    private UserSettingsService userSettingsService;
    
    private UserSettings userSettings;

    private final User user;

    public EditOrCreateUserSettingsPanel(String id) {
        super(id);
        user = userService.loadUserByEmail(((B4WebSession) getSession()).getUser().getUsername());
        initSettings();
    }

    private void initSettings() {
        try {
            userSettings = userSettingsService.loadByUser(user.getId());
        } catch (B4ServiceException e) {
            // TODO Feedbackpannel Meldung
        }
    }

    
    private List<String> getThemes() {
        List<ITheme> themes = Bootstrap.getSettings(getApplication()).getThemeProvider().available();
        List<String> res = new ArrayList<String>();
        for (ITheme t : themes) {
            res.add(t.name());
        }
        return res;
    }
    
    @Override
    protected void initGui() {

        final Form<UserSettings> form = new Form<UserSettings>("form", new CompoundPropertyModel<UserSettings>(userSettings));
        
        StringResourceModel lName = new StringResourceModel("name.label", EditOrCreateUserSettingsPanel.this, null);
        form.add(new Label("name.label", lName));
        
        form.add(new DropDownChoice<String>("name", new PropertyModel<String>(userSettings, "theme"), getThemes()));

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
                    UserSettings u = userSettingsService.save((UserSettings) form.getModelObject());
                    
                    ApplicationSettingsUtil.applySettings(u);
                    
                    getFeedback().info(new StringResourceModel("feedback.success", EditOrCreateUserSettingsPanel.this, null).getString());
                } catch (B4ServiceException e) {
                    getFeedback().error(new StringResourceModel(e.getKey(), EditOrCreateUserSettingsPanel.this, null).getString());
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
