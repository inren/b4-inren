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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.INavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar.ComponentPosition;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.settings.IBootstrapSettings;
import de.inren.frontend.auth.LoginPage;
import de.inren.frontend.auth.LogoutPage;
import de.inren.frontend.common.session.B4WebSession;
import de.inren.frontend.navigation.GNode;
import de.inren.frontend.navigation.NavList;
import de.inren.frontend.navigation.NavigationElement;
import de.inren.frontend.navigation.NavigationProvider;

/**
 * @author Ingo Renner
 *
 */
@Slf4j
public class TemplatePage<T> extends GenericWebPage<T> {

    public TemplatePage() {
        super();
    }

    public TemplatePage(IModel<T> model) {
        super(model);
    }
    
    public TemplatePage(PageParameters parameters) {
        super(parameters);
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!hasBeenRendered()) {
            commonInit();
        }
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        configureTheme(getPageParameters());
    }
    
    private void commonInit() {
        add(new HtmlTag("html"));

        add(new MetaTag("viewport", Model.of("viewport"), Model.of("width=device-width, initial-scale=1.0")));
        add(new ChromeFrameMetaTag("chrome-frame"));
        add(new MetaTag("description", Model.of("description"), Model.of("InRen")));
        add(new MetaTag("author", Model.of("author"), Model.of("Ingo Renner <renneringo@gmail.com>")));

        add(newNavbar("navbar"));
        add(new Footer("footer"));

        add(getLeftComponent("left"));
        add(getRightComponent("right"));
        
        add(new FeedbackPanel("feedbackPanel").setOutputMarkupId(true));

        add(new BootstrapBaseBehavior());
        add(new Code("code-internal"));

        add(new HeaderResponseContainer("footer-container", "footer-container"));

    }
    
    /**
     * creates a new {@link Navbar} instance
     *
     * @param markupId The components markup id.
     * @return a new {@link Navbar} instance
     */
    protected Navbar newNavbar(String markupId) {
    	Navbar navbar = new Navbar(markupId);

    	navbar.setPosition(Navbar.Position.STATIC_TOP);

    	// show brand name
    	navbar.brandName(Model.of("InRen"));

    	navbar.addComponents(NavigationProvider.get().getTopNavbarComponents(getActivePermissions()));
    	
    	// Theme selector on the right.
    	final List<INavbarComponent> components = new ArrayList<INavbarComponent>();
    	// components.add(new ImmutableNavbarComponent(new ThemesDropDown(), Navbar.ComponentPosition.RIGHT));
    	if (isSignedIn()) {
    	    components.add(new ImmutableNavbarComponent(
    	            new NavbarButton<LogoutPage>(LogoutPage.class, Model.of("Logout")).setIconType(IconType.globe), ComponentPosition.RIGHT)
    	    );
    	} else {
            components.add(new ImmutableNavbarComponent(
                    new NavbarButton<LoginPage>(LoginPage.class, Model.of("Login")).setIconType(IconType.globe), ComponentPosition.RIGHT)
            );
    	}
    	navbar.addComponents(components);
    	return navbar;
    }
    
    private boolean isSignedIn() {
        return ((B4WebSession) getSession()).isSignedIn();
    }
    
    private Collection<String> getActivePermissions() {
        return ((B4WebSession) getSession()).getRoles();
    }
    
    protected boolean hasNavigation() {
        return true;
    }

    /**
     * sets the theme for the current user.
     *
     * @param pageParameters current page parameters
     */
    private void configureTheme(PageParameters pageParameters) {
        StringValue theme = pageParameters.get("theme");

        if (!theme.isEmpty()) {
            IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
            settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
        }
    }
    
    protected Component getLeftComponent(String id) {
        log.debug("getLeftComponent for class: " + getClass());
        GNode<NavigationElement> menu = NavigationProvider.get().getSideNavbarComponents(getClass(), getActivePermissions());
        if (menu==null) {
            return new Label(id, "").setVisible(false);
        } else {
            return new NavList(id, menu);
        }
    }
    
    protected Component getRightComponent(String id) {
        return new Label(id, Model.of("Right"));
    }
}
