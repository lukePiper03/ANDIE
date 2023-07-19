package test.cosc202.andie;
import cosc202.andie.SharpenFilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.awt.image.BufferedImage;

/**
 * The SharpnessFilterTest class is a JUnit test class for testing the apply()
 * method of the SharpenFilter class.
 * 
 * @author Eszter Scarlett-Herbert
 * @version 2.0
 *          note: the calculation of the known RBG values are adapted
 *          from ChatGPT.
 */
public class SharpnessFilterTest {
    /**
     * This method is used to test the apply() method of the SharpenFilter class. It
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
        SharpenFilter filter = new SharpenFilter();
        BufferedImage outputImage = filter.apply(inputImage);
        assertNotNull(outputImage);
        assertEquals(inputImage.getWidth(), outputImage.getWidth());
        assertEquals(inputImage.getHeight(), outputImage.getHeight());
    }
   
    /**
     * This method is used to test the apply() method of the SharpenFilter class. It
     * creates a test image with known hex values and uses reflection to
     * create an instance of the SharpenFilter class.
     * The apply() method is then applied to the test image and the resulting
     * image's RGB values are compared to the expected RGB values to verify the
     * accuracy of the conversion.
     * 
     * @throws Exception if there is an exception in creating the test image or
     *                   invoking the apply() method through reflection.
     */
    @Test
    public void testApply() {

        // this is going to create a test image, which is a 3x3 black and white checker
        // board pattern
        BufferedImage input = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        input.setRGB(0, 0, 0x000000);
        input.setRGB(1, 0, 0x000000);
        input.setRGB(2, 0, 0x000000);
        input.setRGB(0, 1, 0x000000);
        input.setRGB(1, 1, 0xFFFFFF);
        input.setRGB(2, 1, 0x000000);
        input.setRGB(0, 2, 0x000000);
        input.setRGB(1, 2, 0x000000);
        input.setRGB(2, 2, 0x000000);

        // this creates an image which is the expected output image after applying the
        // SharpenFilter to the input image.
        BufferedImage expectedOutput = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        expectedOutput.setRGB(0, 0, 0x000000);
        expectedOutput.setRGB(1, 0, 0x000000);
        expectedOutput.setRGB(2, 0, 0x000000);
        expectedOutput.setRGB(0, 1, 0x000000);
        expectedOutput.setRGB(1, 1, 0x000000);
        expectedOutput.setRGB(2, 1, 0x000000);
        expectedOutput.setRGB(0, 2, 0x000000);
        expectedOutput.setRGB(1, 2, 0x000000);
        expectedOutput.setRGB(2, 2, 0x000000);

        // The SharpenFilter object is created, and the apply()
        // method is called on the input image, producing the output image.
        SharpenFilter filter = new SharpenFilter();
        BufferedImage output = filter.apply(input);

        // checks that the output image is equal to the expectedOutput image.
        // This is done by checking that the width and height of the images are the
        // same,
        // and then checking each pixel of the images to ensure that they are the same.
        assertEquals(expectedOutput.getWidth(), output.getWidth());
        assertEquals(expectedOutput.getHeight(), output.getHeight());
        for (int y = 0; y < expectedOutput.getHeight(); y++) {
            for (int x = 0; x < expectedOutput.getWidth(); x++) {
                assertEquals(expectedOutput.getRGB(x, y), output.getRGB(x, y));
            }
        }
    }
}
