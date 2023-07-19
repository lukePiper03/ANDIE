
package test.cosc202.andie;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import cosc202.andie.ImagePanel;
import cosc202.andie.ViewActions;

/**
 * The ViewActionsTest class is a JUnit test class for testing the various()
 * methods of the zooms int the ViewActions class.
 * 
 * @author Eszter Scarlett-Herbert
 * @version 2.0
 *          note: the use of the known reflection as a way to access the
 *          constructor and setting its accessibility flag to true (wihtout
 *          chnaging the class itself), are adapted from ChatGPT.
 * 
 */
public class ViewActionsTest {
    ImagePanel imagePanel = new ImagePanel();

    @Test
    public void testZoomInAction() {

        try {
            /*
             * uses the reflection, so the ZoomInAction( name, ImageIcon , String , Integer)
             * instance can be created without
             * invoking the constructor explicitly.
             */
            Constructor<ViewActions.ZoomInAction> constructor = ViewActions.ZoomInAction.class
                    .getDeclaredConstructor(String.class, ImageIcon.class, String.class, Integer.class);
            constructor.setAccessible(true); // Set the constructor to be accessible
            ViewActions.ZoomInAction zoomInAction = constructor.newInstance("Zoom In", null, "Zoom In",
                    KeyEvent.VK_PLUS);

            /*
             * performs the zoom in action and asserts that the zoom level of the imagePanel
             * increased by 10, and should now be equal to 110.
             */
            zoomInAction.actionPerformed(null);
            assertEquals(110.0, imagePanel.getZoom(), 0.001);

            /*
             * handles any exceptions that may occur during the reflection process and
             * prints a message indicating that the constructor invocation with reflection
             * failed.
             */
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Failed to invoke ZoomIn constructor with reflection");
        }

    }

    @Test
    public void testZoomOutAction() {

        /*
         * uses the reflection, so the ZoomOutAction( name, ImageIcon , String ,
         * Integer) instance can be created without
         * invoking the constructor explicitly.
         */
        try {
            Constructor<ViewActions.ZoomOutAction> constructor = ViewActions.ZoomOutAction.class
                    .getDeclaredConstructor(String.class, ImageIcon.class, String.class, Integer.class);
            constructor.setAccessible(true); // Set the constructor to be accessible
            ViewActions.ZoomOutAction zoomOutAction = constructor.newInstance("Zoom Out", null, "Zoom Out",
                    KeyEvent.VK_PLUS);

            /*
             * performs the zoom out action and asserts that the zoom level of the
             * imagePanel decreased by 10, and should now be equal to 90.
             */
            zoomOutAction.actionPerformed(null);
            assertEquals(90.0, imagePanel.getZoom(), 0.001);

            /*
             * handles any exceptions that may occur during the reflection process and
             * prints a message indicating that the constructor invocation with reflection
             * failed.
             */
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Failed to invoke ZoomOut constructor with reflection");
        }
    }

    @Test
    public void testZoomFullAction() {
        /*
         * uses the reflection, so the ZoomFullAction( name, ImageIcon , String ,
         * Integer) instance can be created without
         * invoking the constructor explicitly.
         */
        try {
            Constructor<ViewActions.ZoomFullAction> constructor = ViewActions.ZoomFullAction.class
                    .getDeclaredConstructor(String.class, ImageIcon.class, String.class, Integer.class);
            constructor.setAccessible(true); // Set the constructor to be accessible
            ViewActions.ZoomFullAction zoomFullAction = constructor.newInstance("Zoom Full", null, "Zoom Full",
                    KeyEvent.VK_PLUS);

            /*
             * asserts that the zoom value initially should be set to 100, then triggers the
             * zoom full action and checks that tus has the zoom as 100
             */
            assertEquals(100.0, imagePanel.getZoom(), 0.001); //tolerance is 0.001
            zoomFullAction.actionPerformed(null);
            assertEquals(100.0, imagePanel.getZoom(), 0.001); //tolerance is 0.001

            /*
             * handles any exceptions that may occur during the reflection process and
             * prints a message indicating that the constructor invocation with reflection
             * failed.
             */
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Failed to invoke Zoom Full with reflection");
        }
    }

}
