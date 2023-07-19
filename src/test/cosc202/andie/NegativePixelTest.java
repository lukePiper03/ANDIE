package test.cosc202.andie;

import org.junit.Test;
import cosc202.andie.NormalizeImage;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;

/**
 * This class represents a JUnit test for the NegativePixelTest class.
 * It tests the functionality of the normalizeImage() method by creating a test image with negative pixel values
 * and verifying that after calling the normalizeImage() method, all pixel values become positive.
 * 
 * @author Hannah Srzich
 */
public class NegativePixelTest {

    /**
     * Test case for the normalizeImage() method.
     * It creates a test image with negative R, G, and B pixel values and verifies that after calling the normalizeImage() method,
     * all pixel values become positive.
     */
    @Test
    public void testNormalizeImage() {
        // Create a 3x3 image with negative R, G, and B pixel values
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        // Set negative R, G, and B values for each pixel
        int[][] pixelValues = {
            {-50, -100, -150},
            {-75, -125, -175},
            {-200, -225, -250}
        };

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int r = pixelValues[y][x]; // put each pixel value into r, g, b
                int g = pixelValues[y][x];
                int b = pixelValues[y][x];
                int pixel = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, pixel);
            }
        }

        // Call the normalizeImage method
        BufferedImage normalizedImage = NormalizeImage.shiftPixelValue(image);

        // Verify the normalized pixel values are all positive
        for (int y = 0; y < normalizedImage.getHeight(); y++) {
            for (int x = 0; x < normalizedImage.getWidth(); x++) {
                int rgb = normalizedImage.getRGB(x, y);

                // Extract R, G, and B values
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                assertTrue(r >= 0 && g >= 0 && b >= 0);
            }
        }
    }
}


