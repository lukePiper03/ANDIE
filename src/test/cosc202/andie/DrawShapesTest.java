package test.cosc202.andie;

import org.junit.Test;

import cosc202.andie.DrawSquare;
import cosc202.andie.ImagePanel;
import cosc202.andie.DrawActions;
import cosc202.andie.DrawOval;
import cosc202.andie.DrawOutline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The DrawShapesTest class is a JUnit test class for testing the
 * apply() method of the DrawSquare class and the DrawOval class.
 * 
 * @author Shape Test methods by Lia Walker, and Outline methods by Eszter
 *         Scarlett-Herbert
 * @version 1.0
 *          note: the idea for how to check the pixels is adapted from ChatGPT,
 *          and we have calculated the correct pixels ourselves
 */

public class DrawShapesTest {

    public DrawSquare square;
    public DrawOval oval;
    public BufferedImage inputImage;
    public BufferedImage outputImage;

    /**
     * This method is used to test the apply() method of the DrawSquare class when
     * filling a square.
     * It creates a test image with known colour and dimensions and then checks
     * the pixels that make up the square are the correct colour.
     */
    @Test
    public void DrawSquareTest() {
        // An ImagePanel instance is created. This is necessary because the DrawOutline
        // class relies on the Andie.imagePanel field from the cosc202.andie.Andie
        // class. By assigning the imagePanel to Andie.imagePanel, it ensures that the
        // test is using the correct environment
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;

        // creates a square with a known colour, and size to add onto the buffered image
        // panel
        Color colour = Color.RED;
        square = new DrawSquare(colour, 20, 20, 40, 40);
        inputImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        outputImage = square.apply(inputImage);

        // this checks that when applying the shape the image dimensions remain the
        // same, i.e that the feauture is non destructive
        assertEquals(100, outputImage.getWidth());
        assertEquals(100, outputImage.getHeight());

        // this will now loop through the blank buffered image, pixel by pixel aand do
        // two checks, firstly checking the pixels in the square are the correct colour,
        // and then the pizels outside the square are black (eqal to zer0)
        for (int x = 0; x < outputImage.getWidth(); x++) {
            for (int y = 0; y < outputImage.getHeight(); y++) {
                if (x >= 20 && x < 20 + 40 && y >= 20 && y < 20 + 40) {
                    assertEquals(colour.getRGB(), outputImage.getRGB(x, y));
                } else {
                    assertEquals(0, outputImage.getRGB(x, y));
                }
            }
        }

    }

    /**
     * This method is used to test the apply() method of the DrawOval class when
     * filling an oval.
     * It creates a test image with known colour and dimensions and then checks
     * the pixels that make up the oval are the correct colour.
     */
    @Test
    public void DrawOvalTest() {
        // An ImagePanel instance is created. This is necessary because the DrawOutline
        // class relies on the Andie.imagePanel field from the cosc202.andie.Andie
        // class. By assigning the imagePanel to Andie.imagePanel, it ensures that the
        // test is using the correct environment
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;

        // creates an oval with a known colour, and size to add onto the buffered image
        // panel
        Color colour = Color.RED;
        oval = new DrawOval(colour, 20, 20, 5, 10);
        inputImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        outputImage = oval.apply(inputImage);

        // this checks that when applying the shape the image dimensions remain the
        // same, i.e that the feauture is non destructive
        assertEquals(100, outputImage.getWidth());
        assertEquals(100, outputImage.getHeight());

        // this will now loop through the blank buffered image, pixel by pixel aand do
        // two checks, firstly checking the pixels in the oval are the correct colour,
        // and then the pizels outside the square are black (eqal to zer0)
        for (int x = 0; x < outputImage.getWidth(); x++) {
            for (int y = 0; y < outputImage.getHeight(); y++) {
                // checking if the pixels in the oval are the same colour as the set colour
                if (((x == 21) || (x == 23)) && (21 < y) && (y < 28)) {
                    assertEquals(colour.getRGB(), outputImage.getRGB(x, y));
                } else if (x == 22 && (20 < y) && (y < 29)) {
                    assertEquals(colour.getRGB(), outputImage.getRGB(x, y));
                   
                    // next three staments will check the region of the square image, for the different oval colors
                    // because of the rounding in the corners of the oval the pixels will be blended colors
                    // so for the test to pass it needs to check for these as well as the red
                } else if (((x == 20) || (x == 24)) && ((y > 19) || (y < 30))) {
                    assertNotEquals(colour.getRGB(), outputImage.getRGB(x, y));
                } else if (((x == 21) || (x == 23)) && ((y == 20) || (y == 21) || (y == 28) || (y == 29))) {
                    assertNotEquals(colour.getRGB(), outputImage.getRGB(x, y));
                } else if (((x == 22)) && ((y == 20) || (y == 29))) {
                    assertNotEquals(colour.getRGB(), outputImage.getRGB(x, y));
            
                // then perform the zero check    
                } else {
                    assertEquals(0, outputImage.getRGB(x, y));
                }
            }
        }

    }

