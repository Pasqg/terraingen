package org.pasqg.terraingen.utils;

import javax.imageio.ImageIO;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static java.awt.image.Raster.createWritableRaster;

public class ImageUtils {

    public static void writeToFile(BufferedImage aImage, String aPath, String aFormat) throws IOException {
        ImageIO.write(aImage, aFormat, new File(aPath));
    }

    public static BufferedImage fromFloatArray(float[] aFloats, int aWidth, int aHeight) {
        BufferedImage image = floatImage(aWidth, aHeight);
        float[] data = ((DataBufferFloat) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(aFloats, 0, data, 0, aFloats.length);
        return image;
    }

    private static BufferedImage floatImage(int aWidth, int aHeight) {
        SampleModel sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT,
                aWidth, aHeight, 1, aWidth, new int[]{0});
        DataBuffer buffer = new DataBufferFloat(aWidth * aHeight);

        boolean isAlphaPremultiplied = false;
        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                false,
                isAlphaPremultiplied,
                Transparency.OPAQUE,
                DataBuffer.TYPE_FLOAT);
        WritableRaster writeableRaster = createWritableRaster(sampleModel, buffer, null);
        return new BufferedImage(colorModel, writeableRaster, colorModel.isAlphaPremultiplied(), null);
    }
}
