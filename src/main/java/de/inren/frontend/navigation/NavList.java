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

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.bricket.b4.core.service.UncheckedRuntimeException;

/**
 * @author Ingo Renner
 *
 */
@Slf4j
public class NavList extends Panel {
    
    private GNode<NavigationElement> node;
    
    public NavList(String id, GNode<NavigationElement> node) {
        super(id);
        this.node = node;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!hasBeenRendered()) {
            initGui();
        }
    }

    private void initGui() {
        
        List<GNode<NavigationElement>> list = node.getChildren();
        
        ListView<GNode<NavigationElement>> listview = new ListView<GNode<NavigationElement>>("nav-ul", list) {

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            protected void populateItem(ListItem<GNode<NavigationElement>> item) {
                
                NavigationElement data = item.getModel().getObject().getData();
                    item.add(new NavListItem("nav-li", data.getClazz(), 
                            new StringResourceModel(data.getLanguageKey(), getComponent(data), null)));
                if (item.getModel().getObject().getChildren().isEmpty()) {
                    item.add(new Label("nav-sub", "").setVisible(false));
                } else {
                    item.add(new NavList("nav-sub", item.getModel().getObject()));
                }
            }

        };
        add(listview);
    }
    
    protected Page getComponent(NavigationElement data) {
        try {
            return data.getClazz().newInstance();
        } catch (InstantiationException e) {
            log.error(e.getMessage(),e);
            throw new UncheckedRuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(),e);
            throw new UncheckedRuntimeException(e.getMessage());
        }
    }
}
