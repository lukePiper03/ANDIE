package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * RotateImage class rotates an input image 90 degrees to the right or left or 180 degrees depending on the user preference
 */
public class RotateImage implements ImageOperation, java.io.Serializable {

    // Booleans to check which rotation is happening in apply
    boolean isRight;
    boolean is180;

    /**
     * RotateImage constructor
     * @param isRight
     * @param is180
     *
     */
    public RotateImage(boolean isRight, boolean is180) {
        this.isRight = isRight;
        this.is180 = is180;
    }

    /**
     * Apply the rotation to the specified image
     * @param input
     * @return Buffered image with filter 
     */
    public BufferedImage apply(BufferedImage input) {
        // Find height and width of image
        int rows = input.getHeight();
        int cols = input.getWidth();

        // Create an emtpy 2D array
        int [][] test90 = new int[rows][cols];
        int [][] test180 = new int[cols][rows];

        // Create a copy of image
        BufferedImage rotate180Image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotate90Image = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_ARGB);
        
        // Flip image 180
        if(is180) {
            // Loop through input
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // Store current pixel in testarray
                    test180[cols-col-1][rows-row-1] = input.getRGB(col, row);
                }
            }
            rotate180Image = setRotatedImage(rotate180Image, test180, is180, rows, cols);
            return rotate180Image;
        } else {
            // Loop through every pixel in image
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // If rotation is 90 degrees to the right
                    if(isRight) {
                        // Make after rotation pixel set to current input pixel
                        test90[rows-row-1][col] = input.getRGB(col, row);
                        // Otherwise rotate 90 degrees to the left
                    } else {
                        // Make after rotation pixel set to current input pixel
                        test90[row][cols-col-1] = input.getRGB(col, row);
                    }
                }
            }
            // Copy image over
            rotate90Image = setRotatedImage(rotate90Image, test90, is180, rows, cols);
            // Set input to new rotatedImage
            return rotate90Image;
        }
    }

    /**
     * Set the pixels of the new image to the array of pixels in rotated image
     * @param input
     * @param cloneArray
     * @param is180
     * @param rows
     * @param cols
     * @return new rotated image
     */
    public BufferedImage setRotatedImage(BufferedImage input, int[][]cloneArray, boolean is180, int rows, int cols) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if(is180) {
                    // Grab each x and y from array and store as pixel
                    int pixel = cloneArray[y][x];
                    // Set each pixel of the image image
                    input.setRGB(y, x, pixel);
                } else {
                    // Grab each x and y from array and store as pixel
                    int pixel = cloneArray[x][y];
                    // Set each pixel of the image image
                    input.setRGB(x, y, pixel);
                }            
            }
        }
        // Return new image
        return input;
    }
}