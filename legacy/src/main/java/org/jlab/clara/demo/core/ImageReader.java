package org.jlab.clara.demo.core;

import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Read images from a ZIP file.
 */
public class ImageReader implements AutoCloseable {

    private List<Path> allImages = new ArrayList<>();
    private final Path unzipDir;

    /**
     * Creates a new reader.
     *
     * @param dataSet path to a ZIP file with images
     * @throws IOException if the file could not be opened
     */
    public ImageReader(Path dataSet) throws IOException {
        unzipDir = ZipUtils.extract(dataSet);
        Path baseDir = Files.newDirectoryStream(unzipDir).iterator().next();
        DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*");
        stream.forEach(path -> allImages.add(path));
    }

    /**
     * Gets the number of images in the dataset.
     *
     * @return the number of images
     */
    public int getImageCount() {
        return allImages.size();
    }

    /**
     * Reads an image from the dataset.
     *
     * @param index the index of the image file in the dataset
     * @return the image
     */
    public Image readImage(int index) {
        Path img = allImages.get(index);
        return readImage(img);
    }

    /**
     * Reads an image from disk.
     *
     * @param img the path to the image file
     * @return the image
     */
    public static Image readImage(Path img) {
        return new Image(Imgcodecs.imread(img.toString()), img.getFileName().toString());
    }

    @Override
    public void close() {
        try {
            ZipUtils.removeDirectory(unzipDir);
        } catch (IOException e) {
            // ignore
        }
        allImages.clear();
    }
}
