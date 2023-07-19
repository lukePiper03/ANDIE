package test.cosc202.andie;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;

import org.junit.Assert;
import cosc202.andie.*;

/**
 * Junit tests to see if the output ARGB value of a single pixel mathces the expected output ARGB value
 * and to test that an error is thrown if we try to apply a brightness opperation to a null image.
 * 
 * @author Dante Vannini
 * @version 1.0
 */

public class BrightnessTest {
    private Brightness brightness;
    private BufferedImage inputImage; 
    

    /**test to see if applying the brightness/contrast filter to an image containing one pixel correctly
     * changes the argb value of the pixel. in this case, the pixel should not change, as its argb values are: 255, 0, 0, 255
     * and i ran these values through the formula using my calculator and found that they should not change.
     */
    @Test
    public void testApply(){
        brightness = new Brightness(20,10);
        inputImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        inputImage.setRGB(0, 0, 0xFF0000FF);
        Brightness brightness = new Brightness(20,10);
        brightness.apply(inputImage);
        int expectedOut = 0xFF0000FF; // the argb value of the pixel should remain unchanged
        int outputPixel = inputImage.getRGB(0, 0); //the argb value of the pixel after brightness and contrast was applied
        assertEquals(expectedOut, outputPixel); //compares expected and actual argb values
    }

    /**
     * Test to make sure that an illegalArgument exception is thrown when trying to apply brightess to a null image. 
     * Code addapted from demo 2 on https://howtodoinjava.com/junit5/expected-exception-example/
     */
    @Test
    public void nullImageTest(){
        brightness = new Brightness(10, 10);
        inputImage = null;
        NullPointerException thrown  = Assert.assertThrows(NullPointerException.class, () ->{
            brightness.apply(inputImage);
        });
        assertNotNull(thrown);
        
    }

}
