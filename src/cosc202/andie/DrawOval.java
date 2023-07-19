package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * The DrawOval class represents an image operation that draws an oval shape on an image.
 * It implements the ImageOperation interface.
 */
public class DrawOval implements ImageOperation {

    private Color colour;
    private int x1;
    private int y1;
    private int width;
    private int height;

    /**
     * Constructs a new DrawOval object with the specified color and coordinates.
     *
     * @param colour The color of the oval.
     * @param x1     The x-coordinate of the top-left corner of the bounding rectangle of the oval.
     * @param y1     The y-coordinate of the top-left corner of the bounding rectangle of the oval.
     * @param width  The width of the bounding rectangle of the oval.
     * @param height The height of the bounding rectangle of the oval.
     */
    public DrawOval(Color colour, int x1, int y1, int width, int height) {
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
    }

    /**
     * Applies the draw oval operation to the input image.
     *
     * @param input The input image to apply the operation on.
     * @return The modified image with the oval drawn on it.
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

        Graphics2D g2d = input.createGraphics();
        // smooths edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // sets color
        g2d.setColor(colour);
        // draws oval
        g2d.fillOval(x1Scaled, y1Scaled, widthScaled, heightScaled);
        g2d.dispose();
        ImagePanel.region = null;
        return input;
    }
}


