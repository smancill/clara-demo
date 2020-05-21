package org.jlab.clara.demo.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jlab.clara.demo.core.Image;
import org.jlab.clara.demo.core.ImageReader;
import org.jlab.clara.engine.ClaraSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

public class ImageDataTypeTest {

    @BeforeAll
    public static void setUp() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void serializeDataType() throws Exception {
        String name = "lena.png";
        URL resource = getClass().getClassLoader().getResource(name);
        Image img = ImageReader.readImage(Paths.get(resource.getPath()));

        ClaraSerializer serializer = ImageDataType.INSTANCE.serializer();
        Image copy = (Image) serializer.read(serializer.write(img));

        assertThat(copy.name(), is(img.name()));
        assertThat(copy.mat().size(), is(img.mat().size()));
        assertThat(copy.mat().type(), is(img.mat().type()));

        assertTrue(compareData(copy.mat(), img.mat()));
    }

    private static boolean compareData(Mat m1, Mat m2) {
        return Arrays.equals(getData(m1),  getData(m2));
    }

    private static byte[] getData(Mat m) {
        byte[] data = new byte[(int) (m.total() * m.channels())];
        m.get(0, 0, data);
        return data;
    }
}
