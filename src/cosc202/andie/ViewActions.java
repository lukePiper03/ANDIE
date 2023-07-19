package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the View menu.
 * </p>
 * 
 * <p>
 * The View menu contains actions that affect how the image is displayed in the application.
 * These actions do not affect the contents of the image itself, just the way it is displayed.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills edited by Hannah Srzich
 * @version 1.0
 */
public class ViewActions {
    String zoomIn;
    String zoomOut; 
    String zoomFull; 
    
    /**
     * A list of actions for the View menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of View menu actions.
     * </p>
     */
    public ViewActions() {

        actions = new ArrayList<Action>();
        actions.add(new ZoomInAction(Andie.bundle.getString("zoomIn"), null, Andie.bundle.getString("zoomInDesc"), Integer.valueOf(KeyEvent.VK_EQUALS)));
        actions.add(new ZoomOutAction(Andie.bundle.getString("zoomOut"), null, Andie.bundle.getString("zoomOutDesc"), Integer.valueOf(KeyEvent.VK_MINUS)));
        actions.add(new ZoomFullAction(Andie.bundle.getString("zoomFull"), null, Andie.bundle.getString("zoomFullDesc"), Integer.valueOf(KeyEvent.VK_PERIOD)));
    }

    /**
     * <p>
     * Create a menu containing the list of View actions.
     * This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * </p>
     * 
     * @return The view menu UI element.
     */
    public JMenu createMenu() {
        JMenu viewMenu = new JMenu(Andie.bundle.getString("view"));

        for (Action action: actions) {
            JMenuItem menuItem = new JMenuItem(action);
            menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(), KeyEvent.CTRL_DOWN_MASK));
            viewMenu.add(menuItem);
        }
        return viewMenu;
    }

    /**
     * <p>
     * Action to zoom in on an image.
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ZoomInAction extends ImageAction {

        /**
         * <p>
         * Create a new zoom-in action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ZoomInAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the zoom-in action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ZoomInAction is triggered.
         * It increases the zoom level by 10%, to a maximum of 200%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("zoomInError");
                String message = Andie.bundle.getString("zoomInErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            int zoom = (int) target.getZoom()+10;
            target.setZoom(zoom);
            //System.out.println(zoom);
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to zoom out of an image.
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ZoomOutAction extends ImageAction {

        /**
         * <p>
         * Create a new zoom-out action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ZoomOutAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the zoom-iout action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ZoomOutAction is triggered.
         * It decreases the zoom level by 10%, to a minimum of 50%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("zoomOutError");
                String message = Andie.bundle.getString("zoomOutErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            int zoom = (int) target.getZoom()-10;
            target.setZoom(zoom);
            target.repaint();
            target.getParent().revalidate();
            /* zoom probaly shouldnt resize window
            // Get new dimension
            int width = target.getImage().getCurrentImage().getWidth()*(int)(zoom)/100;
            int height = target.getImage().getCurrentImage().getHeight()*(int)(zoom)/100;
            Dimension dim = new Dimension(width, height);
            // Make window resize to image size.
            target.getImage().resizeWindow(dim);
            */
        }

    }

    /**
     * <p>
     * Action to reset the zoom level to actual size.
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ZoomFullAction extends ImageAction {

        /**
         * <p>
         * Create a new zoom-full action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ZoomFullAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the zoom-full action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ZoomFullAction is triggered.
         * It resets the Zoom level to 100%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("zoomFullError");
                String message = Andie.bundle.getString("zoomFullErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            target.setZoom(100);
            double zoom = target.getZoom();
            target.setZoom(zoom);
            target.repaint();
            target.getParent().revalidate();
            /* zoom probaly shouldnt resize window
            //Get new dimension
            int width = target.getImage().getCurrentImage().getWidth()*(int)(zoom)/100;
            int height = target.getImage().getCurrentImage().getHeight()*(int)(zoom)/100;
            Dimension dim = new Dimension(width, height);
            //Make window resize to image size.
            target.getImage().resizeWindow(dim);
            */
        }

    }



}
