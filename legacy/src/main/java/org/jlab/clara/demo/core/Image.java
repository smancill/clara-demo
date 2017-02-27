package org.jlab.clara.demo.core;

import org.opencv.core.Mat;

/**
 * A holder for an image read from disk.
 */
public class Image {

    private final Mat image;
    private final String name;

    /**
     * Creates a new image holder.
     *
     * @param image the data of the image
     * @param imageName the name of the image
     */
    public Image(Mat image, String imageName) {
        this.image = image;
        this.name = imageName;
    }

    /**
     * Gets the data of the image.
     *
     * @return the image data
     */
    public Mat mat() {
        return image;
    }

    /**
     * Gets the name of the image.
     *
     * @return the image name
     */
    public String name() {
        return name;
    }
}
