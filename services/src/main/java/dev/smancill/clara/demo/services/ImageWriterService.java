/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo.services;

import dev.smancill.clara.demo.core.Image;
import dev.smancill.clara.demo.core.ImageWriter;
import dev.smancill.clara.demo.data.ImageDataType;
import org.jlab.clara.engine.EngineDataType;
import org.jlab.clara.std.services.AbstractEventWriterService;
import org.jlab.clara.std.services.EventWriterException;
import org.json.JSONObject;
import org.opencv.core.Core;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A service that writes images into a ZIP file.
 */
public class ImageWriterService extends AbstractEventWriterService<ImageWriter> {

    /**
     * Creates a new image writer service.
     */
    public ImageWriterService() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    protected ImageWriter createWriter(Path file, JSONObject opts) throws EventWriterException {
        try {
            return new ImageWriter(file);
        } catch (IOException e) {
            throw new EventWriterException(e);
        }
    }

    @Override
    protected void closeWriter() {
        try {
            writer.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    protected void writeEvent(Object event) throws EventWriterException {
        var img = (Image) event;
        writer.writeImage(img.mat(), img.name());
    }

    @Override
    protected EngineDataType getDataType() {
        return ImageDataType.INSTANCE;
    }
}
