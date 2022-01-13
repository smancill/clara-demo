/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo.data;

import dev.smancill.clara.demo.core.Image;
import dev.smancill.clara.demo.core.ImageReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageDataTypeTest {

    @BeforeAll
    public static void setUp() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void serializeDataType() throws Exception {
        var name = "lena.png";
        var resource = getClass().getClassLoader().getResource(name);
        var img = ImageReader.readImage(Path.of(resource.getPath()));

        var serializer = ImageDataType.INSTANCE.serializer();
        var copy = (Image) serializer.read(serializer.write(img));

        assertThat(copy.name(), is(img.name()));
        assertThat(copy.mat().size(), is(img.mat().size()));
        assertThat(copy.mat().type(), is(img.mat().type()));

        assertTrue(compareData(copy.mat(), img.mat()));
    }

    private static boolean compareData(Mat m1, Mat m2) {
        return Arrays.equals(getData(m1),  getData(m2));
    }

    private static byte[] getData(Mat m) {
        var data = new byte[(int) (m.total() * m.channels())];
        m.get(0, 0, data);
        return data;
    }
}
