package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be
 * added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills edited by Hannah Srzich
 * @version 1.0
 */
public class EditActions {
    String undo;
    String redo;

    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();
        actions.add(new UndoAction(Andie.bundle.getString("undo"), null, Andie.bundle.getString("undoDesc"), Integer.valueOf(KeyEvent.VK_Z)));
        actions.add(new RedoAction(Andie.bundle.getString("redo"), null, Andie.bundle.getString("redoDesc"), Integer.valueOf(KeyEvent.VK_Y)));
        actions.add(new UndoAllAction(Andie.bundle.getString("undoAll"), null, Andie.bundle.getString("undoAllDesc"), Integer.valueOf(KeyEvent.VK_U)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Edit actions.
     * This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu(Andie.bundle.getString("edit"));

        for (Action action: actions) {
            JMenuItem menuItem = new JMenuItem(action);
            menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(), KeyEvent.CTRL_DOWN_MASK));
            editMenu.add(menuItem);
        }

        return editMenu;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
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
                String title = Andie.bundle.getString("undoError");
                String message = Andie.bundle.getString("undoErrorImageMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            try {
                target.getImage().undo();
            } catch (Exception undoException) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("undoError");
                String message = Andie.bundle.getString("undoErrorNoneMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
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
                String title = Andie.bundle.getString("redoError");
                String message = Andie.bundle.getString("redoErrorImageMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            try {
                target.getImage().redo();
            } catch (Exception redoException) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("redoError");
                String message = Andie.bundle.getString("redoErrorNoneMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to undo all {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     * @author Hannah Srzich
     */
    public class UndoAllAction extends ImageAction {

        /**
         * <p>
         * Create a new undoAll action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        UndoAllAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undoAll action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes all applied operations.
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
                String title = Andie.bundle.getString("undoAllError");
                String message = Andie.bundle.getString("undoAllErrorImageMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            try {
                target.getImage().undo();
            } catch (Exception undoException) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("undoAllError");
                String message = Andie.bundle.getString("undoAllErrorNoneMessage")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            target.getImage().undoAll();
            target.repaint();
            target.getParent().revalidate();
        }
    }

}
