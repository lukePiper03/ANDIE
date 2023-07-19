package cosc202.andie;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to support the recording, saving, and re applying of image operations
 * on an editable image object.
 */
public class MacroActions {

    protected ArrayList<Action> actions;
    private ImageIcon recordIcon;

    /**
     * Create a set of macro actions
     */
    public MacroActions() {
        // used to set the red icon when the macro is being recorded
        // the try ctach prevents the programme from loading if no image exists in the
        // foldr
        // the red dot is then scaled to an appropriate size and is set as the recording
        // icon
        try {
            recordIcon = new ImageIcon(
                    ImageIO.read(Andie.class.getClassLoader().getResource("pictures/" + "record.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = recordIcon.getImage();
        Image scaledImage = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
        recordIcon = new ImageIcon(scaledImage);

        actions = new ArrayList<Action>();
        actions.add(new RecordAction(Andie.bundle.getString("Record"), null, "Start recording",
                Integer.valueOf(KeyEvent.VK_SEMICOLON)));
        actions.add(new StopAndSaveAction(Andie.bundle.getString("SaveMacro"), null, "Stop recording and save",
                Integer.valueOf(KeyEvent.VK_QUOTE)));
        actions.add(new OpenAndApplyAction(Andie.bundle.getString("OpenMacro"), null, null,
                Integer.valueOf(KeyEvent.VK_SLASH)));
        actions.add(new CancelAction(Andie.bundle.getString("CancelMacro"), null, "Cancel recording",
                Integer.valueOf(KeyEvent.VK_COLON)));
    }

    /**
     * creates a menu containing the set of macro actions
     * This will then create a mnemonic key, which acts as a keyboard shortcut,
     * that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * 
     * @return The macro menu UI element
     */
    public JMenu createMenu() {
        JMenu macroMenu = new JMenu(Andie.bundle.getString("MacroTitle"));

        // the null icon is what the icon is changed to if the macro is no longer being
        // recorded
        ImageIcon nullIcon = null; // Null icon

        for (Action action : actions) {
            JMenuItem menuItem = new JMenuItem(action);

            // Check if the action is an instance of RecordAction
            // if the macro is being recorded then display the red dot on the menu bar as an
            // icon to let the user know that the macro is being recorded
            if (action instanceof RecordAction) {
                RecordAction recordAction = (RecordAction) action;
                recordAction.addChangeListener(e -> {
                    if (recordAction.isRecording()) {
                        macroMenu.setIcon(recordIcon);
                    } else {
                        macroMenu.setIcon(nullIcon); // Set the icon to null
                    }
                    macroMenu.revalidate(); // Update the menu
                });
            }

            // Check if the action is an instance of CancelAction
            // if the cancel has its firestatechanged triggered then revalidate
            // the menu and remove the red recording dot as the icon
            if (action instanceof CancelAction) {
                CancelAction cancelAction = (CancelAction) action;
                cancelAction.addChangeListener(e -> {
                    macroMenu.setIcon(nullIcon); // Set the icon to null
                    macroMenu.revalidate(); // Update the menu
                });
            }

            // Check if the action is an instance of StopAndSaveAction
            // if the stopand save action has its firestatechanged triggered then revalidate
            // the menu and remove the red recording dot as the icon
            if (action instanceof StopAndSaveAction) {
                StopAndSaveAction stopAndSaveAction = (StopAndSaveAction) action;
                stopAndSaveAction.addChangeListener(e -> {
                    macroMenu.setIcon(nullIcon); // Set the icon to null
                    macroMenu.revalidate(); // Update the menu
                });
            }

            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(),
                    KeyEvent.CTRL_DOWN_MASK));
            macroMenu.add(menuItem);
        }

        return macroMenu;
    }

    /**
     * action to record a new macro
     */
    public class RecordAction extends ImageAction {
        // both the change listener and the isRecording are going to track the recording
        // state of the record action
        private List<ChangeListener> changeListeners;
        private boolean isRecording;

        /**
         * creates a new RecordAction object
         * 
         * @param name     the name of the action
         * @param icon     the icon that represents the action
         * @param desc     a short description of the action
         * @param mnemonic a mnemonic key as a shortcut
         */
        public RecordAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            changeListeners = new ArrayList<>();
        }

        // this method is used to add a change listener to the changeListeners list. The
        // listener will receive notifications when the recording state changes.
        public void addChangeListener(ChangeListener listener) {
            changeListeners.add(listener);
        }

        // This method is used to remove a change listener from the changeListeners
        // list.
        public void removeChangeListener(ChangeListener listener) {
            changeListeners.remove(listener);
        }

        /**
         * This method is called when the RecordAction is triggered or performed. It
         * checks if the target image has an image and if it's not already recording. If
         * the conditions are met, it sets the isRecording flag to true and calls the
         * record() method on the target image. If the target image is already
         * recording, it shows an error message.
         * 
         * @param e the action event that trigered the RecordAction
         */
        public void actionPerformed(ActionEvent e) {
            // set style options
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("MacroError");
                String message = Andie.bundle.getString("MacroErrorDesc")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            if (!target.getImage().isRecording()) {
                setRecording(true);
                target.getImage().record();
            } else {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("RecordingErrorTitle");
                String message = Andie.bundle.getString("AlreadyRecordingError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            }
        }

        // this is going to check if the macro is recording
        public boolean isRecording() {
            return isRecording;
        }

        // this method is used to set the recording state (isRecording flag) to the
        // specified value. It also calls the fireStateChanged() method to notify the
        // change listeners about the state change.
        public void setRecording(boolean recording) {
            isRecording = recording;
            fireStateChanged();
        }

        // this method iterates over all the registered change listeners in the
        // changeListeners list and invokes the stateChanged() method on each listener,
        // passing a ChangeEvent object as a parameter. This notifies the listeners that
        // the state of the RecordAction has changed.
        private void fireStateChanged() {
            for (ChangeListener listener : changeListeners) {
                listener.stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * Action to stop a recording and save the set of recorded operations as a .ops
     * file
     */
    public class StopAndSaveAction extends ImageAction {
        // the change listener ais going to track the recording state of the record
        // action
        private List<ChangeListener> changeListeners;

        /**
         * constructer for a new StopAndSaveAction object
         * 
         * @param name     the name of the action
         * @param icon     the icon that represents the action
         * @param desc     a short description of the action
         * @param mnemonic a mnemonic key as a shortcut for the action
         */
        public StopAndSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            changeListeners = new ArrayList<>();
        }

        /**
         * Method to stop the recording and save the recorded actions to a .ops file
         * when a stop and save
         * action is triggered.
         * Some code adapted from hannahs saveAs action
         * 
         * @param e the action event that triggered the stop and save action
         */
        public void actionPerformed(ActionEvent e) {
            // set style options
            Andie.setOptionStyle();
            // if no image is open in ANDIE then display an error message so the user cannot
            // save and use the macro feature
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("MacroError");
                String message = Andie.bundle.getString("MacroErrorDesc")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // if the macro feature is not already recording in ANDIE then display an error
            // message so the user cannot save and use the macro feature
            if (!target.getImage().isRecording()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("RecordingErrorTitle");
                String message = Andie.bundle.getString("NotRecordingError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // if the macro associated with the target image is either null or empty, i.e no
            // changes to the original image have been made then there is nothing to save in
            // the macro, so an error message is displayed
            if (target.getImage().getMacro() == null || target.getImage().getMacro().isEmpty()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("MacroSaveErrorTitle");
                String message = Andie.bundle.getString("NothingToSaveError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            Andie.setOptionStyle();
            JFileChooser fileChooser = new JFileChooser();
            // custom font
            Andie.setJChooserFont(fileChooser.getComponents());
            int result = fileChooser.showSaveDialog(target);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    // Check if the file path already exists.
                    File filePath = new File(imageFilepath + ".ops");
                    boolean filePathAlreadyexists = filePath.exists();

                    if (filePathAlreadyexists) {
                        // Play error sound, play the audio in a separate thread
                        Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                        audioThread.start();
                        String[] imagePath = imageFilepath.split("/");
                        String message = imagePath[imagePath.length - 1];
                        String title = Andie.bundle.getString("saveAsError")+ "       ";
                        String fullMessage = Andie.bundle.getString("saveAsFilePathError") + message + Andie.bundle.getString("saveAsFilePathErrorCont");
                        
                        int override = JOptionPane.showConfirmDialog(null, fullMessage, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (override == JOptionPane.NO_OPTION) {
                            // Reload the export option file chooser and return.
                            actionPerformed(e);
                            return;
                        }
                    }
                    // stops recording and saves the operations as a .ops file
                    target.getImage().stopRecording(imageFilepath);
                    fireStateChanged(); // Trigger the change listener to update the menu
                } catch (Exception ex) {
                    // These are the default messages that will come up on the message box.
                    // String title = Andie.bundle.getString("saveAsError");
                    // String message = Andie.bundle.getString("saveAsImageError");
                    // JOptionPane.showMessageDialog(null, message, title,
                    // JOptionPane.ERROR_MESSAGE, Andie.icon);
                }
            }
        }

        // this method is used to add a change listener to the changeListeners list. The
        // listener will receive notifications when the recording state changes.
        public void addChangeListener(ChangeListener listener) {
            changeListeners.add(listener);
        }

        // This method is used to remove a change listener from the changeListeners
        // list.
        public void removeChangeListener(ChangeListener listener) {
            changeListeners.remove(listener);
        }

        // this method iterates over all the registered change listeners in the
        // changeListeners list and invokes the stateChanged() method on each listener,
        // passing a ChangeEvent object as a parameter. This notifies the listeners that
        // the state of the RecordAction has changed.
        // this is used to change the icon back to null once the macro is saved
        private void fireStateChanged() {
            for (ChangeListener listener : changeListeners) {
                listener.stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * Action to open a previously saved macro file and apply its operations to the
     * loaded image
     */
    public class OpenAndApplyAction extends ImageAction {

        /**
         * Creates a new OpenAndApply action
         * 
         * @param name     the name of the action
         * @param icon     an icon to represent the action
         * @param desc     a short description of the action
         * @param mnemonic a mnemonic key to act as a shortcut for the action
         */
        public OpenAndApplyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Method to open and apply a .ops file when an open and apply action is
         * triggered
         * 
         * @param e the actionEvent that triggered the open and apply action
         */
        public void actionPerformed(ActionEvent e) {
            // set style options
            Andie.setOptionStyle();
            // check if an image is actually open
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("MacroError");
                String message = Andie.bundle.getString("MacroErrorDesc")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // check if the image is already recording, then dont allow user to open
            // anything
            if (target.getImage().isRecording()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("RecordingErrorTitle");
                String message = Andie.bundle.getString("alreadyRecordingError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            // custom font and color
            Andie.setJChooserFont(fileChooser.getComponents());
            int result = fileChooser.showOpenDialog(target);
            String macroFile = "";
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    macroFile = fileChooser.getSelectedFile().getCanonicalPath();
                    Stack<ImageOperation> fileMacro = target.getImage().openMacro(macroFile);
                    Stack<ImageOperation> reverseStack = new Stack<ImageOperation>();
                    while(!fileMacro.isEmpty()){
                        reverseStack.push(fileMacro.pop());
                    }
                    //adds the operations from the macro onto the image, in the correct order.  
                    while(!reverseStack.isEmpty()){
                        target.getImage().apply(reverseStack.pop());
                    }
                } catch (Exception ex) {
                    // Play error sound, play the audio in a separate thread
                    Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                    audioThread.start();
                    // System.out.println("ERROR: " + ex.getMessage());
                    String title = Andie.bundle.getString("openError");
                    String[] imagePath = macroFile.split("/");
                    String message = imagePath[imagePath.length - 1] + " " + Andie.bundle.getString("openOpsError")+ "       ";
                    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                }
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Action to cancel a macro recording, which stops the recording and re sets the
     * macro stack without
     * saving the recorded actions
     */
    public class CancelAction extends ImageAction {
        private List<ChangeListener> changeListeners;

        /**
         * Creates a new CancelAction action
         * 
         * @param name     the name of the action
         * @param icon     the icon to represent the action
         * @param desc     a short description of the action
         * @param mnemonic a mnemonic key for a keyboard shortcut for the action
         */
        public CancelAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            changeListeners = new ArrayList<>();
        }

        /**
         * Cancels the recording when a CancelAction is triggered
         * this code will
         * 1. Checks if the target image has an image. If not, displays an error
         * message.
         * 2. Checks if the target image is currently recording. If not, the method
         * returns.
         * 3. cancels the recording process by invoking the cancelRecording() method on
         * the target image.
         * 4. Notify the registered change listeners about the state change by
         * invoking the stateChanged() method on each listener through the
         * fireStateChanged() method.
         * 
         * @param e the ActionEvent that triggered the CancelAction action
         */
        public void actionPerformed(ActionEvent e) {
            // set style options
            // then this checks if the target image associated with the action does not have
            // an image. If that's the case, an error message is displayed
            // if the target image is not currently recording. If it's not recording, the
            // method returns and does nothing further.
            // target.getImage().cancelRecording(): invokes the cancelRecording() method on
            // the target image. This method is responsible for canceling the recording
            // process.
            // then call the firestatechanged to update the menubar that the recording has
            // been cancelled
            Andie.setOptionStyle();
            if (!target.getImage().hasImage()) {
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("MacroError");
                String message = Andie.bundle.getString("MacroErrorDesc")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            if (!target.getImage().isRecording()) { // the macro is not recording, so cancel should do nothing
                return;
            }
            Andie.setOptionStyle();
            target.getImage().cancelRecording();
            fireStateChanged();
        }

        // this method is used to add a change listener to the changeListeners list. The
        // listener will receive notifications when the recording state changes.
        public void addChangeListener(ChangeListener listener) {
            changeListeners.add(listener);
        }

        // This method is used to remove a change listener from the changeListeners
        // list.
        public void removeChangeListener(ChangeListener listener) {
            changeListeners.remove(listener);
        }

        // The fireStateChanged() method iterates over all the registered change
        // listeners and invokes the stateChanged() method on each listener, passing a
        // ChangeEvent object as a parameter. This notifies the listeners that the
        // recording state has changed.I.e that the recroding is cancelled
        private void fireStateChanged() {
            for (ChangeListener listener : changeListeners) {
                listener.stateChanged(new ChangeEvent(this));
            }
        }
    }
}
