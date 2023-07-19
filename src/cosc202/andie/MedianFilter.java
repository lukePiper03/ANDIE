package cosc202.andie;

import java.awt.image.*;
import java.util.*;
/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * A Median filter blurs an image by replacing each central pixel by the median of the
 * pixels in a surrounding local neighborhood of a given radius.
 * A median filter is more effective than convolution when the goal is to 
 * simultaneously reduce "salt and pepper" noise and preserve edges.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Hannah Srzich, adapted from Mean Filter by Steven Mills
 * @version 1.0
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;
    /**
     * <p>
     * Construct a Median filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the local neighbourhood used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    public MedianFilter(int radius) {
        this.radius = radius;    
    }
    /**
     * <p>
     * Construct a Median filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     * 
     * @see MedianFilter(int)
     */
    public MedianFilter() {
        this(1);
    }
    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     * 
     * <p>
     * The Median filter is is a nonlinear operation, not implemented via convolution.
     * The size of the local neighbourhood is specified by the {@link radius}.  
     * Larger radii leads to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Median filter to.
     * @return The resulting (blurred) image.
     */
    public BufferedImage apply(BufferedImage input) {
        int edge = radius + 1;
        int crop = edge + 2;
        // extened the image by edge number of pixels on each size
        input = ImageEdgeExtension.extendImage(input, crop);

        // Create a BufferedImage output image.
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        int numberNeighbours = (1+(radius*2))*(1+(radius*2)); // The number of cells contained in the radius r.
        int neighbour = 0; // An index counter for each array of a, r, g, b neighbours.
         
        int center = (numberNeighbours - 1)/2; // Index of the median value once the arrays are sorted.
        // Initalize arrays of length "number of neighbours."
        int[] medianA = new int [numberNeighbours];
        int[] medianR = new int [numberNeighbours];
        int[] medianG = new int [numberNeighbours];
        int[] medianB = new int [numberNeighbours];

        

        /*
         * Note that for now we iterate over the entire images pixels,
         * excluding edge cases, still need to deal with the edge cases. TBC.
         */
        for (int y = edge; y < input.getHeight() - edge; ++y) {
            for (int x = edge; x < input.getWidth() - edge; ++x) {
                /*
                 * Iterate over the local neighbours, adding their a, r, g, b 
                 * values respectively into the median arrays. Starting from the
                 * top left corner of the local neighbourhood.
                 */ 
                
                for (int i = x - radius; i <= x + radius; i++){
                    for(int j = y - radius; j <= y + radius; j++){
                        // Unpacking a, r, g, b from argb into their arrays.
                        try{
                            int argb = input.getRGB(i, j);
                            medianA[neighbour] = (argb & 0xFF000000) >> 24;
                            medianR[neighbour] = (argb & 0x00FF0000) >> 16;
                            medianG[neighbour] = (argb & 0x0000FF00) >> 8;
                            medianB[neighbour] = (argb & 0x000000FF);
                        }catch(ArrayIndexOutOfBoundsException e){
                            /*If accessing a pixel value outside image boundaries, 
                              (which in practice never happens), leave the pixel value as is.
                            */
                        }
                        neighbour++;
                    }
                }
                /*
                 * Iterate over the local neighbours, adding their a, r, g, b 
                 * values respectively into the median arrays. Starting from the
                 * top left corner of the local neighbourhood.
                 */ 
                Arrays.sort(medianA);
                Arrays.sort(medianR);
                Arrays.sort(medianG);
                Arrays.sort(medianB);
                int a = medianA[center];
                int r = medianR[center];
                int g = medianG[center];
                int b = medianB[center];
                
                // Packing a, r, g, b into argb int.
                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                output.setRGB(x, y, argb);
                // Reset the neighbour array index counter and count for the next loop.
                neighbour = 0;
            }
        }
        // crop the image back to origional size
        output = ImageEdgeExtension.cropImage(output, crop);
        return output;
    }
}
