/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo;

import dev.smancill.clara.demo.core.FaceDetector;
import dev.smancill.clara.demo.core.ImageReader;
import dev.smancill.clara.demo.core.ImageWriter;
import org.opencv.core.Core;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Runs a face detector over all images in a given zipped data set.
 */
public final class OpenCVDemo {

    private OpenCVDemo() { }

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        var classifier = OpenCVDemo.class.getResource("/lbpcascade_frontalface.xml");
        if (classifier == null) {
            throw new IOException("classifier not found");
        }
        var inputDataSet = Path.of(args[0]);
        var outputDataSet = Path.of(args[1]);

        try (var reader = new ImageReader(inputDataSet);
             var writer = new ImageWriter(outputDataSet)) {

            var df = new FaceDetector(classifier.getPath());

            for (int i = 0; i < reader.getImageCount(); i++) {
                var start = System.currentTimeMillis();

                var img = reader.readImage(i);
                var read = System.currentTimeMillis();

                var result = df.run(img.mat());
                var end = System.currentTimeMillis();

                writer.writeImage(result, img.name());
                var write = System.currentTimeMillis();

                System.out.println("Read: " + (read - start));
                System.out.println("Time: " + (end - read));
                System.out.println("Write: " + (write - end));
            }

            System.out.println();
            System.out.println("Saved " + outputDataSet.toAbsolutePath());
        }
    }
}
