package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * The DrawOutline class represents an image operation that draws an outline shape on an image.
 * It implements the ImageOperation interface.
 */
public class DrawOutline implements ImageOperation {

    private Color colour;
    private int x1;
    private int y1;
    private int width;
    private int height;
    private String shape = null;

    /**
     * Constructs a new DrawOutline object with the specified shape, color, and coordinates.
     *
     * @param shape  The shape of the outline ("square" or "oval").
     * @param colour The color of the outline.
     * @param x1     The x-coordinate of the starting point of the outline.
     * @param y1     The y-coordinate of the starting point of the outline.
     * @param width  The width of the outline.
     * @param height The height of the outline.
     */
    public DrawOutline(String shape, Color colour, int x1, int y1, int width, int height) {
        this.shape = shape;
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
    }

    /**
     * Applies the draw outline operation to the input image.
     *
     * @param input The input image to apply the operation on.
     * @return The modified image with the outline drawn on it.
     */
    public BufferedImage apply(BufferedImage input) {
        double scale = Andie.imagePanel.getScale();
        // get the unscaled and centered region params of the rectangle
        // Calculate the unscaled coordinates and dimensions of the region
        int x1Scaled = (int) ((x1 - Andie.imagePanel.topLeftX) / scale);
        int y1Scaled = (int) ((y1 - Andie.imagePanel.topLeftY) / scale);
        int widthScaled = (int) (width / scale);
        int heightScaled = (int) (height / scale);

        // If the whole region is outside of the image
        if (x1Scaled > input.getWidth() || y1Scaled > input.getHeight() || x1Scaled + widthScaled < input.getMinX() || y1Scaled + heightScaled < input.getMinY()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            JOptionPane.showMessageDialog(null, Andie.bundle.getString("cropRegionErrorMessage")+ "       ", Andie.bundle.getString("DrawShapesError"),
                    JOptionPane.ERROR_MESSAGE, Andie.icon);
            return input;
        }

        if (this.shape.equals("square")) {
            Graphics2D g2d = input.createGraphics();
            g2d.setColor(colour);
            g2d.drawRect(x1Scaled, y1Scaled, widthScaled, heightScaled);
            g2d.dispose();
            ImagePanel.region = null;
            return input;
        } else if (this.shape.equals("oval")) {
            Graphics2D g2d = input.createGraphics();
            // smooths edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // sets color
            g2d.setColor(colour);
            // draws outline
            g2d.drawOval(x1Scaled, y1Scaled, widthScaled, heightScaled);
            g2d.dispose();
            ImagePanel.region = null;
            return input;
        }
        return input;
    }
}
