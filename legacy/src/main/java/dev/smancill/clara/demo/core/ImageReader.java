/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo.core;

import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Read images from a ZIP file.
 */
public class ImageReader implements AutoCloseable {

    private final List<Path> imagePaths = new ArrayList<>();
    private final Path unzipDir;

    /**
     * Creates a new reader.
     *
     * @param dataSet path to a ZIP file with images
     * @throws IOException if the file could not be opened
     */
    public ImageReader(Path dataSet) throws IOException {
        unzipDir = ZipUtils.extract(dataSet);
        try (var files = Files.newDirectoryStream(unzipDir)) {
            var it = files.iterator();
            if (it.hasNext()) {
                var path = it.next();
                if (Files.isDirectory(path)) {
                    loadAllImages(path);
                } else {
                    loadAllImages(unzipDir);
                }
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }

    private void loadAllImages(Path dir) throws IOException {
        try (var files = Files.newDirectoryStream(dir)) {
            StreamSupport.stream(files.spliterator(), false)
                         .filter(Files::isRegularFile)
                         .forEach(this.imagePaths::add);
        }
    }

    /**
     * Gets the number of images in the dataset.
     *
     * @return the number of images
     */
    public int getImageCount() {
        return imagePaths.size();
    }

    /**
     * Reads an image from the dataset.
     *
     * @param index the index of the image file in the dataset
     * @return the image
     */
    public Image readImage(int index) {
        var path = imagePaths.get(index);
        return readImage(path);
    }

    /**
     * Reads an image from disk.
     *
     * @param path the path to the image file
     * @return the image
     */
    public static Image readImage(Path path) {
        var fileName = path.getFileName();
        if (fileName == null) {
            throw new IllegalArgumentException("Empty path to image");
        }
        var data = Imgcodecs.imread(path.toString());
        return new Image(data, fileName.toString());
    }

    @Override
    public void close() {
        try {
            ZipUtils.removeDirectory(unzipDir);
        } catch (IOException e) {
            // ignore
        }
        imagePaths.clear();
    }
}
