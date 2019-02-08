package org.jlab.clara.demo.services;

import org.jlab.clara.base.ClaraUtil;
import org.jlab.clara.demo.core.FaceDetector;
import org.jlab.clara.demo.core.Image;
import org.jlab.clara.demo.data.ImageDataType;
import org.jlab.clara.engine.EngineData;
import org.jlab.clara.engine.EngineDataType;
import org.jlab.clara.std.services.AbstractService;
import org.jlab.clara.std.services.ServiceUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * A Service that detects faces on the received images.
 */
public class FaceDetectorService extends AbstractService {

    private static final String DEFAULT_CLASSIFIER = "lbpcascade_frontalface.xml";
    private static final String CONFIG_CLASSIFIER_KEY = "classifier";

    private volatile ThreadLocal<FaceDetector> faceDetector;


    /**
     * Creates a new face detector service.
     */
    public FaceDetectorService() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        reset();
    }


    private ThreadLocal<FaceDetector> createFaceDetector(String classifier) {
        System.out.printf("using classifier: %s%n", Paths.get(classifier).getFileName());
        return ThreadLocal.withInitial(() -> new FaceDetector(classifier));
    }


    @Override
    public EngineData configure(EngineData input) {
        if (input.getMimeType().equals(EngineDataType.JSON.mimeType())) {
            String source = (String) input.getData();
            JSONObject config = new JSONObject(source);
            if (config.has(CONFIG_CLASSIFIER_KEY)) {
                try {
                    String classifierResource = config.getString(CONFIG_CLASSIFIER_KEY);
                    faceDetector = createFaceDetector(classifierResource);
                } catch (JSONException e) {
                    System.err.printf("invalid configuration: %s%n", e.getMessage());
                } catch (Exception e) {
                    System.err.printf("could not load classifier: %s%n", e.getMessage());
                }
            }
        } else {
            System.err.printf("unsupported configuration mime-type: %s%n", input.getMimeType());
        }
        return null;
    }


    @Override
    public EngineData execute(EngineData input) {
        EngineData output = new EngineData();
        if (input.getMimeType().equals(ImageDataType.INSTANCE.mimeType())) {
            Image img = (Image) input.getData();
            try {
                System.out.printf("Received image %s: %s%n", img.name(), img.mat());
                Mat result = faceDetector.get().run(img.mat());
                output.setData(ImageDataType.INSTANCE, new Image(result, img.name()));
            } catch (Exception e) {
                ServiceUtils.setError(output, "could not process input image");
            }
        } else {
            ServiceUtils.setError(output, "unexpected mime-type: " + input.getMimeType());
        }
        return output;
    }


    @Override
    public Set<EngineDataType> getInputDataTypes() {
        return ClaraUtil.buildDataTypes(ImageDataType.INSTANCE, EngineDataType.JSON);
    }


    @Override
    public Set<EngineDataType> getOutputDataTypes() {
        return ClaraUtil.buildDataTypes(ImageDataType.INSTANCE, EngineDataType.JSON);
    }


    @Override
    public void reset() {
        faceDetector = createFaceDetector(getDefaultClassifier());
    }


    @Override
    public void destroy() {
        // nothing
    }


    private String getDefaultClassifier() {
        try {
            Path destination = Files.createTempDirectory("temp");
            Path classifier = destination.resolve(DEFAULT_CLASSIFIER);
            destination.toFile().deleteOnExit();
            classifier.toFile().deleteOnExit();
            try (InputStream in = getClass().getClassLoader()
                                            .getResourceAsStream(DEFAULT_CLASSIFIER)) {
                Files.copy(in, classifier);
            }
            return classifier.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
