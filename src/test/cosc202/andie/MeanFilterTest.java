package test.cosc202.andie;

import cosc202.andie.MeanFilter;

import static org.junit.Assert.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


import org.junit.Test;

/**
 * The MeanFilterTest class is a JUnit test class for testing the apply()
 * method of the MeanFilter class.
 * 
 * @author Eszter Scarlett-Herbert
 * @version 2.0
 *          note: the calculation of the known RBG values, and the
 *          implementation of reflection, which is a way to access the
 *          constructor and setting its accessibility flag to true, are adapted
 *          from ChatGPT.
 */
public class MeanFilterTest {
    /**
     * This method is used to test the apply() method of the MeanFilter class. It
     * creates a test image with known hex values and uses reflection to
     * create an instance of the MeanFilter class.
     * The apply() method is then applied to the test image and the resulting
     * image's RGB values are compared to the expected RGB values to verify the
     * accuracy of the conversion.
     * 
     * @throws Exception if there is an exception in creating the test image or
     *                   invoking the apply() method through reflection.
     */
    @Test
    public void testApply() {
       // this creates a new instance of BufferedImage with a size of 3x3 pixels.
        // the getDataBuffer() method returns the DataBuffer which holds the pixel data,
        // which is represented in an array of bytes the Arrays.fill(pixels, (byte) 255)
        // fills the pixel array, setting all pixels in the image to white
        BufferedImage input = new BufferedImage(3, 3, BufferedImage.TYPE_BYTE_GRAY);
        byte[] pixels = ((DataBufferByte) input.getRaster().getDataBuffer()).getData();
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

        // The MeanFilter instance is created using reflection and with a radius of 5.
        // The apply() method is then invoked, and the output image is compared to the
        // expected output by checking the values of the pixels
        try {
            Constructor<MeanFilter> constructor = MeanFilter.class.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            MeanFilter meanFilter = constructor.newInstance(1); // Use desired radius for testing
            BufferedImage actualOutputImage = meanFilter.apply(input);

            // If the RGB value of the center pixel in the actual output image matches the
            // expected value, the test passes. If not, the test fails and an error message
            // is displayed.

            Raster raster = actualOutputImage.getData();
            int[] actualValues = new int[9];
            raster.getPixels(0, 0, 3, 3, actualValues);
            int[] expectedValues = {15, 10, 5, 15, 9, 4, 15, 9, 3};
            assertArrayEquals(expectedValues, actualValues);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Failed to invoke MeanFilter(int) constructor with reflection");
        }

    }

}
