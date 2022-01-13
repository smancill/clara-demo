/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo.data;

import dev.smancill.clara.demo.core.Image;
import org.jlab.clara.base.error.ClaraException;
import org.jlab.clara.engine.ClaraSerializer;
import org.jlab.clara.engine.EngineDataType;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Clara engine data-type for an image.
 */
public final class ImageDataType extends EngineDataType {

    private static final String MIME_TYPE = "binary/img-file";

    private static final int IMG_META_FIELDS = 5;
    private static final int IMG_META_SIZE = Integer.BYTES * IMG_META_FIELDS;

    /**
     * A data-type object.
     */
    public static final ImageDataType INSTANCE = new ImageDataType();

    /**
     * Creates the data-type.
     */
    private ImageDataType() {
        super(MIME_TYPE, new ClaraSerializer() {

            @Override
            public ByteBuffer write(Object data) throws ClaraException {
                var img = (Image) data;

                int nameLength = img.name().length();
                long imgSize = img.mat().total() * img.mat().channels();
                long totalBytes = IMG_META_SIZE + nameLength + imgSize;

                int bufferSize = (int) totalBytes;
                if (bufferSize != totalBytes) {
                    throw new IllegalArgumentException("image is too large");
                }

                var imgData = new byte[(int) imgSize];
                img.mat().get(0, 0, imgData);

                var buffer = allocateBytes(bufferSize);
                buffer.putInt(nameLength);
                buffer.putInt(img.mat().rows());
                buffer.putInt(img.mat().cols());
                buffer.putInt(img.mat().type());
                buffer.putInt(imgData.length);

                buffer.put(img.name().getBytes());
                buffer.put(imgData);
                buffer.flip();

                return buffer;
            }

            @Override
            public Object read(ByteBuffer buffer) throws ClaraException {
                var nameLength = buffer.getInt();
                var imgRows = buffer.getInt();
                var imgCols = buffer.getInt();
                var imgType = buffer.getInt();
                var imgSize = buffer.getInt();

                var nameData = new byte[nameLength];
                buffer.get(nameData);
                var name = new String(nameData);

                var imgData = new byte[imgSize];
                buffer.get(imgData);
                var img = new Mat(imgRows, imgCols, imgType);
                img.put(0, 0, imgData);

                return new Image(img, name);
            }
        });
    }

    private static ByteBuffer allocateBytes(int size) {
        var bb = ByteBuffer.allocate(size);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }
}
