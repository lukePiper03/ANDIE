package test.cosc202.andie;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

import cosc202.andie.HueFilter;

/**
 * This class represents a JUnit test for the HueFilter class.
 * It tests the functionality of the HueFilter's apply() method by applying the filter to a test image and verifying the result.
 * The test image is a 3x3 pixel image of random colors that should all change to orange when the HueFilter is applied.
 * The expected result is compared with the actual result.
 * 
 * @author Hannah Srzich
 */
public class HueFilterTest {
    
    /**
     * Test case for the HueFilter's apply() method.
     * It creates a test image with random colors, applies the hue filter to change them to orange, 
     * and verifies the result against the expected values.
     */
    @Test
    public void testHueFilter() {
        // Create a 3x3 pixel image of random colors that should go to orange as they all have maximum saturation
        BufferedImage image = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, Color.RED.getRGB());
        image.setRGB(1, 0, Color.GREEN.getRGB());
        image.setRGB(2, 0, Color.BLUE.getRGB());
        image.setRGB(0, 1, Color.YELLOW.getRGB());
        image.setRGB(1, 1, Color.MAGENTA.getRGB());
        image.setRGB(2, 1, Color.CYAN.getRGB());

        // Apply the HueFilter
        HueFilter hueFilter = new HueFilter(Color.ORANGE);
        BufferedImage outputImage = hueFilter.apply(image);

        // Assert the output is correct
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(0, 0));
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(1, 0));
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(2, 0));
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(0, 1));
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(1, 1));
        assertEquals(Color.ORANGE.getRGB(), outputImage.getRGB(2, 1));
    }
}


