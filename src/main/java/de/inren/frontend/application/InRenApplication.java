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
package de.inren.frontend.application;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.jqueryui.JQueryUIJavaScriptReference;
import de.agilecoders.wicket.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.markup.html.themes.metro.MetroTheme;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import de.inren.frontend.auth.B4AuthorizationStrategy;
import de.inren.frontend.common.session.B4WebSession;

/**
 * @author Ingo Renner
 * 
 */
public class InRenApplication extends WebApplication implements IB4Application {

    public InRenApplication() {
    }

    public static InRenApplication get() {
        return (InRenApplication) WebApplication.get();
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        super.init();

        /* Spring injection */
        getComponentInstantiationListeners().add(
                new SpringComponentInjector(this));

        /* wickets own security toolkit B4 style */
        getSecuritySettings().setAuthorizationStrategy(new B4AuthorizationStrategy());

        /* Bootstrap */
        configureBootstrap();

        new AnnotatedMountScanner().scanPackage("org.bricket.b4.*.wicket").mount(this);

        Localizer localizer = new Localizer() {

            @Override
            public String getString(String key, Component component, IModel<?> model, Locale locale, String style, String defaultValue)
                    throws MissingResourceException {
                try {
                    return super.getString(key, component, model, locale, style, defaultValue);
                } catch (MissingResourceException e) {
                    return key + (locale==null? "": locale.getLanguage());
                }
            }

        };

        this.getResourceSettings().setLocalizer(localizer);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new B4WebSession(request);
    }

    private void configureBootstrap() {
        BootstrapSettings settings = new BootstrapSettings();
        settings.minify(true)
        // use minimized version of all bootstrap references
        .useJqueryPP(true).useModernizr(true).useResponsiveCss(true)
        .setJsResourceFilterName("footer-container")
        .getBootstrapLessCompilerSettings().setUseLessCompiler(false);

        ThemeProvider themeProvider = new BootswatchThemeProvider() {
            {
                add(new MetroTheme());
                defaultTheme("cyborg");
            }
        };
        settings.setThemeProvider(themeProvider);

        Bootstrap.install(this, settings);
        configureResourceBundles();
    }

    /**
     * configure all resource bundles (css and js)
     */
    private void configureResourceBundles() {
        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());

        getResourceBundles()
        .addJavaScriptBundle(
                InRenApplication.class,
                "core.js",
                (JavaScriptResourceReference) getJavaScriptLibrarySettings()
                .getJQueryReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings()
                .getWicketEventReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings()
                .getWicketAjaxReference(),
                (JavaScriptResourceReference) ModernizrJavaScriptReference.INSTANCE);

        getResourceBundles().addJavaScriptBundle(InRenApplication.class,
                "bootstrap-extensions.js",
                JQueryUIJavaScriptReference.instance(),
                Html5PlayerJavaScriptReference.instance());

        getResourceBundles().addCssBundle(InRenApplication.class,
                "bootstrap-extensions.css", Html5PlayerCssReference.instance());

        getResourceBundles().addCssBundle(
                InRenApplication.class,
                "application.css",
                (CssResourceReference) Bootstrap.getSettings()
                .getResponsiveCssResourceReference(),
                (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE
                );
    }

    @Override
    public FeedbackPanel getFeedbackPanel(Page page) {
        return (FeedbackPanel) page.visitChildren(new FeedbackPanelVisitor());
    }

    private static final class FeedbackPanelVisitor implements IVisitor<Component, Component> {
        private final Set<Component> visited = new HashSet<Component>();

        @Override
        public void component(Component component, IVisit<Component> visit) {
            if (!visited.contains(component)) {
                visited.add(component);
                if (component instanceof FeedbackPanel) {
                    visit.stop(component);
                }
            }
        }
    }

}