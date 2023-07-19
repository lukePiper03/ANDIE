package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * <p>
 * Actions provided by the File menu.
 * </p>
 * 
 * <p>
 * The File menu is very common across applications, 
 * and there are several items that the user will expect to find here.
 * Opening and saving files is an obvious one, but also exiting the program.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Hannah Srzich adapted from Steven Mills
 * @version 1.0
 */
public class FileActions {
    
    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;
    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        actions = new ArrayList<Action>();
        actions.add(new FileOpenAction(Andie.bundle.getString("open"), null, Andie.bundle.getString("openDesc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction(Andie.bundle.getString("save"), null, Andie.bundle.getString("saveDesc"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction(Andie.bundle.getString("saveAs"), null, Andie.bundle.getString("saveAsDesc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(Andie.bundle.getString("export"), null, Andie.bundle.getString("exportDesc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new FileExitAction(Andie.bundle.getString("exit"), null, Andie.bundle.getString("exitDesc"), Integer.valueOf(KeyEvent.VK_X)));
    }
    /**
     * <p>
     * Create a menu containing the list of File actions.
     * This will then create a mnemonic key, which acts as a keyboard shortcut, that, when pressed along
     * with the appropriate modifier keys, triggers the menu item action.
     * The accelerator key, on the other hand, represents a keyboard shortcut that
     * combines one or more modifier keys (such as Ctrl, Shift)
     * with a specific key or character. It allows the user to perform the
     * associated action directly from the keyboard without navigating through the
     * menu structure.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Andie.bundle.getString("file"));
        for (Action action: actions) {
            JMenuItem menuItem = new JMenuItem(action);
            menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(), KeyEvent.CTRL_DOWN_MASK));
            fileMenu.add(menuItem);
        }
        return fileMenu;
    }
    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {
        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        public FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // if we are currently recording dont not allow using to open a new image
            if(target.getImage().recording){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("openError");
                String message = Andie.bundle.getString("fileRecordError") + "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            ImagePanel.region = null;
            JFileChooser fileChooser = new JFileChooser();
            Andie.setJChooserFont(fileChooser.getComponents());
            int result = fileChooser.showOpenDialog(target);
            String imageFilepath = "";
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    // Check is there is anything on the ops stack
                    if(target.getImage().getOpsSize() > 0){
                        // Play error sound, play the audio in a separate thread
                        Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                        audioThread.start();
                        String title = Andie.bundle.getString("open");
                        String message = Andie.bundle.getString("openCheck")+ "       ";
                        
                        int checkOpen = JOptionPane.showConfirmDialog(null,message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon);
                        if(checkOpen == JOptionPane.YES_OPTION){
                            target.getImage().open(imageFilepath);
                        }
                    } else { // if there is not anything on the ops stack, open the image
                        target.getImage().open(imageFilepath);
                    }
                } catch (Exception ex) {
                    // Play error sound, play the audio in a separate thread
                    Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                    audioThread.start();
                    String title = Andie.bundle.getString("openError");
                    String[] imagePath = imageFilepath.split("/");
                    String message = imagePath[imagePath.length - 1] + " " + Andie.bundle.getString("openTypeError")+ "       ";
                    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                }
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }
    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {
        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the file-save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAction is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // if we are currently recording don't not allow using to open a new image
            if(target.getImage().recording){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("saveError");
                String message = Andie.bundle.getString("fileRecordError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Check if an image exists.
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                // These are the default messages that will come up on the message box.
                String title = Andie.bundle.getString("saveError");
                String message = Andie.bundle.getString("exportImageError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            try {
                target.getImage().save();           
            } catch (Exception ex) {
                // Play error sound, play the audio in a separate thread
                //Thread audioThread = new Thread(() -> Andie.playErrorSound());
                //audioThread.start();
                //String title = Andie.bundle.getString("saveError");
                //String message = Andie.bundle.getString("saveImageError")+ "       ";
                //JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            }
        }
    }
    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {
        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
         /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // if we are currently recording do not allow using to open a new image
            if(target.getImage().recording){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("saveAsError");
                String message = Andie.bundle.getString("fileRecordError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Check if an image exists.
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                // These are the default messages that will come up on the message box.
                String title = Andie.bundle.getString("saveAsError");
                String message = Andie.bundle.getString("exportImageError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            Andie.setJChooserFont(fileChooser.getComponents());
            int result = fileChooser.showSaveDialog(target);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    // Check if the file path already exists.
                    File filePath = new File(imageFilepath+".ops");
                    boolean filePathAlreadyexists = filePath.exists();
                    if(filePathAlreadyexists){ 
                        // Play error sound, play the audio in a separate thread
                        Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                        audioThread.start();
                        String[] imagePath = imageFilepath.split("/");
                        String message = imagePath[imagePath.length - 1];
                        String title = Andie.bundle.getString("saveAsError");
                        String fullMessage = Andie.bundle.getString("saveAsFilePathError") + message + Andie.bundle.getString("saveAsFilePathErrorCont")+ "       ";
                        int override = JOptionPane.showConfirmDialog(null,fullMessage, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon);
                        if(override == JOptionPane.NO_OPTION){
                            // Reload the export option file chooser and return.
                            actionPerformed(e);
                            return;
                        }
                    }
                    // Export image.
                    target.getImage().saveAs(imageFilepath);
                } catch (Exception ex) {
                    // Play error sound, play the audio in a separate thread
                    Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                    audioThread.start();
                    // These are the default messages that will come up on the message box.
                    String title = Andie.bundle.getString("saveAsError");
                    String message = Andie.bundle.getString("saveAsImageError")+ "       ";
                    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                }
            }
        }
    }
    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends ImageAction {
        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            //super(name, icon);
            super(name, icon, desc, mnemonic);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
         /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExitAction is triggered.
         * It quits the program.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // if there are no ops on the stack just exit
            if(!target.getImage().hasOps()){
                // Play exit sound, play the audio in a separate thread
                Andie.sound.playExitSound();
                System.exit(0);
                return;
            }
            // If operations arent there ask user if they want to exit.
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("exit");
            String message = Andie.bundle.getString("exitCheck")+ "       ";
            int result = JOptionPane.showConfirmDialog(null,message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon);
            if(result == JOptionPane.YES_OPTION){
                // Play exit sound, play the audio in a separate thread
                Andie.sound.playExitSound();
                System.exit(0);
            }
        }
    }
     /**
     * <p>
     * Action to export an image to a new file location.
     * </p>
     * 
     */
    public class FileExportAction extends ImageAction {

        private File filePath = null;
        /**
         * <p>
         * Create a new FileExportAction.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        public FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the FileExportAction is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExportAction is triggered.
         * It prompts the user to select a file and exports the current image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.setOptionStyle();
            // if we are currently recording don't not allow using to open a new image
            if(target.getImage().recording){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                String title = Andie.bundle.getString("exportError");
                String message = Andie.bundle.getString("fileRecordError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            // Check if an image exists.
            if(!target.getImage().hasImage()){
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                audioThread.start();
                // These are the default messages that will come up on the message box.
                String title = Andie.bundle.getString("exportError");
                String message = Andie.bundle.getString("exportImageError")+ "       ";
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            Andie.setJChooserFont(fileChooser.getComponents());
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Images", "png");
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG Images", "jpg", "jpeg");
            FileNameExtensionFilter gifFilter = new FileNameExtensionFilter("GIF Images", "gif");
            fileChooser.addChoosableFileFilter(pngFilter);
            fileChooser.addChoosableFileFilter(jpgFilter);
            fileChooser.addChoosableFileFilter(gifFilter);
            fileChooser.setFileFilter(pngFilter);
            int result = fileChooser.showSaveDialog(target);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    // Check if the file path already exists.
                    String extension = "";
                    String fileType = fileChooser.getFileFilter().getDescription();
                    // If user selects all files, default png.
                    if (fileType.equals("PNG Images") || fileType.equals("All Files")) {
                        extension = "png";
                    } else if (fileType.equals("JPG Images")) {
                        extension = "jpg";
                    } else if (fileType.equals("GIF Images")) {
                        extension = "gif";
                    }
                    filePath = new File(imageFilepath + "." + extension);
                    boolean filePathAlreadyexists = filePath.exists();
                    if(filePathAlreadyexists){ 
                        // Play error sound, play the audio in a separate thread
                        Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                        audioThread.start();
                        String[] imagePath = imageFilepath.split("/");
                        String message = imagePath[imagePath.length - 1];
                        String title = Andie.bundle.getString("exportError");
                        String fullMessage = Andie.bundle.getString("saveAsFilePathError") + message + Andie.bundle.getString("exportFilePathErrorCont")+ "       ";
                        int override = JOptionPane.showConfirmDialog(null,fullMessage, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon);
                        if(override == JOptionPane.NO_OPTION){
                            // Reload the export option file chooser and return.
                            actionPerformed(e);
                            return;
                        }
                    }
                    // Export image.
                    if (extension.equals("png")) {
                        target.getImage().export(imageFilepath, "png");
                    } else if (extension.equals("jpg")) {
                        target.getImage().export(imageFilepath, "jpg");
                    } else if (extension.equals("gif")) {
                        target.getImage().export(imageFilepath, "gif");
                    } // Else deal with it!
                } catch (Exception ex) {
                    // Play error sound, play the audio in a separate thread
                    Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
                    audioThread.start();
                    // These are the default messages that will come up on the message box.
                    String title = Andie.bundle.getString("exportError");
                    String message = Andie.bundle.getString("exportImageError")+ "       ";
                    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
                }
            }
        }
        
        /**
         * Returns the File object that represents the file path of the current image.
         * @return the File object representing the file path of the current image.
         */
        public File getFilePath(){
            return filePath;
        }
    }
}

