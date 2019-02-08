package org.jlab.clara.demo.services;

import org.jlab.clara.demo.core.Image;
import org.jlab.clara.demo.core.ImageReader;
import org.jlab.clara.demo.data.ImageDataType;
import org.jlab.clara.engine.EngineData;
import org.jlab.clara.engine.EngineDataType;
import org.jlab.clara.std.services.AbstractEventReaderService;
import org.jlab.clara.std.services.EventReaderException;
import org.json.JSONObject;
import org.opencv.core.Core;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A service that reads images from a ZIP file.
 */
public class ImageReaderService extends AbstractEventReaderService<ImageReader> {

    /**
     * Creates a new image reader service.
     */
    public ImageReaderService() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    protected ImageReader createReader(Path file, JSONObject opts) throws EventReaderException {
        try {
            return new ImageReader(file);
        } catch (IOException e) {
            throw new EventReaderException("Could not create reader", e);
        }
    }

    @Override
    protected void closeReader() {
        try {
            reader.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    protected int readEventCount() throws EventReaderException {
        return reader.getImageCount();
    }

    @Override
    protected ByteOrder readByteOrder() throws EventReaderException {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    protected Object readEvent(int eventNumber) throws EventReaderException {
        return reader.readImage(eventNumber);
    }

    @Override
    protected EngineDataType getDataType() {
        return ImageDataType.INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        Path inputDataSet = Paths.get(ImageReader.class.getResource("/dataset.zip").getPath());

        ImageReaderService reader = new ImageReaderService();

        JSONObject config = new JSONObject();
        config.put("action", "open");
        config.put("file", inputDataSet.toString());

        EngineData configData = new EngineData();
        configData.setData(EngineDataType.JSON, config.toString());

        reader.configure(configData);

        for (int i = 0; i < reader.readEventCount(); i++) {
            Image img = (Image) reader.readEvent(i);
            System.out.println(img.name());
        }

        reader.destroy();
    }
}
