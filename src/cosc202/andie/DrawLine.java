package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * The DrawLine class represents an image operation that draws a line on an image.
 * It implements the ImageOperation interface.
 */
public class DrawLine implements ImageOperation {

    public Color colour;
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    /**
     * Constructs a new DrawLine object with the specified color and coordinates.
     *
     * @param colour The color of the line.
     * @param x1     The x-coordinate of the starting point of the line.
     * @param y1     The y-coordinate of the starting point of the line.
     * @param x2     The x-coordinate of the ending point of the line.
     * @param y2     The y-coordinate of the ending point of the line.
     */
    public DrawLine(Color colour, int x1, int y1, int x2, int y2) {
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Draws a line on the input image with the specified coordinates and color.
     *
     * @param input The input image to draw the line on.
     * @param x1    The x-coordinate of the starting point of the line.
     * @param y1    The y-coordinate of the starting point of the line.
     * @param x2    The x-coordinate of the ending point of the line.
     * @param y2    The y-coordinate of the ending point of the line.
     * @return The modified image with the line drawn on it.
     */
    public BufferedImage drawLine(BufferedImage input, int x1, int y1, int x2, int y2) {
        double scale = Andie.imagePanel.getScale();
        // get the unscaled and centred region params of the rectangle
        // Calculate the unscaled coordinates and dimensions of the region
        int x1Scaled = (int) ((x1 - Andie.imagePanel.topLeftX) / scale);
        int y1Scaled = (int) ((y1 - Andie.imagePanel.topLeftY) / scale);
        int x2Scaled = (int) ((x2 - Andie.imagePanel.topLeftX) / scale);
        int y2Scaled = (int) ((y2 - Andie.imagePanel.topLeftY) / scale);

        boolean xSwapped = false;
        boolean ySwapped = false;
        // Swap so that the below conditions for drawing an image still hold
        if(x1Scaled > x2Scaled){
            int temp = x1Scaled;
            x1Scaled = x2Scaled;
            x2Scaled = temp;
            xSwapped = true;
        }
        if(y1Scaled > y2Scaled){
            int temp = y1Scaled;
            y1Scaled = y2Scaled;
            y2Scaled = temp;
            ySwapped = true;
        }

        // If the whole region is outside of the image
        if (x1Scaled > input.getWidth() || y1Scaled > input.getHeight() || x1Scaled + Math.abs(x2Scaled - x1Scaled) < input.getMinX() || y1Scaled + Math.abs(y2Scaled - y1Scaled) < input.getMinY()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            JOptionPane.showMessageDialog(null, Andie.bundle.getString("cropRegionErrorMessage")+ "       ", Andie.bundle.getString("DrawShapesError"),
                    JOptionPane.ERROR_MESSAGE, Andie.icon);
            return input;
        }
        // if swapped swap back
        if(xSwapped){
            int temp = x1Scaled;
            x1Scaled = x2Scaled;
            x2Scaled = temp;
        }
        if(ySwapped){
            int temp = y1Scaled;
            y1Scaled = y2Scaled;
            y2Scaled = temp;
        }

        Graphics2D g2d = input.createGraphics();
        // smooths edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(colour);

        g2d.drawLine(x1Scaled, y1Scaled, x2Scaled, y2Scaled);
        g2d.dispose();
        ImagePanel.region = null;
        return input;
    }

    /**
     * Applies the draw line operation to the input image.
     *
     * @param input The input image to apply the operation on.
     * @return The modified image with the line drawn on it.
     */
    public BufferedImage apply(BufferedImage input) {
        return drawLine(input, x1, y1, x2, y2);
    }

}

