package org.jlab.clara.demo.data;

import org.jlab.clara.base.error.ClaraException;
import org.jlab.clara.demo.core.Image;
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
                Image img = (Image) data;

                int nameLength = img.name().length();
                long imgSize = img.mat().total() * img.mat().channels();
                long totalBytes = IMG_META_SIZE + nameLength + imgSize;

                int bufferSize = (int) totalBytes;
                if (bufferSize != totalBytes) {
                    throw new IllegalArgumentException("image is too large");
                }

                byte[] imgData = new byte[(int) imgSize];
                img.mat().get(0, 0, imgData);

                ByteBuffer buffer = allocateBytes(bufferSize);
                buffer.putInt(nameLength);
                buffer.putInt(img.mat().rows());
                buffer.putInt(img.mat().cols());
                buffer.putInt(img.mat().type());
                buffer.putInt((int) imgSize);

                buffer.put(img.name().getBytes());
                buffer.put(imgData);
                buffer.flip();

                return buffer;
            }

            @Override
            public Object read(ByteBuffer buffer) throws ClaraException {
                int nameLength = buffer.getInt();
                int imgRows = buffer.getInt();
                int imgCols = buffer.getInt();
                int imgType = buffer.getInt();
                int imgSize = buffer.getInt();

                byte[] nameData = new byte[nameLength];
                buffer.get(nameData);
                String name = new String(nameData);

                byte[] imgData = new byte[imgSize];
                buffer.get(imgData);
                Mat img = new Mat(imgRows, imgCols, imgType);
                img.put(0, 0, imgData);

                return new Image(img, name);
            }
        });
    }

    private static ByteBuffer allocateBytes(int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }
}
