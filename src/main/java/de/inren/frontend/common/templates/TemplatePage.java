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

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import de.agilecoders.wicket.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.settings.IBootstrapSettings;
import de.inren.frontend.application.HomePage;
import de.inren.frontend.health.HealthWorktopPage;
import de.inren.frontend.user.ManageUsersPage;

/**
 * @author Ingo Renner
 *
 */
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
        //add(newNavigation("navigation"));
        add(new Footer("footer"));
        
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
    	navbar.brandName(Model.of("InRen Tmpl"));

    	navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
    			new NavbarButton<HomePage>(HomePage.class, Model.of("Overview")).setIconType(IconType.home),
    			new NavbarButton<HealthWorktopPage>(HealthWorktopPage.class, Model.of("Health")).setIconType(IconType.heart),
                        new NavbarButton<HealthWorktopPage>(ManageUsersPage.class, Model.of("Users")).setIconType(IconType.user)
    	            )
		);
    	// Theme selector on the right.
    	navbar.addComponents(new ImmutableNavbarComponent(new ThemesDropDown(), Navbar.ComponentPosition.RIGHT));
    	return navbar;
    }
    
    protected boolean hasNavigation() {
        return true;
    }

    /**
     * creates a new navigation component.
     *
     * @param markupId The component's markup id
     * @return a new navigation component.
     */
    private Component newNavigation(String markupId) {
        WebMarkupContainer navigation = new WebMarkupContainer(markupId);
        //navigation.add(new AffixBehavior("200"));
        navigation.setVisible(hasNavigation());
        return navigation;
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
    
}