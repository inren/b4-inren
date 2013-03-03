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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ingo Renner
 *
 */
public class GTree<T> implements Serializable {
 
    private GNode<T> root;
     
    public GTree(GNode<T> root) {
        this.root = root;
    }
 
    public GNode<T> getRoot() {
        return this.root;
    }
 
    public List<GNode<T>> toList() {
        List<GNode<T>> list = new ArrayList<GNode<T>>();
        walk(root, list);
        return list;
    }
     
    public String toString() {
        return toList().toString();
    }
     
    private void walk(GNode<T> element, List<GNode<T>> list) {
        list.add(element);
        for (GNode<T> data : element.getChildren()) {
            walk(data, list);
        }
    }
}
