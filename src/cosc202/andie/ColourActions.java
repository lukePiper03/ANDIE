package cosc202.andie;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations
 * will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Dante Vannini edited by Hannah Srzich
 * @version 1.0
 */
public class ColourActions {
    String greyScale;

    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new BrightnessAction(Andie.bundle.getString("brightness"), null,
                Andie.bundle.getString("contrastDesc"), Integer.valueOf(KeyEvent.VK_B)));
        actions.add(new ContrastAction(Andie.bundle.getString("contrast"), null, Andie.bundle.getString("brightDesc"),
                Integer.valueOf(KeyEvent.VK_C)));
        actions.add(new InvertFilterAction(Andie.bundle.getString("invert"), null, Andie.bundle.getString("invertDesc"),
                Integer.valueOf(KeyEvent.VK_I)));
        actions.add(new ConvertToGreyAction(Andie.bundle.getString("greyScale"), null,
                Andie.bundle.getString("greyDesc"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new HueFilterAction(Andie.bundle.getString("hueFilter"), null, Andie.bundle.getString("hueDesc"),
                Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new DitherFilterAction(Andie.bundle.getString("Dither"), null, Andie.bundle.getString("ditherDesc"),
                Integer.valueOf(KeyEvent.VK_D)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu colorMenu = new JMenu(Andie.bundle.getString("colour"));

        for (Action action : actions) {
            JMenuItem menuItem = new JMenuItem(action);
            menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(),
                    KeyEvent.CTRL_DOWN_MASK));
            colorMenu.add(menuItem);
        }

        return colorMenu;
    }

    /**
     * A method to get the slider object.
     * 
     * @param min
     * @param max
     * @return slider
     * 
     */
    public JSlider getSlider(int min, int max) {
        // Show J Slider
        JSlider slider = new JSlider();
        Andie.colorFontSlider(slider);
        // all settings for this JSlider
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setValue((min + max) / 2);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
        Dimension d = slider.getPreferredSize();
        slider.setPreferredSize(new Dimension(d.width + 200, d.height));

        return slider;
    }

    /**
     * A support method to get the slider options for the getSliderRadius
     * method.
     * 
     * @param optionPane
     * @param min
     * @param max
     * @return JSlider
     *         credit to:
     *         http://www.java2s.com/Tutorial/Java/0240__Swing/UsingJOptionPanewithaJSlider.htm
     */
    static JSlider getSlider(final JOptionPane optionPane, int min, int max) {
        JSlider slider = new JSlider();
        Andie.colorFontSlider(slider);
        // all settings for this JSlider
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setValue((min + max) / 2);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        // note no ticking to snaps, as it is a double not int
        Dimension d = slider.getPreferredSize();
        slider.setPreferredSize(new Dimension(d.width + 400, d.height));
        // add a listener to the slider
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(Integer.valueOf(theSlider.getValue()));
                }
            }

        };

        return slider;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();

                String title = Andie.bundle.getString("greyError");
                String message = Andie.bundle.getString("greyErrorMessage") + "       ";

                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                Andie.setOptionStyle();
                return;
            }
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * Changes the brightness and contrast of an image
     */
    public class BrightnessAction extends ImageAction {
        /**
         * The minimum percentage value for brightness.
         */
        private static int minPercentage = -100;
        /**
         * The maximum percentage value for brightness.
         */
        private static int maxPercentage = 100;
        /**
         * The title for the brightness slider.
         */
        private String titleSlider = Andie.bundle.getString("brightness");

        // create a new spinner that iniialises the range of values between the min and
        // the max
        // then the Jspinner will use that model as a parameter and create a spinner
        // based on it
        private SpinnerNumberModel model = new SpinnerNumberModel(minPercentage, minPercentage, minPercentage, 0.1);
        private JSpinner spinner = new JSpinner(model);

        // these are the default messages that will come up on the message box
        private String title = Andie.bundle.getString("invalidInput");
        private String message = Andie.bundle.getString("percentMessage") + minPercentage + " "
                + Andie.bundle.getString("and") + maxPercentage + "." + "       ";

        /**
         * Constructs a new BrightnessAction with the specified name, icon, description,
         * and mnemonic.
         *
         * @param name     the name of the action
         * @param icon     the icon to be displayed for the action
         * @param desc     the description of the action
         * @param mnemonic the mnemonic key for the action
         */
        BrightnessAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the brightness action when triggered by an event.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("brightError");
                String message = Andie.bundle.getString("brightErrorMessage") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }

            Double brightness = 0.0;
            int min = minPercentage;
            int max = maxPercentage;
            String title = titleSlider;
            EditableImage realImage = target.getImage();

            // Show J Slider
            JSlider slider = new JSlider();
            Andie.colorFontSlider(slider);
            // all settings for this JSlider
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setMaximum(max);
            slider.setMinimum(min);
            slider.setValue((min + max) / 2);
            slider.setMajorTickSpacing(20);
            slider.setMinorTickSpacing(5);
            // note no ticking to snaps, as it is a double not int
            Dimension d = slider.getPreferredSize();
            slider.setPreferredSize(new Dimension(d.width + 400, d.height));

            // pdate the image on screen as the slider moves
            slider.addChangeListener(new ChangeListener() {
                // keep track of the last value of the slider
                int lastSilderValue = 0;

                public void stateChanged(ChangeEvent ce) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // make a copy of the editable image so that the actual image isnt changed
                    EditableImage copyImage = realImage.deepCopyEditable();
                    target.setImage(copyImage);
                    // if slider value is nothing dont apply
                    if (slider.getValue() == 0) {
                        return;
                    }
                    // update the image if the difference between the last slider value and new
                    // slider value is greater than or equal to 2
                    if (Math.abs(lastSilderValue - slider.getValue()) >= 2) {
                        target.getImage().apply(new Brightness(slider.getValue(), 0));
                        target.repaint();
                        target.getParent().revalidate();
                        // Update the last value of the slider
                        lastSilderValue = slider.getValue();
                    }
                }
            });

            // make option pane
            int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);
            // Update for mouse listener bug
            Andie.chooserOperating = false;
            // reset image as the origional image
            target.setImage(realImage);

            // if user presses cancel
            if (select == JOptionPane.CANCEL_OPTION) {
                // re paint the origional image
                target.repaint();
                target.getParent().revalidate();
                return;
            } else { // else we can apply the filter
                brightness = (double) slider.getValue();
            }

            // Create and apply the filter
            target.getImage().apply(new Brightness(brightness, 0));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Changes the brightness and contrast of an image
     */
    public class ContrastAction extends ImageAction {
        /**
         * The minimum percentage value for contrast.
         */
        private static int minPercentage = -100;
        /**
         * The maximum percentage value for contrast.
         */
        private static int maxPercentage = 100;
        /**
         * The title for the contrast slider.
         */
        private String titleSlider = Andie.bundle.getString("contrast");

        /**
         * Constructs a new ContrastAction with the specified name, icon, description,
         * and mnemonic.
         *
         * @param name     the name of the action
         * @param icon     the icon to be displayed for the action
         * @param desc     the description of the action
         * @param mnemonic the mnemonic key for the action
         */
        ContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the contrast action when triggered by an event.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("contrastError");
                String message = Andie.bundle.getString("contrastErrorMessage") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            Double contrast = 0.0;
            int min = minPercentage;
            int max = maxPercentage;
            String title = titleSlider;
            EditableImage realImage = target.getImage();

            // Show J Slider
            JSlider slider = new JSlider();
            Andie.colorFontSlider(slider);
            // all settings for this JSlider
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setMaximum(max);
            slider.setMinimum(min);
            slider.setValue((min + max) / 2);
            slider.setMajorTickSpacing(20);
            slider.setMinorTickSpacing(5);
            // note no ticking to snaps, as it is a double not int
            Dimension d = slider.getPreferredSize();
            slider.setPreferredSize(new Dimension(d.width + 400, d.height));

            // update the image on screen as the slider moves
            slider.addChangeListener(new ChangeListener() {
                // keep track of the last value of the slider
                int lastSilderValue = 0;

                public void stateChanged(ChangeEvent ce) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // Create a deep copy of the editable image (so that we don't change the actual
                    // editable image)
                    EditableImage copyImage = realImage.deepCopyEditable();
                    // Set the target to have this new copy of the actual image.
                    target.setImage(copyImage);
                    // Apply the median filter to the new copy of the actual image.
                    if (slider.getValue() == 0) { // No change to apply.
                        return;
                    }
                    // update the image if the difference between the last slider value and new
                    // slider value is greater than or equal to 2
                    if (Math.abs(lastSilderValue - slider.getValue()) >= 2) {
                        target.getImage().apply(new Brightness(0, slider.getValue()));
                        target.repaint();
                        target.getParent().revalidate();
                        // Update the last value of the slider
                        lastSilderValue = slider.getValue();
                    }
                }
            });

            // make option pane
            int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);

            // Update for mouse listener bug
            Andie.chooserOperating = false;

            // reset image as the origional image
            target.setImage(realImage);

            // if user presses cancel
            if (select == JOptionPane.CANCEL_OPTION) {
                // re paint the origional image
                target.repaint();
                target.getParent().revalidate();
                return;
            } else { // otherwise get the final silder value
                contrast = (double) slider.getValue();
            }

            // Create and apply the filter
            target.getImage().apply(new Brightness(0, contrast));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * Represents an action that applies an invert filter to an image.
     */
    public class InvertFilterAction extends ImageAction {

        /**
         * Constructs a new InvertFilterAction with the specified name, icon,
         * description, and mnemonic.
         *
         * @param name     the name of the action
         * @param icon     the icon to be displayed for the action
         * @param desc     the description of the action
         * @param mnemonic the mnemonic key for the action
         */
        InvertFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the invert filter action when triggered by an event.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("invertError");
                String message = Andie.bundle.getString("filterErrorMessage") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }

            // Create and apply the invert filter to the target image
            target.getImage().apply(new InvertFilter());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Represents an action that applies a hue filter to an image based on a
     * selected color.
     */
    public class HueFilterAction extends ImageAction {

        /**
         * Constructs a new HueFilterAction with the specified name, icon, description,
         * and mnemonic.
         *
         * @param name     the name of the action
         * @param icon     the icon to be displayed for the action
         * @param desc     the description of the action
         * @param mnemonic the mnemonic key for the action
         */
        HueFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the hue filter action when triggered by an event.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("hueError");
                String message = Andie.bundle.getString("filterErrorMessage") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }

            // Save the real image
            EditableImage realImage = target.getImage();

            // Create a custom dialog box with a color chooser
            JColorChooser colorChooser = new JColorChooser();
            Andie.setJChooserFont(colorChooser.getComponents());
            // Remove the preview panel
            colorChooser.setPreviewPanel(new JPanel());
            colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // Create a deep copy of the editable image (so that we don't change the actual
                    // editable image)
                    EditableImage copyImage = realImage.deepCopyEditable();
                    // Set the target to have this new copy of the actual image
                    target.setImage(copyImage);
                    // Apply the filter with the current color
                    target.getImage().apply(new HueFilter(colorChooser.getColor()));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // Show the color chooser dialog
            int result = JOptionPane.showOptionDialog(null, colorChooser, Andie.bundle.getString("hueFilter"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            // Update for mouse listener bug
            Andie.chooserOperating = false;
            // Reset image to the original image
            target.setImage(realImage);

            // If user presses cancel
            if (result == JOptionPane.CANCEL_OPTION) {
                // Re-paint the original image
                target.repaint();
                target.getParent().revalidate();
                return;
            }

            // Apply the filter with the selected color
            target.getImage().apply(new HueFilter(colorChooser.getColor()));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Represents an action that applies a dither filter to an image based on
     * selected colors and a slider value.
     */
    public class DitherFilterAction extends ImageAction {

        /**
         * Constructs a new DitherFilterAction with the specified name, icon,
         * description, and mnemonic.
         *
         * @param name     the name of the action
         * @param icon     the icon to be displayed for the action
         * @param desc     the description of the action
         * @param mnemonic the mnemonic key for the action
         */
        DitherFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the dither filter action when triggered by an event.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("ditherError");
                String message = Andie.bundle.getString("filterErrorMessage") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // save the real image
            EditableImage realImage = target.getImage();

            // create two JColorChooser components
            JColorChooser colorChooser1 = new JColorChooser();
            JColorChooser colorChooser2 = new JColorChooser();
            // change font in the color chooser
            Andie.setJChooserFont(colorChooser1.getComponents());
            Andie.setJChooserFont(colorChooser2.getComponents());
            // remove the preview panels
            colorChooser1.setPreviewPanel(new JPanel());
            colorChooser2.setPreviewPanel(new JPanel());
            // set dimensions
            colorChooser1.setPreferredSize(new Dimension(600, 200));
            colorChooser2.setPreferredSize(new Dimension(600, 200));

            // make the slider
            JSlider slider = getSlider(0, 100);
            Andie.colorFontSlider(slider);

            // create JPanel
            JPanel panel = new JPanel(new BorderLayout());

            // Create a panel to hold the top color choosers
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(colorChooser1, BorderLayout.WEST);
            topPanel.add(colorChooser2, BorderLayout.EAST);
            panel.add(topPanel, BorderLayout.NORTH);
            // Create a panel to hold bottom slider
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(slider, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            // Create a custom panel and add the JColorChooser components to it
            panel.setPreferredSize(new Dimension(1200, 250));

            // color listener for no 1
            colorChooser1.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // create a deep copy of the editable image (so that we don't change the actual
                    // editable image)
                    EditableImage copyImage = realImage.deepCopyEditable();
                    // set the target to have this new copy of the actual image
                    target.setImage(copyImage);
                    // apply the filter with the current color
                    target.getImage().apply(
                            new DitherFilter(slider.getValue(), colorChooser1.getColor(), colorChooser2.getColor()));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // color listener for no 2
            colorChooser2.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // create a deep copy of the editable image (so that we don't change the actual
                    // editable image)
                    EditableImage copyImage = realImage.deepCopyEditable();
                    // set the target to have this new copy of the actual image
                    target.setImage(copyImage);
                    // apply the filter with the current color
                    target.getImage().apply(
                            new DitherFilter(slider.getValue(), colorChooser1.getColor(), colorChooser2.getColor()));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // slider listener
            slider.addChangeListener(new ChangeListener() {
                // keep track of the last value of the slider
                int lastSilderValue = 0;

                public void stateChanged(ChangeEvent ce) {
                    // Update for mouse listener bug
                    Andie.chooserOperating = true;
                    // Create a deep copy of the editable image (so that we don't change the actual
                    // editable image)
                    EditableImage copyImage = realImage.deepCopyEditable();
                    // Set the target to have this new copy of the actual image.
                    target.setImage(copyImage);
                    // Apply the median filter to the new copy of the actual image.
                    if (slider.getValue() == 0) { // No change to apply.
                        return;
                    }
                    // update the image if the difference between the last slider value and new
                    // slider value is greater than or equal to 2
                    if (Math.abs(lastSilderValue - slider.getValue()) >= 2) {
                        target.getImage().apply(new DitherFilter(slider.getValue(), colorChooser1.getColor(),
                                colorChooser2.getColor()));
                        target.repaint();
                        target.getParent().revalidate();
                        // Update the last value of the slider
                        lastSilderValue = slider.getValue();
                    }
                }
            });

            // Show the Option Dialog with the custom panel
            int result = JOptionPane.showOptionDialog(null, panel, Andie.bundle.getString("Dither"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            // Update for mouse listener bug
            Andie.chooserOperating = false;

            // reset image as the original image
            target.setImage(realImage);

            // if user presses cancel
            if (result == JOptionPane.CANCEL_OPTION) {
                // re paint the origional image
                target.repaint();
                target.getParent().revalidate();
                return;
            }

            // else, apply the filter
            target.getImage().apply(new DitherFilter(slider.getValue(), colorChooser1.getColor(), colorChooser2.getColor()));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
