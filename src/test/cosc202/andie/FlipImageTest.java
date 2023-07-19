package test.cosc202.andie;

import org.junit.Test;

import cosc202.andie.FlipImage;

import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.awt.Color;


    
public class FlipImageTest {

    /**
     * Testing the expected output of a horizontal flip of an image
     * Grabs the RGB values of pixels in image and checks they match the expected output after image flip
     */
    @Test
    public void flipImageHorizontallyTest() {
        BufferedImage input = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        // First pixel rgb's
        input.setRGB(0, 0, Color.RED.getRGB());
        input.setRGB(0, 0, Color.GREEN.getRGB());
        input.setRGB(0, 0, Color.BLUE.getRGB());
        // Second pixel in top row
        input.setRGB(1, 0, Color.RED.getRGB());
        input.setRGB(1, 0, Color.GREEN.getRGB());
        input.setRGB(1, 0, Color.BLUE.getRGB());

        // Third pixel in top row
        input.setRGB(2, 0, Color.RED.getRGB());
        input.setRGB(2, 0, Color.GREEN.getRGB());
        input.setRGB(2, 0, Color.BLUE.getRGB());
        
        BufferedImage expectedOutput = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        // Check pixels match on an expected horizontally flip output
        expectedOutput.setRGB(0, 0, Color.RED.getRGB());
        expectedOutput.setRGB(0, 0, Color.GREEN.getRGB());
        expectedOutput.setRGB(0, 0, Color.BLUE.getRGB());

        expectedOutput.setRGB(1, 0, Color.RED.getRGB());
        expectedOutput.setRGB(1, 0, Color.GREEN.getRGB());
        expectedOutput.setRGB(1, 0, Color.BLUE.getRGB());

        expectedOutput.setRGB(2, 0, Color.RED.getRGB());
        expectedOutput.setRGB(2, 0, Color.GREEN.getRGB());
        expectedOutput.setRGB(2, 0, Color.BLUE.getRGB());
        
        // Flip image horizontally
        FlipImage flip = new FlipImage(false);
        BufferedImage output = flip.apply(input);
        
        // Check width and height stay same
        assertEquals(expectedOutput.getWidth(), output.getWidth());
        assertEquals(expectedOutput.getHeight(), output.getHeight());
        // Loop through every pixel
        for (int x = 0; x < expectedOutput.getWidth(); x++) {
            for (int y = 0; y < expectedOutput.getHeight(); y++) {
                // Compare every pixel on expectedOut to actual output
                assertEquals(expectedOutput.getRGB(x, y), output.getRGB(x, y));
            }
        }
    }

    /**
     * Testing the expected output of a vertical flip of an image
     * Grabs the RGB values of pixels in image and checks they match the expected output after image flip
     */
    @Test
    public void flipImageVerticallyTest() {
        BufferedImage input = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        // Check rgb's of first pixel in image
        input.setRGB(0, 0, Color.RED.getRGB());
        input.setRGB(0, 0, Color.GREEN.getRGB());
        input.setRGB(0, 0, Color.BLUE.getRGB());

        // Get second pixel in top row
        input.setRGB(1, 0, Color.RED.getRGB());
        input.setRGB(1, 0, Color.GREEN.getRGB());
        input.setRGB(1, 0, Color.BLUE.getRGB());

        // Get third pixel in top row
        input.setRGB(2, 0, Color.RED.getRGB());
        input.setRGB(2, 0, Color.GREEN.getRGB());
        input.setRGB(2, 0, Color.BLUE.getRGB());

        
        BufferedImage expectedOutput = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        // Check pixels match on an expected vertical flip output
        expectedOutput.setRGB(0, 1, Color.RED.getRGB());
        expectedOutput.setRGB(0, 1, Color.GREEN.getRGB());
        expectedOutput.setRGB(0, 1, Color.BLUE.getRGB());

        expectedOutput.setRGB(1, 1, Color.RED.getRGB());
        expectedOutput.setRGB(1, 1, Color.GREEN.getRGB());
        expectedOutput.setRGB(1, 1, Color.BLUE.getRGB());

        expectedOutput.setRGB(2, 1, Color.RED.getRGB());
        expectedOutput.setRGB(2, 1, Color.GREEN.getRGB());
        expectedOutput.setRGB(2, 1, Color.BLUE.getRGB());
        
        // Flip image vertically
        FlipImage flip = new FlipImage(true);
        BufferedImage output = flip.apply(input);
        
        // Check width and height stay same
        assertEquals(expectedOutput.getWidth(), output.getWidth());
        assertEquals(expectedOutput.getHeight(), output.getHeight());
        // Loop through every pixel
        for (int x = 0; x < expectedOutput.getWidth(); x++) {
            for (int y = 0; y < expectedOutput.getHeight(); y++) {
                // Compare every pixel on expectedOut to actual output
                assertEquals(expectedOutput.getRGB(x, y), output.getRGB(x, y));
            }
        }
    }
}
