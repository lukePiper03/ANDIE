package test.cosc202.andie;

import static org.junit.Assert.*;

import org.junit.Test;

import cosc202.andie.ImagePanel;

/**
 * The ImagePanelTest class is a JUnit test class for testing the various()
 * methods of the zooms int the ImagePanels class.
 * 
 * @author Stephen Mills, adapated by Eszter Scarlett-Herbert
 * @version 2.0
 *          note:the getZoomInitialValue and getZoomAfterSetValue are taken from
 *          the lab book. Credit to
 *          https://cosc202.cspages.otago.ac.nz/lab-book/COSC202LabBook.pdf
 */
public class ImagePanelTest {
    /*test is to verify the expected behavior of the getZoom() method in the ImagePanel class. 
     * if the value is not set to 100 then the test will fail
    */
    @Test
    public void getZoomInitialValue() {
        ImagePanel testpanel = new ImagePanel();
        assertTrue(testpanel.getZoom()== 100.00);
    }

     /*test is to verify the expected behavior of the setZoom() method in the ImagePanel class. 
     * if the value is not changed from the initial value, or is too low then the test will fail
     * as the code is not behaving as expected
    */
    @Test
    public void getZoomAfterSetValue() {
        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setZoom(0.00);
        assertFalse(imagePanel.getZoom() == 100.00);
        assertTrue(imagePanel.getZoom() >= 50.00);

    }

}