    /**
     * This method is used to test the apply() method of the DrawSquare class when
     * drawing an outline.
     * It creates a test image with known colour and dimensions and then checks
     * the pixels that make up the square outline are the correct colour.
     */
    @Test
    public void DrawSquareOutlineTest() {
        // An ImagePanel instance is created. This is necessary because the DrawOutline
        // class relies on the Andie.imagePanel field from the cosc202.andie.Andie
        // class. By assigning the imagePanel to Andie.imagePanel, it ensures that the
        // test is using the correct environment
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;
        DrawActions.isOutline = true;

        // a Color object is created representing the color of the square outline
        // then an instance of DrawOutline, specifying the shape, color, coordinates,
        // width, and height are created
        // finally an input image is created using BufferedImage with a size of 5x5
        // pixels and the square outline is applied
        Color color = Color.RED;
        DrawOutline squareOutline = new DrawOutline("square", color, 1, 1, 2, 2);
        BufferedImage inputImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
        BufferedImage outputImage = squareOutline.apply(inputImage);

        // this checks that the image height and width do not change when the shape is
        // added
        assertEquals(5, outputImage.getWidth());
        assertEquals(5, outputImage.getHeight());

        // this defines an array of values of what the outline should be
        int[] expectedValues = {
                0, 0, 0, 0, 0,
                0, -65536, -65536, -65536, 0,
                0, -65536, 0, -65536, 0,
                0, -65536, -65536, -65536, 0,
                0, 0, 0, 0, 0
        };

        // the expected values for each pixel are compared to one another to check they
        // are the correct values
        int index = 0;
        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                assertEquals(expectedValues[index++], outputImage.getRGB(x, y));
            }
        }
    }

    /**
     * This method is used to test the apply() method of the DrawOval class when
     * drawing an outline.
     * It creates a test image with known colour and dimensions and then checks
     * the pixels that make up the square outline are the correct colour.
     */
    @Test
    public void testDrawOvalOutline() {
        // An ImagePanel instance is created. This is necessary because the DrawOutline
        // class relies on the Andie.imagePanel field from the cosc202.andie.Andie
        // class. By assigning the imagePanel to Andie.imagePanel, it ensures that the
        // test is using the correct environment
        ImagePanel imagePanel = new ImagePanel();
        cosc202.andie.Andie.imagePanel = imagePanel;
        DrawActions.isOutline = true;

        // a Color object is created representing the color of the oval outline
        // then an instance of DrawOutline, specifying the shape, color, coordinates,
        // width, and height are created
        // finally an input image is created using BufferedImage with a size of 5x5
        // pixels and the oval outline is applied
        Color color = Color.BLUE;
        DrawOutline ovalOutline = new DrawOutline("oval", color, 1, 1, 3, 2);
        BufferedImage inputImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
        BufferedImage outputImage = ovalOutline.apply(inputImage);

        // Assertions to validate the dimensions of the output image
        assertEquals(5, outputImage.getWidth());
        assertEquals(5, outputImage.getHeight());

        // an array of expected values is craeted for each of the pixels
        // this is slightly different to the square due to the rounding of the oval
        int[] expectedValues = {
                0, 0, 0, 0, 0,
                0, -1744830209, -100663041, -771751681, 1358954751,
                0, -150994689, 587202815, 2097152255, -285212417,
                0, -1744830209, -100663041, -771751681, 1358954751,
                0, 0, 0, 0, 0
        };

        // the expected values for each pixel are compared to one another to check they
        // are the correct values
        int index = 0;
        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                assertEquals(expectedValues[index++], outputImage.getRGB(x, y));
            }
        }
    }
}