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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.model.Model;
import org.bricket.b4.securityinren.service.RoleService.Roles;

import de.agilecoders.wicket.markup.html.bootstrap.navbar.INavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar.ComponentPosition;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.inren.frontend.admin.AdminPage;
import de.inren.frontend.application.HomePage;
import de.inren.frontend.auth.LoginPage;
import de.inren.frontend.health.HealthChartPage;
import de.inren.frontend.health.HealthSettingsPage;
import de.inren.frontend.health.ManageMeasurementsPage;
import de.inren.frontend.role.ManageRolesPage;
import de.inren.frontend.user.ManageUsersPage;
import de.inren.frontend.usersettings.UserSettingsPage;

/**
 * 
 * Decide wich item apears under wich navigationbar.
 * 
 * Hard coded version. Has to be replaced by a db service one in the future.
 * 
 * @author Ingo Renner
 *
 */
@Slf4j
public class NavigationProvider {

    private static NavigationProvider navigationProvider;
    
    private GTree<NavigationElement> tree;
    
    private NavigationProvider() {
        initNavigation();
    }
    
    public static NavigationProvider get() {
        if (navigationProvider == null) {
            navigationProvider = new NavigationProvider();
        }
        return navigationProvider;
    }
    
    public List<INavbarComponent> getTopNavbarComponents(Collection<String> roles) {
        
        List<INavbarComponent> res = new ArrayList<INavbarComponent>();
        // Root
        if (hasRight(tree.getRoot().getData().getRoles(), roles)) {
            res.add(createINavbarComponent(tree.getRoot().getData()));
        }
        // and 1 level
        for (GNode<NavigationElement> e : tree.getRoot().getChildren()) {
            if (hasRight(e.getData().getRoles(), roles)) {
                res.add(createINavbarComponent(e.getData()));
            }
        }
        
        return res;
    }

    public GNode<NavigationElement> getSideNavbarComponents(Class clazz, Collection<String> roles) {

        GNode<NavigationElement> res = null;
        GNode<NavigationElement> r = null;
        for (GNode<NavigationElement> n : tree.getRoot().getChildren()) {
            
            if (findNode(n, clazz)!=null) {
                r = n;
                break;
            }
        }
        if (r!=null) {
            if (hasRight(r.getData().getRoles(), roles)) {
                res = new GNode<NavigationElement>(r.getData());
            }
            // and 1 level
            for (GNode<NavigationElement> e : r.getChildren()) {
                if (hasRight(e.getData().getRoles(), roles)) {
                    res.addChild(e);
                }
            }
        }
        return res;
    }

    private GNode<NavigationElement> findNode(GNode<NavigationElement> node, Class clazz) {
        if (node.getData().getClazz().equals(clazz)) {
            return node;
        }
        for (GNode<NavigationElement> n : node.getChildren()) {
            GNode<NavigationElement> r = findNode(n, clazz);
            if (r!=null) {
                return r;
            }
        }
        return null;
    }

    private INavbarComponent createINavbarComponent(NavigationElement data) {
        return new ImmutableNavbarComponent(new NavbarButton<LoginPage>(data.getClazz(), Model.of(data.getLanguageKey())), data.getPosition());
    }

    private boolean hasRight(List<String> needRoles, Collection<String> givenRoles) {

        if (needRoles.isEmpty()) {
            return true;
        }
        for (String r : needRoles) {
            if (givenRoles.contains(r)) {
                return true;
            }
        }
        return false;
    }

    private void initNavigation() {
        
        // TODO read this data from an editable source like db or xml file.
        
        // static hack to speed up development for other places
        GNode<NavigationElement> root = 
            new GNode<NavigationElement>(new NavigationElement(HomePage.class, "Home", Collections.<String> emptyList(), ComponentPosition.LEFT))
                .addChild(new GNode<NavigationElement>(
                            new NavigationElement(ManageMeasurementsPage.class, "Health", Arrays.asList(Roles.ROLE_USER.name(), Roles.ROLE_ADMIN.name()), ComponentPosition.LEFT), Arrays.asList(
                                    new GNode<NavigationElement>(new NavigationElement(HealthSettingsPage.class, "Settings", Arrays.asList(Roles.ROLE_ADMIN.name()), ComponentPosition.LEFT)),
                                    new GNode<NavigationElement>(new NavigationElement(HealthChartPage.class, "Chart", Arrays.asList(Roles.ROLE_ADMIN.name()), ComponentPosition.LEFT))
                                    )
                                )
                            )
                .addChild(new GNode<NavigationElement>(
                            new NavigationElement(AdminPage.class, "Admin", Arrays.asList(Roles.ROLE_ADMIN.name()), ComponentPosition.RIGHT), Arrays.asList(
                                    new GNode<NavigationElement>(new NavigationElement(ManageUsersPage.class, "Users", Arrays.asList(Roles.ROLE_ADMIN.name()), ComponentPosition.LEFT)),
                                    new GNode<NavigationElement>(new NavigationElement(ManageRolesPage.class, "Roles", Arrays.asList(Roles.ROLE_ADMIN.name()), ComponentPosition.LEFT))
                                )
                            )
                        )
                .addChild(new GNode<NavigationElement>(new NavigationElement(UserSettingsPage.class, "Settings", Arrays.asList(Roles.ROLE_USER.name(), Roles.ROLE_ADMIN.name()), ComponentPosition.RIGHT))
              );
        tree = new GTree<NavigationElement>(root);
    }
}
