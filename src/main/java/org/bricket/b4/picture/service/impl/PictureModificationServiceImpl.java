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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.core.service.UncheckedRuntimeException;
import org.bricket.b4.picture.service.PictureModificationService;
import org.bricket.b4.storehouse.service.StorehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhlabs.image.UnsharpFilter;

/**
 * @author Ingo Renner
 *
 */
@Service(value = "pictureModificationService")
public class PictureModificationServiceImpl extends B4ServiceImpl implements PictureModificationService {
    private final Logger log = LoggerFactory.getLogger(PictureModificationServiceImpl.class);

    @Autowired
    private StorehouseService storehouseService;

    private static final float JPEG_QUALITY_FOR_RESAMPLE = 0.85f;
    private static final float UNSHARP_AMOUNT_FOR_RESAMPLE = 0.2f;

    private static final String THUMBNAIL = "thumb";
    private static final String LAYOUT = "layout";

    @Transactional
    @Override
    protected void onInit() throws B4ServiceException {
        storehouseService.init();
    }

    public void initPictureModificationService() {
        Application.get().getSharedResources().add(PictureResource.PICTURE_RESOURCE, new PictureResource());
        final File file = storehouseService.getDataFile(PictureModificationService.FILE_NOT_FOUND_DIGEST);
        if (!file.exists()) {
            InputStream is = PictureModificationServiceImpl.class
                    .getResourceAsStream(PictureModificationService.FILE_NOT_FOUND_NAME);
            final Hex hex = new Hex();
            byte[] md5sum;
                md5sum = (byte[]) hex.decode(PictureModificationService.FILE_NOT_FOUND_DIGEST);
                // prescale to thumbnail, also test for converter
                storehouseService.doImport(is, md5sum);
                getThumbnailImage(PictureModificationService.FILE_NOT_FOUND_DIGEST);
                log.info("picture modification service \"File not found\" image imported");
        }
    }

    @Override
    public File getImage(String digest) {
        final File file = storehouseService.getDataFile(digest);
        if (file.exists()) {
            return file;
        }
        return storehouseService.getDataFile(PictureModificationService.FILE_NOT_FOUND_DIGEST);
    }

    @Override
    public File getLayoutImage(String digest) {
        return getScaledImage(digest, LAYOUT);
    }

    @Override
    public File getThumbnailImage(String digest) {
        return getScaledImage(digest, THUMBNAIL);
    }

    private int getMaxSize(String area) {
        if (THUMBNAIL.equals(area)) {
            return 100;
        }

        if (LAYOUT.equals(area)) {
            return 400;
        }
        throw new UncheckedRuntimeException("Fatal error: don't know scale size for area=[" + area + "]");
    }

    private File getScaledImage(String digest, String area) {
        File file = storehouseService.getDataFile(area, digest);
        if (file.exists()) {
            return file;
        } else {
            try {
                file = storehouseService.createFile(file);
            } catch (IOException e) {
                throw new UncheckedRuntimeException("Fatal error: could not create file for scaled data ["
                        + file.getAbsolutePath() + "]", e);
            }
        }
        try {
            // Let's hope, imagemagik can do the trick
            return doImageMagickScale(digest, area, file);
        } catch (Exception e) {
            // ok, we scale. Grmpf
            log.error(
                    "Tried http://www.imagemagick.org convert, but it failed. Is the convert program in your path? Error: ",
                    e);
            log.error("now trying java imageio");
            return doOurScale(digest, area, file);
        }
    }

    private File doImageMagickScale(String digest, String area, final File file) {
        File orginal = storehouseService.getDataFile(digest);
        if (!orginal.exists()) {
            orginal = getImage(PictureModificationService.FILE_NOT_FOUND_DIGEST);
        }
        final int max = getMaxSize(area);
        List<String> commandArgs = new ArrayList<String>();
        commandArgs.add("convert");
        commandArgs.add(orginal.getPath());
        commandArgs.add("-thumbnail");
        commandArgs.add(max + "x" + max + ">");
        commandArgs.add("-background");
        commandArgs.add("transparent");
        commandArgs.add("-gravity");
        commandArgs.add("center");
        commandArgs.add("-extent");
        commandArgs.add(max + "x" + max);
        commandArgs.add("png:" + file.getPath());
        Process p;
        try {
            p = Runtime.getRuntime().exec((String[]) commandArgs.toArray(new String[commandArgs.size()]));
        } catch (IOException e1) {
            throw new UncheckedRuntimeException("Error converting " + orginal.getPath() + " to " + file.getPath(), e1);
        }
        int exitValue;
        try {
            exitValue = p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while running " + StringUtils.join(commandArgs.iterator(), ' '), e);
        }
        if (exitValue != 0) {
            throw new RuntimeException("Failed to run " + StringUtils.join(commandArgs.iterator(), ' '));
        }
        return file;
    }

