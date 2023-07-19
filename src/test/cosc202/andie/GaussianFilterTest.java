package test.cosc202.andie;

import cosc202.andie.GaussianBlurFilter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.util.Arrays;

/**
 * The GaussianBlurFilterTest class is a JUnit test class for testing the
 * apply()
 * method of the GaussianBlur class.AND making sure that it keeps the
 * original dimensions of the image
 * 
 * @author Eszter Scarlett-Herbert
 * @version 2.0
 *          note: the idea to use the byte stream is adapted from ChatGPT, have
 *          calculate the values for the GaussianBlur filter myself
 */
public class GaussianFilterTest {
    /**
     * This method is used to test the apply() method of the GaussianBlur class. It
     * creates a test image with a known height and width and then applies the
     * filter to the created image. This is stored as the output image. then the
     * input is compared to the output to make sure they have the same dimensions.
     * 
     * the assert not null checks if the outputImage object is not null, to make
     * sure an image is created when the filter is applied
     */
    @Test
    public void testNonDestructive() {
        BufferedImage inputImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        GaussianBlurFilter filter = new GaussianBlurFilter(3);
        BufferedImage outputImage = filter.apply(inputImage);
        assertNotNull(outputImage);
        assertEquals(inputImage.getWidth(), outputImage.getWidth());
        assertEquals(inputImage.getHeight(), outputImage.getHeight());
    }

    @Test
    /**
     * This method is used to test the apply() method of the GaussianBlur Filter
     * class. It creates a test image with known color values and then applies the
     * filter to the test image and the resulting image's RGB values are compared to
     * the expected RGB values to verify the accuracy of the filter.
     */
    public void testApply() {
        // this creates a new grey scale BufferedImage with a size of 3x3 pixels.
        // the getDataBuffer() method returns the DataBuffer which holds the pixel data,
        // which is represented in an array of bytes the Arrays.fill(pixels, (byte) 255)
        // fills the pixel array, setting all pixels in the image to white
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_BYTE_GRAY);
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Arrays.fill(pixels, (byte) 255);

        // the pixel values at index 1, 4, and 7 to 0 (black are changed to creating a
        // pattern, so that blurrung can occur
        pixels[1] = 0;
        pixels[4] = 0;
        pixels[7] = 0;

        // filter is then applied to the created image
        GaussianBlurFilter filter = new GaussianBlurFilter(1);
        BufferedImage result = filter.apply(image);

        // this makes sure that the image is blurred! firstly the raster takes the data
        // from the buffered image
        // then the expected values are compared to the actual values, and if they are
        // equal the test will pass
        Raster raster = result.getData();
        int[] actualValues = new int[9];
        raster.getPixels(0, 0, 3, 3, actualValues);

        int[] expectedValues = { 253, 5, 253, 253, 5, 253, 253, 5, 253 };
        assertArrayEquals(expectedValues, actualValues);
    }

}
