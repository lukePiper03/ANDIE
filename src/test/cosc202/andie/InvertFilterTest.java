package test.cosc202.andie;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

import cosc202.andie.InvertFilter;

/**
 * This class represents a JUnit test for the InvertFilter class.
 * It tests the functionality of the InvertFilter's apply() method by applying the filter to a test image and verifying the result.
 * The test image is a 3x1 pixel image with built-in Java colors, and the expected result is compared with the actual result.
 * 
 * @author Hannah Srzich
 */
public class InvertFilterTest {
    
    /**
     * Test case for the InvertFilter's apply() method.
     * It creates a test image with built-in Java colors, applies the invert filter, and verifies the 
     * result against the expected inverse color values.
     */
    @Test
    public void testInvertFilter() {
        // Create a 3x1 pixel image of Java's built-in colors
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, Color.RED.getRGB());
        image.setRGB(1, 0, Color.GREEN.getRGB());
        image.setRGB(2, 0, Color.BLUE.getRGB());

        // Apply the invert filter
        InvertFilter invertFilter = new InvertFilter();
        BufferedImage outputImage = invertFilter.apply(image);

        // Compare the input and output pixels (their inverses)
        assertEquals(Color.CYAN.getRGB(), outputImage.getRGB(0, 0));
        assertEquals(Color.MAGENTA.getRGB(), outputImage.getRGB(1, 0));
        assertEquals(Color.YELLOW.getRGB(), outputImage.getRGB(2, 0));
    }
}

