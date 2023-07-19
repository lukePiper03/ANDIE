package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * CropImage class to crop an image based on the current mouse region by user
 * User must first select a region on the image to crop
 */
public class CropImage implements ImageOperation, java.io.Serializable {
    // Boolean to check if flip is vertical or horizontal
    Rectangle region;

    /**
     * CropImage Constructor
     * 
     * @param r rectangle
     */
    public CropImage(Rectangle r) {
        region = r;
        ImagePanel.region = null;
    }

    /**
     * Apply the operation to the image to crop the image based on the mouse region
     * 
     * @param input
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        
        double scale = Andie.imagePanel.getScale();
        
        // get the unscaled and centered region params of the rectangle
        // Calculate the unscaled coordinates and dimensions of the region
        double x0 = ((region.getX() - Andie.imagePanel.topLeftX) / scale);
        double y0 = ((region.getY() - Andie.imagePanel.topLeftY) / scale);
        double width0 = (region.getWidth() / scale);
        double height0 = (region.getHeight() / scale);

        int x = (int) x0;
        int y = (int) y0;
        int width = (int) width0;
        int height = (int) height0;

        // If the whole region is outside of the image
        if (x > input.getWidth() || y > input.getHeight() || x + width < input.getMinX() || y + height < input.getMinY()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            JOptionPane.showMessageDialog(null, Andie.bundle.getString("cropRegionErrorMessage")+ "       ", Andie.bundle.getString("cropError"),
                    JOptionPane.ERROR_MESSAGE, Andie.icon);
            return input;
        }

        // Contain crop to image bounds if it currently exceeds bounds
        x = Math.max(x, 0);
        y = Math.max(y, 0);
        width = Math.min(width, input.getWidth() - x);
        height = Math.min(height, input.getHeight() - y);

        // Crop image
        BufferedImage cropImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = cropImage.createGraphics();
        graphics.drawImage(input.getSubimage(x, y, width, height), 0, 0, null);
        graphics.dispose();
        return cropImage;
    }
}