package cosc202.andie;

import java.awt.image.*;


/**
 * Class to apply flipImage to a given image, either vertically or horizontally
 */
public class FlipImage implements ImageOperation, java.io.Serializable {
    
    // Boolean to check if flip is vertical or horizontal
    boolean isVertical;
    
    /**
     * FlipImage Constructor
     * @param h
     */
    public FlipImage(boolean h) {
        isVertical = h;    
    }

    /**
     * Apply method to flip the specified image vertically or horizontally
     * @param input
     * @return new flipped buffered image
     */
    public BufferedImage apply(BufferedImage input) {
        // Find height and width of image
        int rows = input.getHeight();
        int cols = input.getWidth();

        // Create an empty 2D array
        int[][] test = new int[rows][cols];

        // Create a copy of image
        BufferedImage testImage = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);

        // Vertical flip
        if(isVertical) {
            // Loop through input image
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // Set test array location to vertically flipped position
                    test[row][col] = input.getRGB(col, rows - row - 1);
                }
            }
        } else {
            // Horizontal Flip
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // Set test array location to horizontally flipped position
                    test[row][col] = input.getRGB(cols - col - 1, row);
                }
            }
        }

        // Loop through test array
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                // Grab each x and y from array and store as pixel
                int pixel = test[y][x];
                // Set each pixel of the testImage
                testImage.setRGB(x, y, pixel);
            }
        }

        // Store input as new rotated image
        input = testImage;
        return input;
    }
}
