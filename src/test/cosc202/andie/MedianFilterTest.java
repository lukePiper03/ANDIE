package test.cosc202.andie;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.*;
import java.util.Arrays;
import cosc202.andie.MedianFilter;

/**
 * JUnit testing for to applying a Median filter to an image of radius 1 and 6. Note that the expected output images were extracted from 
 * an online image editor.
 * 
 * @author Hannah Srzich, adapted from https://stackoverflow.com/questions/2343187/loading-resources-using-getclass-getresource
 * @version 2.0
 */
public class MedianFilterTest {
    @Test
    public void testNonDestructive() {
        BufferedImage input = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        MedianFilter filter = new MedianFilter();

        // apply the filter with different radii
        for (int r = 1; r <= 5; ++r) {
            filter = new MedianFilter(r);
            BufferedImage output = filter.apply(input);
            // check if the output image is not null and has the same dimensions as the input image
            assertNotNull(output);
            assertEquals(input.getWidth(), output.getWidth());
            assertEquals(input.getHeight(), output.getHeight());
        }
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

        pixels[0] = 3;
        pixels[1] = 9;
        pixels[2] = 4;
        pixels[3] = 52;
        pixels[4] = 3;
        pixels[5] = 8;
        pixels[6] = 6;
        pixels[7] = 2;
        pixels[8] = 2;


        // filter is then applied to the created image
        MedianFilter filter = new MedianFilter(6);
        BufferedImage result = filter.apply(image);

        // this makes sure that the median filter is blurred! firstly the raster takes the data
        // from the buffered image
        // then the expected values are compared to the actual values, and if they are
        // equal the test will pass
        Raster raster = result.getData();
        int[] actualValues = new int[9];
        raster.getPixels(0, 0, 3, 3, actualValues);

        int[] expectedValues = {4, 4, 4, 4, 4, 4, 4, 4, 4};
        assertArrayEquals(expectedValues, actualValues);
    }


}
