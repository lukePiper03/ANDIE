package test.cosc202.andie;

import org.junit.Test;
import cosc202.andie.RotateImage;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;

public class RotateImageTest {

    /**
     * Test of apply method, of class RotateImage.
     * Testing the expected output of a 180 degree rotation
     * Creates a test image and rotates it 180 degrees and checks it output pixels are in correct place
     */
    @Test
    public void rotateImage180Test() {
        // Create a test image
        RotateImage rotateImage = new RotateImage(false, true);
        BufferedImage input = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        input.setRGB(0, 0, 0xFF0000);
        input.setRGB(1, 0, 0x00FF00);
        input.setRGB(0, 1, 0xFFFF00);
        input.setRGB(1, 1, 0xFF00FF);

        // Rotate the test image
        BufferedImage output = rotateImage.apply(input);

        // Check rotated image has correct width and height
        assertEquals(2, output.getWidth());
        assertEquals(2, output.getHeight());

        // Check rotated image has rotated pixels correctly
        assertEquals(0xFF00FF, output.getRGB(0, 0));
        assertEquals(0xFFFF00, output.getRGB(1, 0));
        assertEquals(0x00FF00, output.getRGB(0, 1));
        assertEquals(0xFF0000, output.getRGB(1, 1));
    }

    /**
     * Test of apply method, of class RotateImage.
     * Testing the expected output of a 90 degree rotation to the right
     * Creates a 3x2 image and rotates it 90 degrees to the right and compares each pixel
     *  is in correct location after rotation
     */
    @Test
    public void rotateImage90RightTest() {
        RotateImage rotateImage = new RotateImage(true, false);
        BufferedImage input = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        input.setRGB(0, 0, 0xFF0000);
        input.setRGB(1, 0, 0x00FF00);
        input.setRGB(2, 0, 0x00FF00);
        input.setRGB(0, 1, 0xFFFF00);
        input.setRGB(1, 1, 0xFF00FF);
        input.setRGB(2, 1, 0x00FF00);

        // Rotate the test image
        BufferedImage output = rotateImage.apply(input);

        // Check rotated image has correct width and height
        assertEquals(2, output.getWidth());
        assertEquals(3, output.getHeight());

        // Check rotated image has rotated pixels correctly
        assertEquals(0xFFFF00, output.getRGB(0, 0));
        assertEquals(0xFF0000, output.getRGB(1, 0));
        assertEquals(0xFF00FF, output.getRGB(0, 1));
        assertEquals(0x00FF00, output.getRGB(1, 1));
        assertEquals(0x00FF00, output.getRGB(0, 2));
        assertEquals(0x00FF00, output.getRGB(1, 2));
    }

    /**
     * Test of apply method, of class RotateImage.
     * Testing the expected output of a 90 degree rotation to the left
     * Creates a 3x2 image and rotates it 90 degrees to the left and compares
     *  the output to the expected output
     */
    @Test
    public void rotateImage90LeftTest() {
        RotateImage rotateImage = new RotateImage(false, false);
        BufferedImage input = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        input.setRGB(0, 0, 0xFF0000);
        input.setRGB(1, 0, 0x00FF00);
        input.setRGB(2, 0, 0x00FF00);
        input.setRGB(0, 1, 0xFFFF00);
        input.setRGB(1, 1, 0xFF00FF);
        input.setRGB(2, 1, 0x00FF00);

        // Rotate the test image
        BufferedImage output = rotateImage.apply(input);

        // Check rotated image has correct width and height
        assertEquals(2, output.getWidth());
        assertEquals(3, output.getHeight());

        // Check rotated image has rotated pixels correctly
        assertEquals(0x00FF00, output.getRGB(0, 0));
        assertEquals(0x00FF00, output.getRGB(1, 0));
        assertEquals(0x00FF00, output.getRGB(0, 1));
        assertEquals(0xFF00FF, output.getRGB(1, 1));
        assertEquals(0xFF0000, output.getRGB(0, 2));
        assertEquals(0xFFFF00, output.getRGB(1, 2));
    }
}
