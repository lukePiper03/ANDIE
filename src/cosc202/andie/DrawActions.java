package cosc202.andie;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * The DrawActions class represents a collection of drawing actions for creating different shapes and modifying colors.
 */
public class DrawActions {

    protected ArrayList<Action> Actions = new ArrayList<Action>();
    public static String shape;
    public static Color colour = Color.white; // by default white
    public static boolean isOutline = false; // default no outline

    /**
     * Constructs a new DrawActions object and initializes the available drawing actions.
     * This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     */
    public DrawActions() {
        Actions.add(new DrawSquareAction(Andie.bundle.getString("Square"), null, Andie.bundle.getString("SquareDesc"),
                Integer.valueOf(KeyEvent.VK_5)));
        Actions.add(new DrawOvalAction(Andie.bundle.getString("Oval"), null, Andie.bundle.getString("OvalDesc"),
                Integer.valueOf(KeyEvent.VK_6)));
        Actions.add(new DrawLineAction(Andie.bundle.getString("Line"), null, Andie.bundle.getString("LineDesc"),
                Integer.valueOf(KeyEvent.VK_7)));
        Actions.add(new DrawOutlineAction(outlineOrFill(), null,
                Andie.bundle.getString("OutlineDesc"), Integer.valueOf(KeyEvent.VK_8)));
        Actions.add(new ColourChooserAction(Andie.bundle.getString("Colour"), null,
                Andie.bundle.getString("ColourDesc"), Integer.valueOf(KeyEvent.VK_9)));
    }

    /**
     * Determines whether the current setting is on fill or outline to
     * display the correct string in the option menu.
     * 
     * @return the string representation of the outline or fill status based on the current configuration
     */
    public String outlineOrFill() {
        if (isOutline) {
            return Andie.bundle.getString("Fill");
        } else {
            return Andie.bundle.getString("Outline");
        }
    }

