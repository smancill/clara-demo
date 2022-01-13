/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo;

import dev.smancill.clara.demo.core.FaceDetector;
import dev.smancill.clara.demo.core.Image;
import dev.smancill.clara.demo.core.ImageReader;
import dev.smancill.clara.demo.core.ImageWriter;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Runs a face detector over all images in a given zipped data set.
 */
public final class OpenCVDemo {

    private OpenCVDemo() { }

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        URL classifier = OpenCVDemo.class.getResource("/lbpcascade_frontalface.xml");
        Path inputDataSet = Paths.get(args[0]);
        Path outputDataSet = Paths.get(args[1]);

        try (ImageReader reader = new ImageReader(inputDataSet);
             ImageWriter writer = new ImageWriter(outputDataSet)) { // nocheck: Indentation

            FaceDetector df = new FaceDetector(classifier.getPath());

            for (int i = 0; i < reader.getImageCount(); i++) {
                long start = System.currentTimeMillis();
                Image img = reader.readImage(i);
                long read = System.currentTimeMillis();
                Mat result = df.run(img.mat());
                long end = System.currentTimeMillis();
                writer.writeImage(result, img.name());
                long write = System.currentTimeMillis();
                System.out.println("Read: " + (read - start));
                System.out.println("Time: " + (end - read));
                System.out.println("Write: " + (write - end));
            }

            System.out.println();
            System.out.println("Saved " + outputDataSet.toAbsolutePath());
        }
    }
}
