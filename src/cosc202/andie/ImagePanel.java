package cosc202.andie;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 * 
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well
 * as zooming
 * in and out.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills edited by Luke Piper and Hannah Srzich
 * @version 1.0
 */
public class ImagePanel extends JPanel implements ActionListener{

    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /**
     * <p>
     * The zoom-level of the current view.
     * A scale of 1.0 represents actual size; 0.5 is zoomed out to half size; 1.5 is
     * zoomed in to one-and-a-half size; and so forth.
     * </p>
     * 
     * <p>
     * Note that the scale is internally represented as a multiplier, but externally
     * as a percentage.
     * </p>
     */
    private double scale;

    // Global variables for mouse region
    public static int x;
    public static int y;
    public static int width;
    public static int height;
    public static int startX;
    public static int startY;
    public static int endX;
    public static int endY;
    static int lineX1;
    static int lineY1;
    static int lineX2;
    static int lineY2;
    public static Rectangle region;

    // for the scaled and centred image
    public int scaledWidth = 0;
    public int scaledHeight = 0;
    public int topLeftX = 0;
    public int topLeftY = 0;

    // For animation of region
    private static final int DASH_PHASE_STEP = 5;
    private static final int DASH_DELAY_MS = 100; //50
    private float dashPhase = 0.0f;

    /**
     * <p>
     * Create a new ImagePanel. 
     * </p>
     * 
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {
        // for the animation of the dashes
        Timer timer = new Timer(DASH_DELAY_MS, this);
        timer.start();

        image = new EditableImage();
        scale = 1.0;
        // Mouse listener for mouse region detection
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Update the paint of the rectangle as the user drags the mouse
                // Change cursor back to default
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                // Grab x2 and y2
                endX = e.getX();
                endY = e.getY();
                // Mouse rectangle parts
                x = Math.min(startX, endX);
                y = Math.min(startY, endY);
                width = Math.abs(endX - startX);
                height = Math.abs(endY - startY);
                // Create region and draw it
                region = new Rectangle(x, y, width, height);
                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handlePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleRelease(e);
            }
        });
    }

    /**
     * Handles the mouse click event.
     *
     * @param e The MouseEvent object containing information about the event.
     */
    public void handleClick(MouseEvent e) {
        // Delete region if mouse clicked
        region = null;
        repaint();
    }

    /**
     * Handles the mouse press event.
     *
     * @param e The MouseEvent object containing information about the event.
     */
    public void handlePress(MouseEvent e) {
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        // Grab x and y of region
        startX = e.getX();
        startY = e.getY();
        // Do same for drawing line
        lineX1 = startX;
        lineY1 = startY;
    }

    /**
     * Handles the mouse release event.
     *
     * @param e The MouseEvent object containing information about the event.
     */
    public void handleRelease(MouseEvent e) {
        // Had to add this bit as it was repainting the original image
        // in the slider, color chooser event
        if (Andie.chooserOperating == false) {
            // Change cursor back to default
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            // Grab end point of region
            endX = e.getX();
            endY = e.getY();
            lineX2 = endX;
            lineY2 = endY;
            // Solve for x and y of region
            x = Math.min(startX, endX);
            y = Math.min(startY, endY);
            // Solve for width and height of region
            width = Math.abs(endX - startX);
            height = Math.abs(endY - startY);
            // Create region and draw it
            region = new Rectangle(x, y, width, height);
            repaint();
        }
    }

    /**
     * <p>
     * Get the currently displayed image.
     * </p>
     *
     * @return the image currently displayed.
     */
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Set the currently displayed image
     * </p>
     *
     * returns the image currently displayed.
     */
    public void setImage(EditableImage image) {
        this.image = image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     * 
     * 
     * <p>
     * 
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * </p>
     * 
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100 * scale;
    }

    /**
     * <p>
     * Get the current scale.
     * </p>
     * 
     * @return The current scale.
     */
    public double getScale() {
        return scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     * 
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * 
     * The zoom level is restricted to the range [50, 200].
     * </p>
     * 
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 50) {
            zoomPercent = 50;
        }
        if (zoomPercent > 200) {
            zoomPercent = 200;
        }
        this.scale = zoomPercent / 100;
    }

    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     * 
     * 
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a
     * default size if no image is present.
     * </p>
     * 
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension(image.getCurrentImage().getWidth(), image.getCurrentImage().getHeight());
        } else {
            return new Dimension(450, 450);
        }
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     * 
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image.hasImage()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scale, scale);

            // Get the scaled dimensions of the image
            this.scaledWidth = (int) (image.getCurrentImage().getWidth() * scale);
            this.scaledHeight = (int) (image.getCurrentImage().getHeight() * scale);

            // Calculate the scaled top-left coordinates
            this.topLeftX = (int) ((getWidth() - scaledWidth) / (2 * scale));
            this.topLeftY = (int) ((getHeight() - scaledHeight) / (2 * scale));

            // Draw the scaled image at the centered coordinates
            g2.drawImage(image.getCurrentImage(), topLeftX, topLeftY, null);

            // Call customer listener
            g2.dispose();
        }
        // If mouse region is present and image is open
        if (region != null && image.hasImage()) {
            // Create graphics
            Graphics2D g2 = (Graphics2D) g.create();
            // If the image is too light, change the colour of the region box to black
            g2.setColor(Color.WHITE);
            if(imageTooLight()){
                g2.setColor(Color.BLACK);
            }
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Set the dashed stroke with moving dashes
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5, new float[]{2}, dashPhase));

            // Draw rectangle
            g2.drawRect(region.x, region.y, region.width, region.height);
            g2.dispose();
        }
    }

    /**
     * A method that is called when the timer fires an event.
     * It repaints the region with a new dash phase to create the animation effect.
     * 
     * @param e the action event, timer in this case.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(Andie.chooserOperating == false){
            // Update the dash phase to create the animation effect
            dashPhase += DASH_PHASE_STEP;
            repaint();
        }
    }

    /**
     * Checks if the current image is too light by counting the number of white or almost white pixels.
     * 
     * @return true if the image is too light, false otherwise.
     */
    private boolean imageTooLight() {
        BufferedImage ourImage = image.getCurrentImage();

        int width = ourImage.getWidth();
        int height = ourImage.getHeight();
        int whitePixelCount = 0;
        int totalPixelCount = width * height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(ourImage.getRGB(x, y));
                int red = pixelColor.getRed();
                int green = pixelColor.getGreen();
                int blue = pixelColor.getBlue();

                // Check if the pixel is white or almost white
                if (red >= 240 && green >= 240 && blue >= 240) {
                    whitePixelCount++;
                }
            }
        }

        double whitePixelRatio = (double) whitePixelCount / totalPixelCount;

        // Threshold value to determine if the image is too white
        double threshold = 0.15;

        // If the ratio of white pixels is greater than the threshold, the image is too light
        // Return true if the image is too light
        return whitePixelRatio >= threshold;
    }
}