    /**
     * Creates and returns a JMenu containing the available drawing actions as menu items.
     *
     * @return The JMenu containing the drawing actions.
     */
    public JMenu createMenu() {

        JMenu DrawMenu = new JMenu(Andie.bundle.getString("DrawShapes"));
        // update the menu to outline or fill text based on current option
        Actions.set(3, new DrawOutlineAction(outlineOrFill(), null, Andie.bundle.getString("OutlineDesc"), Integer.valueOf(KeyEvent.VK_8)));

        for (Action action : Actions) {
            JMenuItem menuItem = new JMenuItem(action);
            menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(),
                    KeyEvent.CTRL_DOWN_MASK));
            DrawMenu.add(menuItem);
        }
        return DrawMenu;
    }

    /**
     * The ColourChooserAction class represents a drawing action that opens a color chooser dialog for selecting a color.
     * It extends the ImageAction class.
     */
    public class ColourChooserAction extends ImageAction {

        /**
         * Constructs a new ColourChooserAction object with the specified name, icon, description, and mnemonic.
         *
         * @param name     The name of the action.
         * @param icon     The icon associated with the action.
         * @param desc     The description of the action.
         * @param mnemonic The mnemonic key code for the action.
         */
        ColourChooserAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the action when triggered by an event.
         *
         * @param e The ActionEvent that triggered the action.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                 // Play error sound, play the audio in a separate thread
                 Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                 audioThread.start();
                 String title = Andie.bundle.getString("ColourError");
                 String message = Andie.bundle.getString("ColourErrorDesc") + "       ";
                 JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                 return;
            }
            // save the real image
            EditableImage realImage = target.getImage();

            // create a custom dialog box with a color chooser
            JColorChooser colorChooser = new JColorChooser();
            Andie.setJChooserFont(colorChooser.getComponents());
            // remove the preview panel
            colorChooser.setPreviewPanel(new JPanel());

            int result = JOptionPane.showOptionDialog(null, colorChooser, Andie.bundle.getString("ColourChooser"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            // reset image as the original image
            target.setImage(realImage);

            // if user presses cancel
            if (result == JOptionPane.CANCEL_OPTION) {
                // re paint the original image
                return;
            }
            DrawActions.colour = colorChooser.getColor();
        }
    }

    /**
     * The DrawSquareAction class represents a drawing action for creating a square shape on an image.
     * It extends the ImageAction class.
     */
    public class DrawSquareAction extends ImageAction {

        /**
         * Constructs a new DrawSquareAction object with the specified name, icon, description, and mnemonic.
         *
         * @param name     The name of the action.
         * @param icon     The icon associated with the action.
         * @param desc     The description of the action.
         * @param mnemonic The mnemonic key code for the action.
         */
        DrawSquareAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the action when triggered by an event.
         *
         * @param e The ActionEvent that triggered the action.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // Force user to drag mouse to create mouse region they want first
            if (ImagePanel.region == null) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                JOptionPane.showMessageDialog(null, Andie.bundle.getString("RegionSelectError") + "       ",
                        Andie.bundle.getString("ColourError"), JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("ColourError");
                String message = Andie.bundle.getString("ColourErrorDesc") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                return;
            }
            DrawActions.shape = "Square";
            Color colour = DrawActions.colour;
            if (isOutline) {
                target.getImage().apply(new DrawOutline("square", colour, ImagePanel.x, ImagePanel.y, ImagePanel.width, ImagePanel.height));
            } else {
                target.getImage().apply(new DrawSquare(colour, ImagePanel.x, ImagePanel.y, ImagePanel.width, ImagePanel.height));
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }


    /**
     * The DrawOvalAction class represents a drawing action for creating an oval shape on an image.
     * It extends the ImageAction class.
     */
    public class DrawOvalAction extends ImageAction {

        /**
         * Constructs a new DrawOvalAction object with the specified name, icon, description, and mnemonic.
         *
         * @param name     The name of the action.
         * @param icon     The icon associated with the action.
         * @param desc     The description of the action.
         * @param mnemonic The mnemonic key code for the action.
         */
        DrawOvalAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the action when triggered by an event.
         *
         * @param e The ActionEvent that triggered the action.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // Force user to drag mouse to create mouse region they want first
            if (ImagePanel.region == null) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                JOptionPane.showMessageDialog(null, Andie.bundle.getString("RegionSelectError") + "       ",
                        Andie.bundle.getString("ColourError"), JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("ColourError");
                String message = Andie.bundle.getString("ColourErrorDesc") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                return;
            }

            DrawActions.shape = "Oval";
            Color colour = DrawActions.colour;
            if (isOutline) {
                target.getImage().apply(new DrawOutline("oval", colour, ImagePanel.x, ImagePanel.y, ImagePanel.width, ImagePanel.height));
            } else {
                target.getImage().apply(new DrawOval(colour, ImagePanel.x, ImagePanel.y, ImagePanel.width, ImagePanel.height));
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }



    /**
     * The DrawLineAction class represents a drawing action for creating a line shape on an image.
     * It extends the ImageAction class.
     */
    public class DrawLineAction extends ImageAction {

        /**
         * Constructs a new DrawLineAction object with the specified name, icon, description, and mnemonic.
         *
         * @param name     The name of the action.
         * @param icon     The icon associated with the action.
         * @param desc     The description of the action.
         * @param mnemonic The mnemonic key code for the action.
         */
        DrawLineAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the action when triggered by an event.
         *
         * @param e The ActionEvent that triggered the action.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // Force user to drag mouse to create mouse region they want first
            if (ImagePanel.region == null) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                JOptionPane.showMessageDialog(null, Andie.bundle.getString("RegionSelectError") + "       ",
                        Andie.bundle.getString("ColourError"), JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("ColourError");
                String message = Andie.bundle.getString("ColourErrorDesc")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("DrawShapesError");
                String message = Andie.bundle.getString("DrawShapesErrorMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                return;
            }
            Color colour = DrawActions.colour;
            DrawActions.shape = "Line";

            target.getImage().apply(
                    new DrawLine(colour, ImagePanel.lineX1, ImagePanel.lineY1, ImagePanel.lineX2, ImagePanel.lineY2));
            target.repaint();
            target.getParent().revalidate();
        }
    }


    /**
     * The DrawOutlineAction class represents a drawing action for toggling the outline mode on an image.
     * It extends the ImageAction class.
     */
    public class DrawOutlineAction extends ImageAction {

        /**
         * Constructs a new DrawOutlineAction object with the specified name, icon, description, and mnemonic.
         *
         * @param name     The name of the action.
         * @param icon     The icon associated with the action.
         * @param desc     The description of the action.
         * @param mnemonic The mnemonic key code for the action.
         */
        DrawOutlineAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the action when triggered by an event.
         *
         * @param e The ActionEvent that triggered the action.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // Change outline to opposite of what it is
            if (isOutline) {
                isOutline = false;
            } else {
                isOutline = true;
            }
            // re draw the menu so that the fill or outline option is updated
            createMenu();
            try {
                Andie.makeJMenu(true); // True as in re draw it
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
            Andie.menuBar.repaint();
        }
    }
}
