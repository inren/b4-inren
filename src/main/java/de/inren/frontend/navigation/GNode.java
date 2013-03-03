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
public class GNode<T> implements Serializable {
 
    private final T data;
    private final List<GNode<T>> children = new ArrayList<GNode<T>>();
 
    public GNode(T data) {
        this.data = data;
    }
     
    public GNode(T data, List<GNode<T>> children) {
        this.data = data;
        setChildren(children);
    }

    public List<GNode<T>> getChildren() {
        return this.children;
    }
 
    public void setChildren(List<GNode<T>> children) {
        this.children.clear();
        this.children.addAll(children);
    }
 
    public GNode<T> addChild(GNode<T> child) {
        children.add(child);
        return this;
    }
     
    public boolean removeChild(GNode<T> child) {
        return children.remove(child);
    }
 
    public T getData() {
        return this.data;
    }
 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(getData().toString()).append(",[");
        int i = 0;
        for (GNode<T> e : getChildren()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(e.getData().toString());
            i++;
        }
        sb.append("]").append("}");
        return sb.toString();
    }
}