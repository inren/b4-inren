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

import java.util.Locale;
import java.util.MissingResourceException;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.BootstrapLess;
import de.agilecoders.wicket.less.BootstrapLessCompilerSettings;
import de.agilecoders.wicket.less.IBootstrapLessCompilerSettings;
import de.agilecoders.wicket.less.Less4JCompiler;
import de.agilecoders.wicket.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.jqueryui.JQueryUIJavaScriptReference;
import de.agilecoders.wicket.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.markup.html.themes.google.GoogleTheme;
import de.agilecoders.wicket.markup.html.themes.metro.MetroTheme;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import de.inren.frontend.auth.B4AuthorizationStrategy;
import de.inren.frontend.common.dns.DnsUtil;
import de.inren.frontend.common.session.B4WebSession;

/**
 * @author Ingo Renner
 * 
 */
@Slf4j
public class InRenApplication extends WebApplication implements IB4Application {
    
    private Folder uploadFolder = null;
    
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

        // new AnnotatedMountScanner().scanPackage("org.bricket.b4.*.wicket").mount(this);

        // TODO gehört ins codeflower package. => Init für Wicket module/packages machen.
        final IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            ((SecurePackageResourceGuard) packageResourceGuard).addPattern("+*.json");
        }
        
        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
            
            @Override
            public void onBeginRequest(RequestCycle cycle) {
                WebClientInfo ci = new WebClientInfo(cycle);                
                log.debug("Request info: " 
                        + ci.getProperties().getRemoteAddress() + ", "
                        + ("".equals(DnsUtil.lookup(ci.getProperties().getRemoteAddress())) ? "" : DnsUtil.lookup(ci.getProperties().getRemoteAddress()) + ", ")
                        + (cycle.getRequest().getUrl().getPath()==null || "".equals(cycle.getRequest().getUrl().getPath()) ? "" : cycle.getRequest().getUrl().getPath() + ", ") 
                        + ci.getUserAgent()
                    );
           }
        });
        
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
        
        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "inren-uploads");
        uploadFolder.mkdirs();
        
        getApplicationSettings().setUploadProgressUpdatesEnabled(true); 
        
        this.getMarkupSettings().setStripWicketTags(true);
        this.getMarkupSettings().setStripComments(true);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new B4WebSession(request);
    }

    
    private void configureBootstrap() {
        final BootstrapSettings settings = new BootstrapSettings();
        final ThemeProvider themeProvider = new BootswatchThemeProvider() {
            {
                add(new MetroTheme());
                add(new GoogleTheme());
                defaultTheme("cyborg");
            }
        };
        settings.setThemeProvider(themeProvider);
        settings.setJsResourceFilterName("footer-container")
                .setThemeProvider(themeProvider);
        Bootstrap.install(this, settings);

        final IBootstrapLessCompilerSettings lessCompilerSettings = new BootstrapLessCompilerSettings();
        lessCompilerSettings.setUseLessCompiler(usesDevelopmentConfig())
                .setLessCompiler(new Less4JCompiler());
        BootstrapLess.install(this, lessCompilerSettings);


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
                .getCssResourceReference(),
                (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE
                );
    }
    

    @Override
    public FeedbackPanel getFeedbackPanel(Page page) {
        return (FeedbackPanel) page.visitChildren(new FeedbackPanelVisitor());
    }
   
    /**
     * @return the folder for uploads
     */
    public Folder getUploadFolder() {
        return uploadFolder;
    }
}