    private String getRelativePath(File f) {
        return f.getAbsolutePath().substring(getBaseDir().length() + 1);
    }

    /**
     * @param dgst
     * @param area
     * @param file
     * @return
     */
    protected File doOurScale(String digest, String area, final File file) {
        String originalDigest = digest;
        File orginal = storehouseService.getDataFile(originalDigest);

        if (!orginal.exists()) {
            orginal = getImage(PictureModificationService.FILE_NOT_FOUND_DIGEST);
            originalDigest = PictureModificationService.FILE_NOT_FOUND_DIGEST;
        }

        final int max = getMaxSize(area);
        final ImageInfo info = new ImageInfo();

        FileInputStream infoInputStream = null;
        FileInputStream fis = null;

        try {
            infoInputStream = new FileInputStream(orginal);
            info.setInput(infoInputStream);

            if (!info.check()) {
                throw new UncheckedRuntimeException("Unknow format.");
            }

            // If it's within the dimensions, just return it.
            if (info.getWidth() <= max && info.getHeight() <= max) {
                fis = new FileInputStream(orginal);
                storehouseService.doImport(area, fis, originalDigest);
                return storehouseService.getDataFile(area, originalDigest);
            }
            // Else go find a scaled image instead.
            else {
                BufferedImage newImg = scaleImage(orginal, max);

                // Run an unsharp filter.
                final UnsharpFilter unsharp = new UnsharpFilter();
                unsharp.setAmount(UNSHARP_AMOUNT_FOR_RESAMPLE);
                newImg = unsharp.filter(newImg, null);

                // Output.
                writeJpeg(newImg, file, JPEG_QUALITY_FOR_RESAMPLE);
                return file;
            }
        } catch (IOException e) {
            throw new UncheckedRuntimeException("Unable to scale image.", e);
        } finally {
            if (infoInputStream != null) {
                try {
                    infoInputStream.close();
                } catch (IOException e) {
                    throw new UncheckedRuntimeException("error closing info input stream.", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new UncheckedRuntimeException("error closing input stream.", e);
                }
            }
        }
    }

    private BufferedImage scaleImage(File orginal, final int max) throws IOException {
        // Load the image.
        BufferedImage originalImage = ImageIO.read(orginal);

        // Figure out the new dimensions.
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();
        final double maxOriginal = Math.max(w, h);
        final double scaling = max / maxOriginal;

        final int newW = (int) Math.round(scaling * w);
        final int newH = (int) Math.round(scaling * h);

        // If we need to scale down by more than 2x, scale to double the
        // eventual size and then scale again. This provides much higher
        // quality results.
        if (scaling < 0.5f) {
            final BufferedImage newImg = new BufferedImage(newW * 2, newH * 2, BufferedImage.TYPE_INT_RGB);
            final Graphics2D gfx = newImg.createGraphics();
            gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            gfx.drawImage(originalImage, 0, 0, newW * 2, newH * 2, null);
            gfx.dispose();
            newImg.flush();
            originalImage = newImg;
        }

        // Scale it.
        BufferedImage newImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        final Graphics2D gfx = newImg.createGraphics();
        gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gfx.drawImage(originalImage, 0, 0, newW, newH, null);
        gfx.dispose();
        newImg.flush();
        originalImage.flush();

        return newImg;
    }

    private void writeJpeg(BufferedImage image, File file, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(quality);

        FileImageOutputStream output = new FileImageOutputStream(file);
        writer.setOutput(output);
        writer.write(null, new IIOImage(image, null, null), iwp);
    }

    /**
     * @return
     */
    protected String getBaseDir() {
        return storehouseService.getBaseDataDir().getAbsolutePath();
    }
}
