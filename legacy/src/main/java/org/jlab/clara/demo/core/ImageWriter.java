package org.jlab.clara.demo.core;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Saves images into a ZIP file.
 */
public class ImageWriter implements AutoCloseable {

    private final Path dataSet;
    private final Path outputDir;

    /**
     * Creates a new writer.
     *
     * @param dataSet path to a ZIP file to store images
     * @throws IOException if the file could not be created
     */
    public ImageWriter(Path dataSet) throws IOException {
        this.dataSet = dataSet;
        this.outputDir = getOutputDir(dataSet.getFileName().toString());
    }

    private Path getOutputDir(String zipFileName) throws IOException {
        String baseName = zipFileName.replaceFirst("[.][^.]+$", "");
        Path outputDir = Files.createTempDirectory("demo").resolve(baseName);
        Files.createDirectory(outputDir);
        return outputDir;
    }

    /**
     * Writes a new image.
     *
     * @param image the data of the image
     * @param imageName the filename of the image
     */
    public void writeImage(Mat image, String imageName) {
        System.out.println(String.format("Writing %s", imageName));
        writeImage(image, outputDir.resolve(imageName));
    }

    /**
     * Writes a new image.
     *
     * @param image the data of the image
     * @param outputFile the path to the output image file
     */
    public static void writeImage(Mat image, Path outputFile) {
        Imgcodecs.imwrite(outputFile.toString(), image);
    }

    @Override
    public void close() {
        try {
            Files.deleteIfExists(dataSet);
            ZipUtils.compress(outputDir, dataSet);
            ZipUtils.removeDirectory(outputDir.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
