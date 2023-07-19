package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 * 
 * <p>
 * A gaussian filter blurs an image by replacing each pixel by that provided by the
 * gaussuan function, then using the avergae of this and can be implemented by a convoloution.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Eszter Scarlett Herbert, adapted from Mean Filter by Steven Mills
 * @version 2.0
 */

public class GaussianBlurFilter implements ImageOperation, java.io.Serializable {
    /*
     * creates an int radius and a floar sigma which
     * will be used in the equations/ specified by the radius the user enters
     */
    private int radius;
    private float sigma;

    /**
     * is going to take in 
     * radius: the radius of the gaussian blur that the
     * user has entered,
     * sigma: is going to be a float variable which is equal to the radius divided by three
     */
    public GaussianBlurFilter(int radius) {
        this.radius = radius;
        this.sigma = radius / 3.0f;
    }

    /**@param input The image to apply the Sharpen filter to.
     * @return The resulting (blurred) image. */
    public BufferedImage apply(BufferedImage input) {
        int crop = radius + 3;
        // extened the image by edge number of pixels on each size
        input = ImageEdgeExtension.extendImage(input, crop);

        /*
         * creates an int called size, which will be created to ensure that the size
         * will always be an odd number- and thus there will be a middle
         * then creates a double array of kernelData, where the grid of the image/
         * radius will always be an odd number
         * additionally the array will have with a length equal to the size of the size
         * squared.
         * the float sum, starts as zero and will be used afterwards to divide the blur
         * the centerIndex
         */
        int size = (2 * radius + 1);
        float[][] kernelData = new float[size][size];
        float sum = 0.0f;

        /*
         * this is used to determine where the center of the kernel will be, the center
         * of the matrix is when the row and column parts are equal to the radius
         * so for example, if the radius is 3, then the size will be 7 x 7, and the
         * centre of
         * x and y will be 3, 3
         */
        int centerOfKernel = radius;

        /*
         * where the row is less than the size, iterate through the row
         * where the column is less than the size, iterate through the column
         */
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                // will determine the distance of the current x- position (the row) from the
                // center of the image
                // then will determine the distance of the current y- position (the column) from
                // the center of the image
                float distancex = (float) row - centerOfKernel;
                float distancey = (float) col - centerOfKernel;

                // the constant part of the gaussian blur is (1.0 / (2 * Math.PI * sigma)) ;
                // then the exponent part of the gaussian blur is (-(Math.pow(distance, 2) +
                // Math.pow(distance, 2)) / (2 * Math.pow( sigma, 2)));
                // this combines the two equations together is that the kernel value in this
                // position is equal to the equation
                // then this value is added to the sum which will be used later on to create the
                // actual blur
                kernelData[row][col] = (float) ((1.0 / (2 * Math.PI * sigma))
                        * Math.exp(-(Math.pow(distancex, 2) + Math.pow(distancey, 2)) / (2 * Math.pow(sigma, 2))));
                sum += kernelData[row][col];
            }
        }

        /*
         * the kernel values become 'normalised' where
         * the kernel data for each pixel is calculated by dividing through by the sum
         * so that the filter values do not add up to more than one and the image doesn't
         * get brighter
         */
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                kernelData[row][col] /= sum;
            }
        }

        /* the 2d array needs to be flattened into a 1d array so it can be recognised by the kernel */
        float finalKernelArray[] = new float[kernelData.length * kernelData[0].length];
        int counter = 0;

        for (int row = 0; row < kernelData.length; row++) {
            for (int col = 0; col < kernelData[row].length; col++) {
                finalKernelArray[counter] = kernelData[row][col];
                counter++;
            }
        }

        /**
         * this is going to apply the kernel, by creating a new kernel object of a
         * specified size, and the matrix of values stored in the final kernel array
         * to reduce the appearance of the black from the average calculation being low because of the "missing" pixels off each side
         * mirrored padding is used. border size is to use for the padded dimensions which is equal to
         * half the size of the input image
         */
        Kernel kernel = new Kernel(size, size, finalKernelArray);
        int borderSize = size / 2;

        /*
         * creates a new BufferedImage called paddedInput with the size of the input
         * image plus a border of size borderSize on each side. The getType() method
         * returns the type of the input image
         */
        BufferedImage paddedInput = new BufferedImage(
                input.getWidth() + borderSize * 2,
                input.getHeight() + borderSize * 2,
                input.getType());

        /*
         * creates a Graphics2D object from the paddedInput image and draws the original
         * input image at the center of the paddedInput image.
         * @param input: the input image to copy from 
         * @param dx1, dy1: the x and y coordinates of the (desired corner) of the output rectangle
         * @param borderSize, borderSize: the width and height of the output rectangle
         * @param sx1, sx2: the x and y coordinates of the (desired corner) of the source rectangle
         * @param borderSize, borderSize: the width and height of the source rectangle
         * @param null: ImageObserver, which is not needed
         */
        Graphics2D g = paddedInput.createGraphics();
        g.drawImage(input, borderSize, borderSize, null);

        /** copy the left and right, top and bottom borders of the input image to the padded image.*/
        g.drawImage(input, 0, borderSize, borderSize, input.getHeight(), borderSize, 0, 0, input.getHeight(), null); // left border
        g.drawImage(input, input.getWidth() - borderSize, borderSize, input.getWidth(), input.getHeight(),
                input.getWidth() - borderSize, 0, input.getWidth(), input.getHeight(), null); // right border
        g.drawImage(input, borderSize, 0, input.getWidth(), borderSize, 0, borderSize, input.getWidth(), 0, null); // top border
        g.drawImage(input, borderSize, input.getHeight() - borderSize, input.getWidth(), input.getHeight(), 0,
                input.getHeight() - borderSize, input.getWidth(), input.getHeight(), null); // bottom border

        /**
         * copy the four corners of the input image to the padded image.
         * @param input: the input image to copy from 
         * @param dx1, dy1: the x and y coordinates of the (desired corner) of the output rectangle
         * @param borderSize, borderSize: the width and height of the output rectangle
         * @param sx1, sx2: the x and y coordinates of the (desired corner) of the source rectangle
         * @param borderSize, borderSize: the width and height of the source rectangle
         * @param null: ImageObserver, which is not needed
         */
        g.drawImage(input, 0, 0, borderSize, borderSize, 0, 0, borderSize, borderSize, null); // top left
        g.drawImage(input, input.getWidth() - borderSize, 0, input.getWidth(), borderSize,
                input.getWidth() - borderSize, 0, input.getWidth(), borderSize, null); // top right
        g.drawImage(input, 0, input.getHeight() - borderSize, borderSize, input.getHeight(), 0,
                input.getHeight() - borderSize, borderSize, input.getHeight(), null); // bottom left
        g.drawImage(input, input.getWidth() - borderSize, input.getHeight() - borderSize, input.getWidth(),
                input.getHeight(), input.getWidth() - borderSize, input.getHeight() - borderSize, input.getWidth(),
                input.getHeight(), null); // bottom right

        /* releases the system resources used by the Graphics2D object. */
        g.dispose();

        /*
         * apply the ConvolveOp to the padded image and crop the output
         * image to remove the padded pixels.
         */
        ConvolveOp op = new ConvolveOp(kernel);
        BufferedImage output = op.filter(paddedInput, null);

        /*
         * returns a cropped image that excludes the border pixels, The output image has
         * the same size as the input image.
         */
        output = output.getSubimage(borderSize, borderSize, input.getWidth(), input.getHeight());
        // crop the image back to original size
        output = ImageEdgeExtension.cropImage(output, crop);

        return output;

    }
}
