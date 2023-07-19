package test.cosc202.andie;

import org.junit.Test;

import cosc202.andie.CropImage;
import cosc202.andie.ImagePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static org.junit.Assert.*;

/**
 * Test the CropImage class returns the expected output from the given mouse
 * region on the image
 */
public class CropImageTest {

    /**
     * Testing the expected output of a cropped image
     * By checking the width and height and then comparing the original image and
     * newly cropped image pixel by pixel
     */
    @Test
    public void cropImageTest1() {
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;
        // New resized image
        CropImage cropImage = new CropImage(new Rectangle(10, 10, 20, 40));
        // New image
        BufferedImage input = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

        // Rotate the image
        BufferedImage output = cropImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(20, output.getWidth());
        assertEquals(40, output.getHeight());

        // Check the input image and crop image pixels match
        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                assertEquals(input.getRGB(x + 10, y + 10), output.getRGB(x, y));
            }
        }
    }

    /**
     * Test the crop image function with a rectangle that is outside the bounds of
     * the image
     * Check the width and height of the cropped image is correct and doesnt go past
     * the image bounds
     * Then compare input image and cropped image pixel by pixel
     */
    @Test
    public void cropImageTest2() {
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;
        // New resized image
        CropImage cropImage = new CropImage(new Rectangle(30, 30, 30, 30));
        // New image
        BufferedImage input = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

        // Rotate the image
        BufferedImage output = cropImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(20, output.getWidth());
        assertEquals(20, output.getHeight());

        // Check the input image and crop image pixels match
        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                assertEquals(input.getRGB(x + 30, y + 30), output.getRGB(x, y));
            }
        }
    }
}
