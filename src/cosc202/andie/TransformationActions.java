package cosc202.andie;

import java.util.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Transformations menu actions for rotating, flipping and resizing images in the ANDIE program.
 * This class creates the actions for these transformations and calls the apply method for these once
 * the action is triggered by the user.
 * 
 * @author Luke Piper edited by Hannah Srzich
 */
public class TransformationActions {
    String r90R;
    String r90L; 
    String r180; 
    String hFlip; 
    String vFlip; 
    String resize;
    String crop;

    /** A list of actions for the Transformation menu. */
    protected ArrayList<Action> actions;

    /**
     * 
     * Create a set of Transformation menu actions.
     *
     */
    public TransformationActions() {
        actions = new ArrayList<Action>();
        actions.add(new RotateImage90RightAction(Andie.bundle.getString("r90R"), null, Andie.bundle.getString("r90Desc"), Integer.valueOf(KeyEvent.VK_R)));
        actions.add(new RotateImage90LeftAction(Andie.bundle.getString("r90L"), null, Andie.bundle.getString("l90Desc"), Integer.valueOf(KeyEvent.VK_L)));
        actions.add(new RotateImage180Action(Andie.bundle.getString("r180"), null, Andie.bundle.getString("r180Desc"), Integer.valueOf(KeyEvent.VK_F)));
        actions.add(new HorizontalFlipAction(Andie.bundle.getString("hFlip"), null, Andie.bundle.getString("flipHDesc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new VerticalFlipAction(Andie.bundle.getString("vFlip"), null, Andie.bundle.getString("flipVDesc"), Integer.valueOf(KeyEvent.VK_U)));
        actions.add(new ResizeImageAction(Andie.bundle.getString("resize"), null, Andie.bundle.getString("resizeDesc"), Integer.valueOf(KeyEvent.VK_D)));
        actions.add(new CropImageAction(Andie.bundle.getString("crop"), null, Andie.bundle.getString("cropDesc"), Integer.valueOf(KeyEvent.VK_C)));
    }

    /**
     * <p>
     * Create a menu containing the list of Transformation actions.
      This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * </p>
     * 
     * @return The transformation menu UI element.
     */
    public JMenu createMenu() {
        JMenu transfromMenu = new JMenu(Andie.bundle.getString("transform"));

        for (Action action: actions) {
            JMenuItem menuItem = new JMenuItem(action);
            int mnemonic = ((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue();
            int accelerator = KeyStroke.getKeyStroke(mnemonic, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK).getKeyCode();
            menuItem.setMnemonic(mnemonic);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
            transfromMenu.add(menuItem);
        }

        return transfromMenu;
    }

    /**
    * A method to get the slider object.
    * @param min the minimum radius
    * @param max the maximum radius
    * @param title the name of the slider
    * @return slider
    * 
    */
    public JSlider getSlider(int min, int max, String title){
        JSlider slider = new JSlider();         // Show J Slider
        // all settings for this JSlider
        slider.setPaintTicks(true);
        slider.setPaintLabels(true); 
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setValue((min + max)/2);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        // note no ticking to snaps, as it is a double not int
        Dimension d = slider.getPreferredSize();
        slider.setPreferredSize(new Dimension(d.width+400,d.height));
        
        return slider;
     }
  
     public void makeSlider(int min, int max, String title, ImagePanel target, int filterType){
        // Note, it does not include Sharpen filter, because it does not have a value
        // Resize filter = 1

        EditableImage realImage = target.getImage();
        // get slider object
        JSlider slider = getSlider(min, max, title);
        Andie.colorFontSlider(slider);
  
        // update the image on screen as the slider moves
        slider.addChangeListener(new ChangeListener() {
           // keep track of the last value of the slider
           int lastSilderValue = 0;
           public void stateChanged(ChangeEvent ce) {
              // Update for mouse listener bug
              Andie.chooserOperating = true;
              // Create a deep copy of the editable image (so that we don't change the actual editable image)
              EditableImage copyImage = realImage.deepCopyEditable();
              // Set the target to have this new copy of the actual image.
              target.setImage(copyImage);
              // Apply the median filter to the new copy of the actual image.
              if (slider.getValue() == 0) { // No change to apply.
                  return;
              }
              // update the image if the difference between the last slider value and new slider value is greater than or equal to 1
              if (Math.abs(lastSilderValue - slider.getValue()) >= 1) {
                 if(filterType == 1){
                    target.getImage().apply(new ResizeImage(slider.getValue()));
                 }
                 target.repaint();
                 target.getParent().revalidate();
                 // Update the last value of the slider
                 lastSilderValue = slider.getValue();
              }
           }
        });
  
        // make option pane
        int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);
        // Update for mouse listener bug
        Andie.chooserOperating = false;
        // reset image as the original image
        target.setImage(realImage);

        // if user presses cancel
        if(select == JOptionPane.CANCEL_OPTION){
            // repaint the original image
            target.repaint();
            target.getParent().revalidate();
            return;
        }
  
        // Create and apply the filter
        if(filterType == 1){
           target.getImage().apply(new ResizeImage(slider.getValue()));
        }

        target.repaint();
        target.getParent().revalidate();
     }

    /**
    * <p>
    * Callback for when the rotate image 90 degrees right action is triggered.
    * </p>
    */
    public class RotateImage90RightAction extends ImageAction {
        /**
         *
         * Create a new rotate 90 right action
         * 
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RotateImage90RightAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the rotate image 90 degrees right action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RotateImage90RightAction is triggered.
         * It will rotate the image 90 degrees right
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
                String title = Andie.bundle.getString("R90Error");
                String message = Andie.bundle.getString("R90ErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new RotateImage(true, false));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Callback for when the rotate image 90 degrees left action is triggered.
     * </p>
     */
    public class RotateImage90LeftAction extends ImageAction {

        /**
         * <p>
         * Create a new rotate image 90 left action
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RotateImage90LeftAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the rotate image 90 degrees left action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RotateImage90LeftAction is triggered.
         * It will rotate the image 90 degrees left
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
                String title = Andie.bundle.getString("L90Error");
                String message = Andie.bundle.getString("L90ErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new RotateImage(false, false));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Callback for when the rotate image 180 degrees action is triggered.
     * </p>
     */
    public class RotateImage180Action extends ImageAction {

        /**
         * <p>
         * Create a new rotate image 180 action
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RotateImage180Action(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the rotate image 180 degrees action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RotateImage180Action is triggered.
         * It will rotate the image 180 degrees
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
                String title = Andie.bundle.getString("180Error");
                String message = Andie.bundle.getString("180ErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new RotateImage(false, true));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Callback for when the horizontal flip action is triggered.
     * </p>
     */
    public class HorizontalFlipAction extends ImageAction {

        /**
         * <p>
         * Create a new horizontal flip action
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        HorizontalFlipAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the horizontal flip action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the HorizontalFlipAction is triggered.
         * It will flip an image horizontally- HorizontalFlip.
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
                String title = Andie.bundle.getString("flipHError");
                String message = Andie.bundle.getString("flipHErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new FlipImage(false));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Callback for when the vertical flip action is triggered.
     * </p>
     */
    public class VerticalFlipAction extends ImageAction {

        /**
         * <p>
         * Create a new vertical flip action
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        VerticalFlipAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the vertical flip action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the VerticalFlipAction is triggered.
         * It will flip the image vertically-VerticalFlip.
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
                String title = Andie.bundle.getString("flipVError");
                String message = Andie.bundle.getString("flipVErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new FlipImage(true));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Callback for when the resize image action is triggered.
     * </p>
     */
    public class ResizeImageAction extends ImageAction {
        private static int maxPercent = 200;
        private static int minPercent = 50;
        private String titleSlider = Andie.bundle.getString("resize");

        /**
         * <p>
         * Create a new resize image action
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ResizeImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the resize image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ResizeImageAction is triggered.
         * It will resize the image to a user given percent{@link ResizeImage}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // Remove the crop region so it doesnt break on slider image change
            ImagePanel.region = null;
            target.repaint();

            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("resizeError");
                String message = Andie.bundle.getString("resizeErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }

            // make slider and change listener, and apply the filter
            makeSlider(minPercent, maxPercent, titleSlider, target, 1);
        }

        /**
        * A method to get the value from the user through a JSlider.
        * @param min
        * @param max
        * @param title
        * @return int value, selected by user
        * 
        */
        public int getSliderValue(int min, int max, String title){
            // Show J Slider
            JSlider slider = new JSlider();
            Andie.colorFontSlider(slider);
            // all settings for this JSlider
            slider.setPaintTicks(true);
            slider.setPaintLabels(true); 
            slider.setMaximum(max);
            slider.setMinimum(min);
            slider.setValue((min + max)/2);
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(5);
            // note no ticking to snaps, as it is a double not int
            Dimension d = slider.getPreferredSize();
            slider.setPreferredSize(new Dimension(d.width+400,d.height));
            // make option pane
            int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);

            if(select == JOptionPane.CANCEL_OPTION){
                return -999;
            }else{
                return (int) slider.getValue();
            }
        }
    }

    /**
     * <p>
     * Callback for when the crop image action is triggered.
     * </p>
     */
    public class CropImageAction extends ImageAction {

        /**
         *
         * Create a new crop image right action
         * 
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        CropImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the crop image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the CropImage is triggered.
         * It will crop the image to the mouse region the user selected
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
                String title = Andie.bundle.getString("cropError");
                String message = Andie.bundle.getString("cropErrorImageMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Force user to drag mouse to create mouse region they want first
            if(ImagePanel.region == null){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                JOptionPane.showMessageDialog(null, Andie.bundle.getString("cropErrorDesc")+ "       ", Andie.bundle.getString("cropError"), JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Create and apply the filter
            target.getImage().apply(new CropImage(ImagePanel.region));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}