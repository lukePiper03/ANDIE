package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to
 * it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can
 * be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always
 * undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original
 * image
 * and the result of applying the current set of operations to it.
 * The operations themselves are stored on a {@link Stack}, with a second
 * {@link Stack}
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills edited by Hannah Srzich and Dante Vannini
 * @version 1.0
 */
public class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /**
     * The current image, the result of applying {@link ops} to {@link original}.
     */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    public Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;
    /** The sequence of operations we wish to record with the macro */
    public Stack<ImageOperation> macro;
    /** tells us if the macro is recording so we know weather to add actions to it */
    public boolean recording;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack
     * of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;
        macro = new Stack<ImageOperation>();
        recording = false;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Check if there are any operations on the current stack
     * </p>
     * 
     * @return True if there are ops, false otherwise.
     */
    public boolean hasOps() {
        if (this.ops.empty()) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Resize the window to fit the image when any action is performed
     * that alters the image.
     * </p>
     *
     */
    public void resizeWindow() {
        // Make window resize to image size, note that the
        // added pixel lengths are for aesthetics of scroll bars
        int width = current.getWidth() + 4;
        int height = current.getHeight() + 54;

        // Get the default toolkit
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the screen size as a dimension object
        Dimension screenSize = toolkit.getScreenSize();

        // Get the maximum width and height in pixels
        int maxWidth = screenSize.width;
        int maxHeight = screenSize.height;

        // make sure the length and height of the window does not cut the menu off
        // or that the frame does not get too big to fit on the screen
        if (width < 684) {
            width = 684;
        } else if (width > maxWidth) {
            width = maxWidth;
        }
        if (height < 538) {
            height = 538;
        } else if (height > maxHeight) {
            height = maxHeight;
        }

        Andie.frame.setSize(new Dimension(width, height));
        // Make window centred when changed image
        Andie.frame.setLocationRelativeTo(null);
    }

    /**
     * <p>
     * Resize the window to fit the image when any action is performed
     * that alters the image with selected dimension.
     * </p>
     *
     */
    public void resizeWindow(Dimension dim) {
        int width = (int) dim.getWidth() + 4;
        int height = (int) dim.getHeight() + 54;
        Andie.frame.setSize(new Dimension(width, height));
        // Make window centered when changed image
        Andie.frame.setLocationRelativeTo(null);
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage.
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that
     * assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally
     * used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so
     * the
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knoweldge of some details about the internals of the
     * BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href=
     * "https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to
     * <a href=
     * "https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under
     * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public EditableImage deepCopyEditable() {
        EditableImage newImage = new EditableImage();
        Stack<ImageOperation> newRedoOps = new Stack<>();
        for (ImageOperation op : redoOps) {
            newRedoOps.push(op);
        }
        Stack<ImageOperation> newOps = new Stack<>();
        for (ImageOperation op : ops) {
            newOps.push(op);
        }
        newImage.original = deepCopy(original);
        newImage.current = deepCopy(current);
        newImage.ops = newOps;
        newImage.redoOps = newRedoOps;
        newImage.imageFilename = null;
        newImage.opsFilename = null;
        return newImage;
    }

    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code>
     * added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try
     * to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        original = ImageIO.read(imageFile);
        current = deepCopy(original);
        ops = new Stack<ImageOperation>(); // clear the ops
        redoOps = new Stack<ImageOperation>();

        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            objIn.close();
            fileIn.close();
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
        }
        this.refresh();
    }
    /**
     * The `makeFrameDimensions` method is responsible for adjusting the dimensions and position of the main application frame
     * based on the size of the current image.
     * 
     * @param current the current image that is being displayed
     */
    public static void makeFrameDimensions(BufferedImage current) {
        
        // Make window resize to image size, note that the
        // added pixel lengths are for aesthetics of scroll bars
        int width = current.getWidth() + 4;
        int height = current.getHeight() + 54;

        // Get the default toolkit
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the screen size as a dimension object
        Dimension screenSize = toolkit.getScreenSize();

        // Get the maximum width and height in pixels
        int maxWidth = screenSize.width;
        int maxHeight = screenSize.height;

        // make sure the length and height of the window does not cut the menu off
        // or that the frame does not get too big to fit on the screen
        if (width < 684) {
            width = 684;
        } else if (width > maxWidth) {
            width = maxWidth;
        }
        if (height < 538) {
            height = 538;
        } else if (height > maxHeight) {
            height = maxHeight;
        }

        Andie.frame.setSize(new Dimension(width, height));
        // Make window centered when changed image
        Andie.frame.setLocationRelativeTo(null);
        
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved
     * as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1 + imageFilename.lastIndexOf(".")).toLowerCase();
        ImageIO.write(original, extension, new File(imageFilename));
        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
    }

    /**
     * <p>
     * Save an image to a speficied file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        this.imageFilename = imageFilename;
        this.opsFilename = imageFilename + ".ops";
        save();
    }

    /**
     * <p>
     * Exports an the current image to the file provided as a parameter with the
     * specified extension.
     * </p>
     * 
     * @param imageFilename The file location to export the image to.
     * @param extension     The extension of the image file format to export as,
     *                      e.g. "png", "jpg", "gif".
     * @throws RuntimeException         if an unexpected error occurs while writing
     *                                  the image.
     * @throws IOException              if an error occurs while exporting the
     *                                  image.
     * @throws IllegalArgumentException if the provided extension is not supported.
     * @author Hannah Srzich, adapted from Werner Kvalem Vesterås
     */
    public void export(String imageFilename, String extension) throws IOException {
        try {
            BufferedImage currentImage = getCurrentImage(); // Retrieve image.
            File outputFile = new File(imageFilename + "." + extension);
            if ("png".equalsIgnoreCase(extension)) {
                if (!ImageIO.write(currentImage, "png", outputFile)) {
                    throw new RuntimeException(Andie.bundle.getString("writeError"));
                }
            } else if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
                if (!ImageIO.write(currentImage, "jpeg", outputFile)) {
                    throw new RuntimeException(Andie.bundle.getString("writeError"));
                }
            } else if ("gif".equalsIgnoreCase(extension)) {
                if (!ImageIO.write(currentImage, "gif", outputFile)) {
                    throw new RuntimeException(Andie.bundle.getString("writeError"));
                }
            } else {
                throw new IllegalArgumentException(Andie.bundle.getString("unsupError") + " " + extension);
            }
        } catch (IOException e) {
            System.err.println(Andie.bundle.getString("expError") + " " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        current = op.apply(current);
        // add to ops
        ops.add(op);
        if (recording) {
            macro.add(op);
        }
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() throws Exception {
        try {
            redoOps.push(ops.pop());
            if(recording){
                macro.pop();
            }
        } catch (EmptyStackException e) {
            throw new Exception(Andie.bundle.getString("undoErrorNoneMessage"));
        }
        refresh();
    }

    /**
     * <p>
     * Undo all {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undoAll() {
        // Put all operations on the redo ops stack.
        boolean loop = true;
        if(recording){
            macro = new Stack<ImageOperation>();
        }
        while (loop) {
            try {
                redoOps.push(ops.pop());
            } catch (EmptyStackException e) {
                loop = false;
            }
            refresh();
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo() throws Exception {
        try {
            if(recording){
                macro.push(redoOps.peek());
            }
            apply(redoOps.pop());
        } catch (EmptyStackException e) {
            throw new Exception(Andie.bundle.getString("undoErrorNoneMessage"));
        }
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the
     *         {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Get the current ops stack size.
     * </p>
     * 
     * @return The size of the current ops stack.
     */
    public int getOpsSize() {
        return ops.size();
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in
     * sequence.
     * This is useful when undoing changes to the image, or in any other case where
     * {@link current}
     * cannot be easily incrementally updated.
     * </p>
     */
    private void refresh() {
        boolean wasRecording = recording;
        Stack<ImageOperation> saveMacro = macro;
        current = deepCopy(original);
        for (ImageOperation op : ops) {
            current = op.apply(current);
        }
        recording = wasRecording;
        macro = saveMacro;
    }

    /**
     * Method to check if the macro is recording
     * @return returns true if the macro is recording, false if not
     */
    public boolean isRecording(){
        return recording;
    }

    public Stack<ImageOperation> getMacro(){
        return macro;
    }


    /**
     * Starts the recording of a macro so that all future operations performed on 
     * the image will be saved to the macro stack
     */
    public void record(){
        recording = true;
        // set new macro stack
        this.macro = new Stack<ImageOperation>();
    }

    /**
     * Stops the macro from recording, and saves the recorded actions as a .ops file
     * 
     * @param fileName the name of the file we will save the macro operations into
     * @throws Exception
     */
    public void stopRecording(String fileName) throws Exception {
        recording = false;
        FileOutputStream fileOut = new FileOutputStream(fileName + ".ops");
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(macro);
        macro = null;
        objOut.close();
        fileOut.close();
    }

    /**
     * Class to open a .ops file and apply the operations stored in it.
     * 
     * @param fileName the name of the .ops file we wish to open
     * @return
     */
    public Stack<ImageOperation> openMacro(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            objIn.close();
            fileIn.close();
            
            return opsFromFile;
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
        }
        return null;
    }

    /**
     * Method to allow the uswer to cancel recording when recording a macro. This re
     * sets the macro,
     * and sets recording to false
     */
    public void cancelRecording(){
        recording = false;
        macro = new Stack<ImageOperation>();
        
    }

    /**
     * Method to allow operations to be added to the ops stack
     * from the macroActions class. this is needed when opening a 
     * macro, as we need to add all the operations from the .ops file
     * into the ops stack of the image
     * @param op the operation we wish to add to the ops stack
     */
    public void addToOps(ImageOperation op){
        ops.push(op);
    }
}