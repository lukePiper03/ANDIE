
package test.cosc202.andie;

import org.junit.Test;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import cosc202.andie.SobelFilter;

/**
 * This class represents a JUnit test for the SobelFilter class.
 * It verifies the correctness of the SobelFilter's apply() method by comparing the actual output image
 * with the expected output image generated using createSobelImage() method.
 *
 * @author Hannah Srzich
 */
public class SobelFilterTest {

    /**
     * Test case for the apply() method of SobelFilter class.
     * It applies the Sobel filter to an input image and compares each pixel of the actual and expected output images.
     */
    @Test
    public void testApply() throws IOException {
        BufferedImage inputImage = createImage();
        BufferedImage expectedOutputImage = createSobelImage();

        // Apply the Sobel filter to the input image
        SobelFilter sobelFilter = new SobelFilter(2);
        BufferedImage actualOutputImage = sobelFilter.apply(inputImage);

        // Compare each pixel of the actual and expected output images
        int width = expectedOutputImage.getWidth();
        int height = expectedOutputImage.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int expectedRGB = expectedOutputImage.getRGB(x, y);
                int actualRGB = actualOutputImage.getRGB(x, y);
                assertEquals(expectedRGB, actualRGB);
            }
        }
    }

    /**
     * Creates a sample input image with two quadrants, green in the top left and red in the bottom right.
     *
     * @return the created input image.
     */
    public static BufferedImage createImage() {
        int width = 2;
        int height = 2;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        // Fill the top left quadrant with green
        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, width / 2, height / 2);

        // Fill the bottom right quadrant with red
        graphics.setColor(Color.RED);
        graphics.fillRect(width / 2, height / 2, width / 2, height / 2);

        return image;
    }

    /**
     * Creates a sample Sobel output image with two pixels, purple in the top left and pink in the bottom right.
     *
     * @return the created Sobel output image.
     */
    public static BufferedImage createSobelImage() {
        int width = 2;
        int height = 2;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Set the top left pixel to purple and the bottom right pixel to pink
        image.setRGB(0, 0, new Color(128, 0, 128).getRGB());
        image.setRGB(1, 0, new Color(255, 128, 128).getRGB());
        image.setRGB(0, 1, new Color(128, 0, 128).getRGB());
        image.setRGB(1, 1, new Color(255, 128, 128).getRGB());

        return image;
    }
}
