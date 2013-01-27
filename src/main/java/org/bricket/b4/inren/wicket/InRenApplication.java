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
package org.bricket.b4.inren.wicket;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.bricket.b4.core.wicket.IB4Application;
import org.bricket.b4.inren.wicket.pages.HomePage;
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

	/* Bootstrap */
	configureBootstrap();

	new AnnotatedMountScanner().scanPackage("org.bricket.b4.*.wicket")
		.mount(this);
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