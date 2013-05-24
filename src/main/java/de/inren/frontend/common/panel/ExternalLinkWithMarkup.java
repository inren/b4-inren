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
package de.inren.frontend.common.panel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Wrap an ExternalLink so you can use it like a Label and get an a tag. 
 * 
 * @author Ingo Renner
 *
 */
public class ExternalLinkWithMarkup extends Panel {

    public ExternalLinkWithMarkup(String id, IModel<String> href, IModel<?> label, AttributeAppender attributeAppender) {
        super(id);
        final ExternalLink externalLink = new ExternalLink("link", href, label);
        if (attributeAppender!=null) {
            externalLink.add(attributeAppender);
        }
        add(externalLink);
    }

    public ExternalLinkWithMarkup(String id, IModel<String> href, AttributeAppender attributeAppender) {
        super(id);
        final ExternalLink externalLink = new ExternalLink("link", href);
        add(externalLink);
    }

    public ExternalLinkWithMarkup(String id, String href, String label, AttributeAppender attributeAppender) {
        super(id);
        final ExternalLink externalLink = new ExternalLink("link", href, label);
        if (attributeAppender!=null) {
            externalLink.add(attributeAppender);
        }
        add(externalLink);
    }

    public ExternalLinkWithMarkup(String id, String href, AttributeAppender attributeAppender) {
        super(id);
        final ExternalLink externalLink = new ExternalLink("link", href);
        if (attributeAppender!=null) {
            externalLink.add(attributeAppender);
        }
        add(externalLink);
    }
}
