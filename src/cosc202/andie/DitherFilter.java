package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;
import java.util.Random;

/**
 * <p>
 * ImageOperation to apply a dither filter to an image.
 * </p>
 * 
 * <p>
 * A class that applies a dither filter to an image. By default uses black and white as the light and dark colors.
 * But, colours can be specified in the constructor.
 * 
 * It applies a technique called "error diffusion dithering" to convert a color image into a black-and-white (or coloured) (grayscale)
 * image using a limited set of colors. The filter aims to create the illusion of continuous shades of gray by 
 * introducing patterns of dots or pixels with different intensities.
 * 
 * Note that some of this code was derived/inspired from https://cmitja.files.wordpress.com/2015/01/hellandtanner_imagedithering11algorithms.pdf, and
 * are all released under a BSD license.s
 * 
 * Code based on a common implementation of the error diffusion dithering algorithm in Java. The createDitherMatrix method was adapted from OpenAI's ChatGPT language model.
 * </p>
 * 
 * @author Hannah Srzich, highly adapted from the MeanFilter class by Steven Mills
 * @version 1.0
 */
public class DitherFilter implements ImageOperation, java.io.Serializable {  
    // Default values for default constructor
    private int ditherLevel = 1; // The higher the dither level the less "dark" random pixels there will be
    Color lightColor = Color.white;
    Color darkColor = Color.black;

    /**
     * Constructs a new instance of the DitherFilter class with default values.
     */
    public DitherFilter() {
    }

    /**
     * Constructs a new instance of the DitherFilter class with the specified parameters.
     *
     * @param ditherLevel The level of dithering to be applied.
     * @param lightColor  The color used for pixels with values greater than or equal to 128.
     * @param darkColor   The color used for pixels with values less than 128.
     */
    public DitherFilter(int ditherLevel, Color lightColor, Color darkColor) {
        this.ditherLevel = ditherLevel;
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }

    /**
     * Applies the dither filter to the given image.
     *
     * @param image The input image to which the filter will be applied.
     * @return The filtered output image.
     */
    public BufferedImage apply(BufferedImage image) {
        // Get dimensions of input image
        int width = image.getWidth();
        int height = image.getHeight();

        // Create output image with the same dimensions as input image
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Get dither matrix
        int[][] ditherMatrix = createDitherMatrix(ditherLevel);

        // Loop over each pixel in the input image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get grayscale value of current pixel
                int oldPixel = getGreyscaleValue(image.getRGB(x, y));

                // Get corresponding value in dither matrix
                int ditherValue = ditherMatrix[x % ditherMatrix.length][y % ditherMatrix.length];

                // Calculate new pixel value
                int newPixel = oldPixel + ditherValue;

                // Set new pixel value to either light or dark color depending on its value
                if (newPixel >= 128) {
                    outputImage.setRGB(x, y, lightColor.getRGB());
                } else {
                    outputImage.setRGB(x, y, darkColor.getRGB());
                }
            }
        }
        // Return the dithered image
        return outputImage;
    }

    /**
     * Creates a dither matrix based on the specified dither level.
     *
     * @param ditherLevel The level of dithering to be applied.
     * @return The generated dither matrix.
     */
    public static int[][] createDitherMatrix(int ditherLevel) {
        int size = ditherLevel * ditherLevel;
        int[] matrix = new int[size];

        // Initialize the matrix with threshold values (0 or 255)
        for (int i = 0; i < size; i++) {
            matrix[i] = i < 255 / 2 ? 0 : 255;
        }

        // Shuffle the matrix using the Fisher-Yates algorithm
        shuffleArray(matrix);

        int[][] ditherMatrix = new int[ditherLevel][ditherLevel];

        // Fill the dither matrix with the shuffled values from the matrix array
        for (int i = 0; i < ditherLevel; i++) {
            for (int j = 0; j < ditherLevel; j++) {

                // Set the elements of the matrix array to 0 for the first half of the array and 255 for the second half
                // Initializes the matrix with threshold values, where the first half is light and the second half dark
                ditherMatrix[i][j] = matrix[i * ditherLevel + j];
            }
        }
        return ditherMatrix;
    }
    
    /**
     * Shuffles the elements of an integer array using the Fisher-Yates algorithm.
     *
     * @param arr The array to be shuffled.
     */
    private static void shuffleArray(int[] arr) {
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * Returns the greyscale value of a given pixel in the range [0, 255].
     * 
     * @param pixel The pixel whose greyscale value is to be calculated.
     * @return The greyscale value of the pixel.
     */
    private static int getGreyscaleValue(int pixel) {
        // Get the RGB values of the pixel
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;
        
        // Calculate the greyscale value using the formula: Y = 0.299 R + 0.587 G + 0.114 B
        int grey = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        
        return grey;
    }


}