package cosc202.andie;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * The InvertFilter class implements the ImageOperation interface and provides a method to invert the colors of an image.
 * Each pixel in the image is inverted by subtracting its red, green, and blue components from 255.
 * 
 * Note that some ideas were drawn from: https://www.geeksforgeeks.org/image-processing-in-java-colored-image-to-negative-image-conversion/
 * by Pratik Agarwal.
 * 
 * The class is highly adapted from the original implementation by Steven Mills.
 *
 * @author Hannah Srzich
 * @version 1.0
 */
public class InvertFilter implements ImageOperation, java.io.Serializable {

    /**
     * Constructs a new InvertFilter object.
     */
    public InvertFilter() {
    }

    /**
     * Applies the invert filter to the given image.
     *
     * @param image the image to apply the filter to
     * @return the modified image with inverted colors
     */
    public BufferedImage apply(BufferedImage image) {
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // Loop through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                // Get the red, green, and blue components of the pixel value
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // Invert the red, green, and blue components
                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;

                // Set the new pixel value in the image
                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return image;
    }     
    
}
