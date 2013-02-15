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

import java.io.File;
import java.nio.charset.Charset;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.encoding.UrlEncoder;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;

import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public class DownloadActionLink extends AActionB4Link {

    private final String fileName;
    private final File file;

    public DownloadActionLink(String fileName, File file) {
        super(false, IconType.download);
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (Strings.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName cannot be an empty string");
        }
        this.file = file;
        this.fileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, Charset.defaultCharset());
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream, fileName));
    }

}
