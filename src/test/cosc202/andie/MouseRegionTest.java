package test.cosc202.andie;

import org.junit.Test;

import cosc202.andie.ImagePanel;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

/**
 * Testing the mouse region works properly inside the ImagePanel class and the
 * three different possible mouseEvents. Mouse pressed, released, and clicked
 */
public class MouseRegionTest {
    /**
     * Testing a mouse press event
     * Should get the startX and startY points of the press
     * 
     * @throws Exception
     */
    @Test
    public void mousePressTest() {
        // Create ImagePanel and MouseEvent
        ImagePanel imagePanel = new ImagePanel();
        MouseEvent pressEvent = new MouseEvent(imagePanel, MouseEvent.MOUSE_PRESSED, 0, 0, 50, 50, 0, false);
        // Call event
        imagePanel.handlePress(pressEvent);
        // Assert that the startX and startY are set to the correct values
        assertEquals(ImagePanel.startX, 50);
        assertEquals(ImagePanel.startY, 50);
        // Assert that the region is null
        assertNull(ImagePanel.region);
    }

    /**
     * Testing that when the mouse get released that the endX, endY, and region are
     * created and initialised
     * 
     * @throws Exception
     */
    @Test
    public void mouseReleasedTest() {
        // Create ImagePanel and MouseEvent
        ImagePanel imagePanel = new ImagePanel();
        MouseEvent releaseEvent = new MouseEvent(imagePanel, MouseEvent.MOUSE_RELEASED, 0, 0, 100, 100, 0, false);
        // Call event
        imagePanel.handleRelease(releaseEvent);
        // Assert that the endX, endY, and region are created and initialised
        assertEquals(ImagePanel.endX, 100);
        assertEquals(ImagePanel.endY, 100);
        assertNotNull(ImagePanel.region);
    }

    /**
     * Testing that when the user clicks a mouse that the mouse region is null
     * 
     * @throws Exception
     */
    @Test
    public void mouseClickedTest() {
        // Create ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        // Set region of imagePanel
        ImagePanel.region = new Rectangle(0, 0, 100, 100);
        // Assert that the region is present
        assertNotNull(ImagePanel.region);
        // Create MouseEvent
        MouseEvent clickEvent = new MouseEvent(imagePanel, MouseEvent.MOUSE_CLICKED, 0, 0, 100, 100, 0, false);
        // Call event
        imagePanel.handleClick(clickEvent);
        // Show that mouse click kills the region to null
        assertNull(ImagePanel.region);
    }
}
