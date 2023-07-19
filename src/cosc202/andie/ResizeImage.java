package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Resize the opened image to the specified size user gives in OptionPane
 */
public class ResizeImage implements ImageOperation, java.io.Serializable{
    
    double scale;

    /**
     * ResizeImage Constructor
     * @param scale
     */
    public ResizeImage(int scale) {
        this.scale = scale;
    }

    /**
     * Create a new image that is resized to the specified size
     * @param input
     * @return new resized buffered image
     */
    public BufferedImage apply(BufferedImage input) {
        // Get new width and height
        double newWidth = input.getWidth() * (scale / 100);
        double newHeight = input.getHeight() * (scale / 100);
        Image newImage;
        // If resizing to bigger instance, scale smooth
        if(newWidth > input.getWidth() || newWidth == input.getWidth()) {
            newImage = input.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_SMOOTH);
        } else {
            // If going to smaller image, scale average pixels
            newImage = input.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_AREA_AVERAGING);
        }
        
        // New clone of input image
        BufferedImage resizedImage = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_ARGB);

        // Grab graphics and pixel of input image to new clone
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(newImage, 0, 0, null);
        g2d.dispose();

        // Return resized image
        return resizedImage;
    }
}
