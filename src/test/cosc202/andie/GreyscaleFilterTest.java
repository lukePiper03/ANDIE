package test.cosc202.andie;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;

import cosc202.andie.ConvertToGrey;
import static org.junit.Assert.*;

/**
 * The GreyscaleFilterTest class is a JUnit test class for testing the apply()
 * method of the ConvertToGrey class.
 * 
 * @author Eszter Scarlett-Herbert
 * @version 1.0
 *          note: the calculation of the known RBG values, and the
 *          implementation of reflection, which is a way to access the
 *          constructor and setting its accessibility flag to true, are adapted
 *          from ChatGPT.
 */

public class GreyscaleFilterTest {
    /**
     * This method is used to test the apply() method of the ConvertToGrey class. It
     * creates a test image with known greyscale values and uses reflection to
     * create an instance of the ConvertToGrey class.
     * The apply() method is then applied to the test image and the resulting
     * image's RGB values are compared to the expected RGB values to verify the
     * accuracy of the conversion.
     * 
     * @throws Exception if there is an exception in creating the test image or
     *                   invoking the apply() method through reflection.
     */
    
    private final static String INPUT_IMAGE_PATH = "test.jpg";
    private final static String OUTPUT_IMAGE_PATH = "testGreyscaleOutput.png";

    @Test
    public void testApply() throws Exception {
        BufferedImage input = null;
        BufferedImage output = null;
        try {
            String basePath = System.getProperty("user.dir");
            URL inputUrl = new URL("file://" + basePath + "/" + INPUT_IMAGE_PATH);
            URL outputUrl = new URL("file://" + basePath + "/" + OUTPUT_IMAGE_PATH);
            System.out.println("Input image path: " + inputUrl);
            System.out.println("Output image path: " + outputUrl);
            input = ImageIO.read(inputUrl);
            output = ImageIO.read(outputUrl);

        } catch (IOException e) {
            System.out.println("Failed to read test image");
        }

        /*
         * uses the reflection, so the MeanFilter(int) instance can be created without
         * invoking the constructor explicitly.
         * then the conversion is applied to the test image (input image above)
         */
        try {
            Constructor<ConvertToGrey> constructor = ConvertToGrey.class.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            ConvertToGrey greyscale = constructor.newInstance(5); // Use desired radius for testing
            BufferedImage actualOutputImage = greyscale.apply(input);

            // Compare actual and expected output images pixel by pixel.
            int width = input.getWidth();
            int height = input.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    assertEquals(output.getRGB(x, y), actualOutputImage.getRGB(x, y));
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Failed to invoke MeanFilter(int) constructor with reflection");
        }

    }

}
