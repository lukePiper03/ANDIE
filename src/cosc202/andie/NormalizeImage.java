package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;

/**
 * The NormalizeImage class provides methods to deal with negative pixel values in an image.
 * It includes operations such as rescaling the pixel values to a specified range, adjusting the mid-value of negative pixels, and shifting pixel values.
 * 
 * The class is highly adapted from the original implementation by Steven Mills.
 * 
 * @author Hannah Srzich
 * @version 1.0
 */
public class NormalizeImage {

    /**
     * Constructs a new NormalizeImage object.
     */
    public NormalizeImage() {}

    /**
     * Rescales the pixel values of the given image to the range [0, 255].
     *
     * @param image the image to rescale
     * @return the rescaled image
     */
    public static BufferedImage rescaleImage(BufferedImage image) {
        // Define the minimum and maximum pixel values
        double min = 0;
        double max = 255;
        
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Create a new BufferedImage to store the rescaled image
        BufferedImage rescaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // Loop through each pixel in the image
        for (int y = 0; y < height; y++) {
           for (int x = 0; x < width; x++) {
              // Get the pixel value at (x, y)
              int pixel = image.getRGB(x, y);
              
              // Extract the red, green, and blue components from the pixel value
              int alpha = (pixel >> 24) & 0xff; // new
              int red = (pixel >> 16) & 0xff;
              int green = (pixel >> 8) & 0xff;
              int blue = pixel & 0xff;
              
              // Rescale each component to the new range
              double rescaledAlpha = (alpha - min) / (max - min) * 255; // new 
              double rescaledRed = (red - min) / (max - min) * 255;
              double rescaledGreen = (green - min) / (max - min) * 255;
              double rescaledBlue = (blue - min) / (max - min) * 255;

              // Convert the rescaled component values back to integers
              int rescaledAlphaInt = (int) Math.round(rescaledAlpha); // new 
              int rescaledRedInt = (int) Math.round(rescaledRed);
              int rescaledGreenInt = (int) Math.round(rescaledGreen);
              int rescaledBlueInt = (int) Math.round(rescaledBlue);
              
              // Set the rescaled pixel value in the new image
              //int rescaledPixel = (rescaledRedInt << 16) | (rescaledGreenInt << 8) | rescaledBlueInt;
              int rescaledPixel = (rescaledAlphaInt << 24) | (rescaledRedInt << 16) | (rescaledGreenInt << 8) | rescaledBlueInt; // new 
              rescaledImage.setRGB(x, y, rescaledPixel);
           }
        }
        
        return rescaledImage;
     }

    /**
     * Adjusts the mid-value of negative pixels in the given image by adding 127 to their RGB values.
     *
     * @param image the image to adjust
     * @return the adjusted image
     */
    public static BufferedImage midValueNegativePixels(BufferedImage image){
        
        // get the image's width and height
        int width = image.getWidth();
        int height = image.getHeight();

        // loop through every pixel in the image and add 127 to its ARGB values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get the current pixel's ARGB value
                int pixel = image.getRGB(x, y);

                // extract the alpha, red, green, and blue values from the pixel
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // add 127 to each of the ARGB values (except for alpha)
                red = red/2 + 127;
                green = green/2 + 127;
                blue = blue/2 + 127;
                /* 
                red = Math.min(red + 127, 255);
                green = Math.min(green + 127, 255);
                blue = Math.min(blue + 127, 255);
                */

                // create a new pixel value with the updated ARGB values
                int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;

                // set the current pixel's value to the new value in the image
                image.setRGB(x, y, newPixel);
            }
        }

        return image;
    }

    /**
     * Shifts the pixel values of the given image by adding 128 to their RGB values based on neighboring pixels.
     *
     * @param image the image to shift pixel values
     * @return the image with shifted pixel values
     */
    public static BufferedImage shiftPixelValue(BufferedImage image){
         int width = image.getWidth();
         int height = image.getHeight();
         BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
         // Apply the kernel to each pixel
         for (int y = 0; y < height; y++) {
           for (int x = 0; x < width; x++) {
             int sumR = 0;
             int sumG = 0;
             int sumB = 0;
     
             // Apply the kernel to the neighboring pixels
             for (int i = 0; i < 3; i++) {
               for (int j = 0; j < 3; j++) {
                 int dx = x + j - 1;
                 int dy = y + i - 1;
     
                 if (dx < 0 || dx >= width || dy < 0 || dy >= height) {
                   continue;
                 }
     
                 Color c = new Color(image.getRGB(dx, dy));
                 sumR += c.getRed();
                 sumG += c.getGreen();
                 sumB += c.getBlue();
               }
             }
             // Calculate the new pixel color values
             int newR = Math.min(Math.max(sumR + 128, 0), 255);
             int newG = Math.min(Math.max(sumG + 128, 0), 255);
             int newB = Math.min(Math.max(sumB + 128, 0), 255);
     
             // Set the new pixel color values
             Color newColor = new Color(newR, newG, newB);
             newImage.setRGB(x, y, newColor.getRGB());
           }
         }

         return newImage;
    }
    
}
