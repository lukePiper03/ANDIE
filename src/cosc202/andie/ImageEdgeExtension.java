package cosc202.andie;

import java.awt.image.*;

/**
 * The ImageEdgeExtension class provides methods for dealing with edge cases when applying convolution filters to images.
 * It includes methods to extend an image by adding border pixels, as well as methods to crop an image by removing border pixels.
 * The extension and cropping can be performed by specifying the number of pixels to add or remove on each side of the image.
 * 
 * @author Hannah Srzich
 * @version 1.0
 */
public class ImageEdgeExtension {
    
    /**
     * Constructs a new ImageEdgeExtension.
     */
    public ImageEdgeExtension() {}

    /**
     * Extends the given image by adding three pixels on each side.
     * The pixels outside the original image are set to the color of the closest edge pixel.
     *
     * @param image the image to extend
     * @return the extended image
     */
    public static BufferedImage extendImage(BufferedImage image) {
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();
    
        // Create a new BufferedImage with the extended size
        BufferedImage extendedImage = new BufferedImage(width + 6, height + 6, BufferedImage.TYPE_INT_RGB);
    
        // Set the pixels in the extended image
        for (int y = 0; y < height + 6; y++) {
            for (int x = 0; x < width + 6; x++) {
                int pixel;
                if (x < 3 || y < 3 || x >= width + 3 || y >= height + 3) {
                    // If the pixel is outside the original image, set it to the color of the closest edge pixel
                    int closestX = Math.min(Math.max(x - 3, 0), width - 1);
                    int closestY = Math.min(Math.max(y - 3, 0), height - 1);
                    pixel = image.getRGB(closestX, closestY);
                } else {
                    // Otherwise, copy the pixel from the original image
                    pixel = image.getRGB(x - 3, y - 3);
                }
                extendedImage.setRGB(x, y, pixel);
            }
        }
    
        return extendedImage;
    }

    /**
     * Crops the given image by removing three pixels on each side.
     *
     * @param image the image to crop
     * @return the cropped image
     */
    public static BufferedImage cropImage(BufferedImage image) {
        // Get the width and height of the original image
        int width = image.getWidth();
        int height = image.getHeight();
    
        // Create a new BufferedImage to store the cropped image
        BufferedImage croppedImage = new BufferedImage(width - 6, height - 6, BufferedImage.TYPE_INT_RGB);
    
        // Loop through each pixel in the cropped image
        for (int y = 3; y < height - 3; y++) {
            for (int x = 3; x < width - 3; x++) {
                // Get the pixel value at (x, y)
                int pixel = image.getRGB(x, y);
    
                // Set the pixel value in the cropped image
                croppedImage.setRGB(x - 3, y - 3, pixel);
            }
        }
    
        return croppedImage;
    }

    /**
     * Extends the given image by adding n pixels on each side.
     * The pixels outside the original image are set to the color of the closest edge pixel.
     *
     * @param image the image to extend
     * @param n the number of pixels to add on each side
     * @return the extended image
     */
    public static BufferedImage extendImage(BufferedImage image, int n) {
        int width = image.getWidth();
        int height = image.getHeight();
        int extWidth = width + 2 * n;
        int extHeight = height + 2 * n;
        
        BufferedImage extendedImage = new BufferedImage(extWidth, extHeight, image.getType());
        
        // Color the border pixels based on the original edge pixels
        for (int y = 0; y < extHeight; y++) {
            for (int x = 0; x < extWidth; x++) {
                if (x < n || y < n || x >= width + n || y >= height + n) {
                    // This is a border pixel
                    int originalX = Math.min(Math.max(x - n, 0), width - 1);
                    int originalY = Math.min(Math.max(y - n, 0), height - 1);
                    int color = image.getRGB(originalX, originalY);
                    extendedImage.setRGB(x, y, color);
                } else {
                    // This is not a border pixel, copy the pixel from the original image
                    int originalX = x - n;
                    int originalY = y - n;
                    int color = image.getRGB(originalX, originalY);
                    extendedImage.setRGB(x, y, color);
                }
            }
        }
        
        return extendedImage;
    }
    
    /**
     * Crops the given image by removing n pixels on each side.
     *
     * @param image the image to crop
     * @param n the number of pixels to remove on each side
     * @return the cropped image
     */
    public static BufferedImage cropImage(BufferedImage image, int n) {
        int width = image.getWidth();
        int height = image.getHeight();
        int croppedWidth = width - 2 * n;
        int croppedHeight = height - 2 * n;
    
        BufferedImage croppedImage = new BufferedImage(croppedWidth, croppedHeight, image.getType());
    
        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                int pixel = image.getRGB(x, y);
                croppedImage.setRGB(x - n, y - n, pixel);
            }
        }
    
        return croppedImage;
    }
    
}
