package test.cosc202.andie;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import cosc202.andie.EmbossFilter;

import static org.junit.Assert.assertEquals;

/**
 * This class represents a JUnit test for the EmbossFilter class.
 * It tests the functionality of the EmbossFilter's apply() method by applying the filter to a test image and verifying the result.
 * The test image is a 2x2 pixel image with predefined colors, and the expected result is compared with the actual result.
 * 
 * @author Hannah Srzich
 */
public class EmbossFilterTest {

    /**
     * Test case for the EmbossFilter's apply() method.
     * It creates a test image, applies the emboss filter, and verifies the result against the expected values.
     */
    @Test
    public void testEmbossFilter() {
        // create a 2x2 pixel test image
        int width = 2;
        int height = 2;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // set the pixels of the test image
        int[] pixels = {
            Color.RED.getRGB(), Color.GREEN.getRGB(), Color.BLUE.getRGB(), Color.WHITE.getRGB()
        };
        image.setRGB(0, 0, width, height, pixels, 0, width);

        // apply the emboss filter
        EmbossFilter embossFilter = new EmbossFilter();
        BufferedImage resultImage = embossFilter.apply(image);

        // verify the result image with expected pixel values
        assertEquals(new Color(255, 0, 128).getRGB(), resultImage.getRGB(0, 0));
        assertEquals(new Color(0, 0, 128).getRGB(), resultImage.getRGB(0, 1));
        assertEquals(new Color(0, 0, 128).getRGB(), resultImage.getRGB(1, 1));
        assertEquals(new Color(255, 0, 128).getRGB(), resultImage.getRGB(1, 0));
    }
}






