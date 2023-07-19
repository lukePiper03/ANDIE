package test.cosc202.andie;

import org.junit.Test;

import cosc202.andie.ResizeImage;

import java.awt.image.BufferedImage;
import static org.junit.Assert.*;

public class ResizeImageTest {

    /**
     * Testing the expected output of a 50% resize of an image
     * Makes a 2x2 image and resizes it to 1x1 image and checks width and height are correct as expected
     */
    @Test
    public void resizeImageSmallerTest() {
        // New resized image
        ResizeImage resizeImage = new ResizeImage(50);
        // New image
        BufferedImage input = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

        // Rotate the image
        BufferedImage output = resizeImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(1, output.getWidth());
        assertEquals(1, output.getHeight());
    }

    /**
     * Testing the expected output of a 75% resize of an image
     * Makes a 4x4 image and resizes it to 3x3 image and checks width and height are correct as expected
     */
    @Test
    public void resizeImageSmallerTest2() {
        ResizeImage resizeImage = new ResizeImage(75);
        BufferedImage input = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

        // Rotate the test image
        BufferedImage output = resizeImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(3, output.getWidth());
        assertEquals(3, output.getHeight());
    }

    /**
     * Testing the expected output of a 200% resize of an image
     * Makes a 2x2 image and resizes it to 4x4 image and checks width and height are correct as expected
     */
    @Test
    public void resizeImageBiggerTest() {
        ResizeImage resizeImage = new ResizeImage(200);
        BufferedImage input = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

        // Rotate the test image
        BufferedImage output = resizeImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(4, output.getWidth());
        assertEquals(4, output.getHeight());
    }


    /**
     * Testing the expected output of a 150% resize of an image
     * Makes a 2x2 image and resizes it to 3x3 image and checks width and height are correct as expected
     */
    @Test
    public void resizeImageBiggerTest2() {
        ResizeImage resizeImage = new ResizeImage(150);
        BufferedImage input = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

        // Rotate the test image
        BufferedImage output = resizeImage.apply(input);

        // Check that the rotated image has the correct dimensions
        assertEquals(3, output.getWidth());
        assertEquals(3, output.getHeight());
    }}
