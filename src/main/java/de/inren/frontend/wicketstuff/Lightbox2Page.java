package de.inren.frontend.wicketstuff;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.wicketstuff.lightbox2.LightboxLink;
import org.wicketstuff.lightbox2.LightboxPanel;

import de.inren.frontend.common.templates.TemplatePage;

public class Lightbox2Page<T> extends TemplatePage<T> {

    public Lightbox2Page() {
        super();
        ResourceReference image = new PackageResourceReference(Lightbox2Page.class, "resources/image-1.jpg");
        ResourceReference thumbnail = new PackageResourceReference(Lightbox2Page.class, "resources/thumb-1.jpg");
        add(new LightboxLink("link", image).add(new Image("image", thumbnail)));

        // Image Set
        add(new LightboxPanel("lightbox1", UrlUtils.rewriteToContextRelative("images/image-1.jpg", RequestCycle.get()), "images/thumb-1.jpg", "plant"));
        add(new LightboxPanel("lightbox2", UrlUtils.rewriteToContextRelative("images/image-2.jpg", RequestCycle.get()), "images/thumb-2.jpg", "plant"));
        add(new LightboxPanel("lightbox3", UrlUtils.rewriteToContextRelative("images/image-3.jpg", RequestCycle.get()), "images/thumb-3.jpg", "plant"));
    }

}
