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
package org.bricket.b4.picture.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Time;
import org.bricket.b4.picture.service.PictureModificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The static Url methods are creating urls like this:
 * http://[servername]/[appname
 * ]/resources/org.apache.wicket.Application//pictureResource?id=[digest]
 * http://[servername]/[appname]/resources/org.apache.wicket.Application//
 * pictureResource?id=[digest]&layout
 * http://[servername]/[appname]/resources/org
 * .apache.wicket.Application//pictureResource?id=[digest]&thumb
 * 
 * Diese Urls are bookmarkeable.
 * @author Ingo Renner
 *
 */
public class PictureResource extends ByteArrayResource {
    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    private static final String LAYOUT = "layout";

    private static final String THUMB = "thumb";

    private static final String ID = "id";

    @SpringBean
    private PictureModificationService pictureModificationService;

    protected static final String FORMAT = "jpg";

    public static final String PICTURE_RESOURCE = "/pictureResource";

    public PictureResource() {
        super("image/jpeg");
        log.info("injecting 1");
        Injector.get().inject(this);
    }

    public static String getUrl(String digest) {
        final PackageResourceReference imageResource = new PackageResourceReference(PICTURE_RESOURCE);
        return RequestCycle.get().mapUrlFor(imageResource, null) + "?" + ID + "=" + digest;
    }

    public static String getLayoutUrl(String digest) {
        PackageResourceReference imageResource = new PackageResourceReference(PICTURE_RESOURCE);
        return RequestCycle.get().mapUrlFor(imageResource, null) + "?" + ID + "=" + digest + "&" + LAYOUT;
    }

    public static String getThumbnailUrl(String digest) {
        PackageResourceReference imageResource = new PackageResourceReference(PICTURE_RESOURCE);
        return RequestCycle.get().mapUrlFor(imageResource, null) + "?" + ID + "=" + digest + "&" + THUMB;
    }

    private File getImageFile(IRequestParameters params) {
        log.info("injecting getImageFile");
        Injector.get().inject(this);
        if (params.getParameterNames().contains(ID)) {
            final String digest = params.getParameterValue(ID).toString();
            log.info("digest=" + digest);
            log.info("pictureModificationService=" + pictureModificationService);
            if (isThumbnail(params)) {
                return pictureModificationService.getThumbnailImage(digest);
            } else {
                if (isLayout(params)) {
                    return pictureModificationService.getLayoutImage(digest);
                } else {
                    return pictureModificationService.getImage(digest);
                }
            }
        }
        return null;
    }

    private boolean isThumbnail(IRequestParameters params) {
        return params.getParameterNames().contains(THUMB);
    }

    private boolean isLayout(IRequestParameters params) {
        return params.getParameterNames().contains(LAYOUT);
    }

    protected byte[] getImageData() {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        File file = getImageFile(params);
        if (file != null) {
            try {
                return IOUtils.toByteArray(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                log.error("file not found", e);
            } catch (IOException e) {
                log.error("io error", e);
            }
        }
        log.error("No image data available.");
        return new byte[0];
    }

    protected Time getImageLastModified() {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        File file = getImageFile(params);
        if (file != null) {
            return Time.valueOf(new Date(file.lastModified()));
        }
        return null;
    }

    // TODO where did this go in 1.5 or 1.6 getResourceState ????????????

    // @Override
    // protected synchronized ResourceState getResourceState() {
    // return new ResourceState() {
    // private byte[] imageData;
    //
    // @Override
    // public Time lastModifiedTime() {
    // if (lastModifiedTime == null) {
    // lastModifiedTime = getImageLastModified();
    // if (lastModifiedTime == null) {
    // lastModifiedTime = Time.now();
    // }
    // }
    // return lastModifiedTime;
    // }
    //
    // @Override
    // public byte[] getData() {
    // if (imageData == null) {
    // imageData = getImageData();
    // }
    // return imageData;
    // }
    //
    // @Override
    // public String getContentType() {
    // return "image/" + FORMAT;
    // }
    // };
    // }
}
