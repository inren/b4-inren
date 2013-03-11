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
package de.inren.frontend.codeflower;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 * 
 */
public class CodeFlowerPanel extends ABasePanel {

    private static final JavaScriptResourceReference D3_JS = new JavaScriptResourceReference(CodeFlowerPanel.class, "js/d3/d3.js");
    private static final JavaScriptResourceReference D3_GEOM_JS = new JavaScriptResourceReference(CodeFlowerPanel.class, "js/d3/d3.geom.js");
    private static final JavaScriptResourceReference D3_LAYOUT_JS = new JavaScriptResourceReference(CodeFlowerPanel.class, "js/d3/d3.layout.js");
    private static final JavaScriptResourceReference CODEFLOWER_JS = new JavaScriptResourceReference(CodeFlowerPanel.class, "js/CodeFlower.js");
    private static final JavaScriptResourceReference DATACONVERTER = new JavaScriptResourceReference(CodeFlowerPanel.class, "js/dataConverter.js");
    private static final PackageResourceReference DATA = new PackageResourceReference(CodeFlowerPanel.class, "data/b4-inren.json");
    private static final CssResourceReference CSS = new CssResourceReference(CodeFlowerPanel.class, "css/codeflower.css");

    private String js ="var currentCodeFlower;" +
            "var createCodeFlower = function(json) {" +
            "  if (currentCodeFlower) { currentCodeFlower.cleanup(); }" +
            "  var total = countElements(json);" +
            "  w = parseInt(Math.sqrt(total) * 32, 10);" + "\n" +
            "  h = parseInt(Math.sqrt(total) * 32, 10);" + "\n" +
            "  currentCodeFlower = new CodeFlower(\"#visualization\", w, h).update(json);" + "\n" +
            "};" + "\n" +
            "d3.json('$data', createCodeFlower);";

    public CodeFlowerPanel(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(D3_JS, false));
        response.render(JavaScriptReferenceHeaderItem.forReference(D3_GEOM_JS, false));
        response.render(JavaScriptReferenceHeaderItem.forReference(D3_LAYOUT_JS, false));
        response.render(JavaScriptReferenceHeaderItem.forReference(CODEFLOWER_JS, false));
        response.render(JavaScriptReferenceHeaderItem.forReference(DATACONVERTER));
        response.render(CssReferenceHeaderItem.forReference(CSS));
        String url = getUrl();
        String out = js.replace("$data", url);
        response.render(JavaScriptReferenceHeaderItem.forScript(out, null));
    }

    private String getUrl() {
            IRequestHandler handler = new ResourceReferenceRequestHandler(DATA, null);
            return RequestCycle.get().urlFor(handler).toString();
    }
    
    @Override
    protected void initGui() {
    }

}
