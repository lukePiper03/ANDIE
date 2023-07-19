package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;

/**
 * The HueFilter class implements the ImageOperation interface and represents a filter that modifies the hue of an image
 * by replacing the hue value of each pixel with a specified hue value.
 * 
 * Note that some of the code in this class was derived and adapted from Stack Overflow: (CC BY-SA) license. 
 * https://stackoverflow.com/questions/4248104/applying-a-tint-to-an-image-in-java
 * and snippets fro HSV in https://pastebin.com/gQc9VTea 
 * 
 * @author Hannah Srzich, highly adapted from the MeanFilter class by Steven Mills
 * @version 1.0
 */
public class HueFilter implements ImageOperation, java.io.Serializable {  

    Color color = null; // Must be set before apply() is called

    /**
     * Constructs a new HueFilter with the specified color.
     *
     * @param color the color to set the hue
     */
    public HueFilter(Color color){
        this.color = color;
    }

    /**
     * Applies the hue filter to the given image by modifying the hue value of each pixel.
     * It converts the given color to HSV format, extracts the hue value, and iterates over each pixel in the image.
     * For each pixel, it extracts the red, green, and blue components, converts the RGB color to HSB format,
     * replaces the hue value with the specified hue, converts the color back to RGB format,
     * and sets the pixel with the new RGB value.
     *
     * @param image the image to apply the filter to
     * @return the modified image with the hue filter applied
     */
    public BufferedImage apply(BufferedImage image) {
        // Convert the given color to HSB format
        float[] hsv = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        // Extract the hue value from the array
        float hue = hsv[0];

        // Iterate over each pixel in the image
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Get the RGB value of the pixel at (x, y)
                int rgb = image.getRGB(x, y);
                // Extract the red, green, and blue components from the RGB value
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
    
                // Convert the RGB color to HSB format
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                // Extract the brightness value from the HSB array
                float brightness = hsb[2];
                // Convert the color to RGB format using the given hue and the extracted saturation and brightness values
                int rgb2 = Color.HSBtoRGB(hue, hsb[1], brightness);
    
                // Set the pixel at (x, y) to the new RGB value
                image.setRGB(x, y, rgb2);
            }
        }
    
        // Return the modified image
        return image;
    }
}
