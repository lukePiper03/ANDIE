package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a sharpness filter.
 * </p>
 * 
 * <p>
 * A sharpness filter makes each pixel like its neighbours it enhances the
 * differences between neighbouring value, and can be implemented by a
 * convoloution.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Eszter Scerlett-Herbert, highly adapted from the Mean filter by Steven Mills
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {
    
    boolean rescaled = true; // boolean value to determine whether the image should be rescaled or not in the range {0,255}  

    /**
     * <p>
     * Default Constructer for the Sharpness filter
     * </p>
     */
    public SharpenFilter() {
    }

    /**
     * Constructer for the Sharpness filter
     * @param rescaled: boolean value to determine whether the image should be clipped or not in the range {0,255}
     */
    public SharpenFilter(boolean rescaled) {
        this.rescaled = rescaled;
    }

    /**
     * <p>
     * Apply a Sharpness filter to an image.
     * </p>
     * <p>
     * As with many filters, the Sharpness filter is implemented via convolution.
     * This method takes a BufferedImage object as input and applies a convolution
     * filter to it. It then returns the resulting (filtered) image.
     * </p>
     * 
     * <ol>
     * <li>firstly the height and width of the image are calculated and padding is
     * initialised</li>
     * <li>then create BufferedImage called paddedImage with with added padding on all sides.</li>
     * <li>Graphics2D object g2d will then draw the input image onto the padded image with padding.the dispose step frees up space.</li>
     * <li>an array of float values called "array" is created, which represent the kernel values.</li>
     * <li>A new Kernel object is created using the "array" values.</li>
     * <li> then the edges and corners are mirrored in the padding </li>
     * <li>a ConvolveOp object called convolve is created with the kernerl,</li>
     * <li>then create a new BufferedImage called output with the same color model
     * as the input image but with no padding. then apply the convolve operation and
     * store the result in output</li>
     * </ol>
     * 
     * @param input The image to apply the Sharpen filter to.
     * @return The resulting (sharpened)) image.
     */
    public BufferedImage apply(BufferedImage input) {

        int w = input.getWidth();
        int h = input.getHeight();
        int padding = 1;
    
        // Create a padded image
        BufferedImage paddedImage = new BufferedImage(w + 2 * padding, h + 2 * padding, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = paddedImage.createGraphics();
    
        // Draw input image onto padded image, cropping to fit within padded image bounds
        g2d.drawImage(input, padding, padding, w, h, null);
    
        g2d.dispose();
    
        // Create the kernel
        float[] kernelData = { 0, -0.5f, 0, -0.5f, 3, -0.5f, 0, -0.5f, 0 };
        Kernel kernel = new Kernel(3, 3, kernelData);
    
        int borderSize =  kernel.getWidth() / 2;

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
         */
        Graphics2D g = paddedInput.createGraphics();
        g.drawImage(input, borderSize, borderSize, null);

        /** copy the left and right, top and bottom borders of the input image to the padded image.
         * @param input: the input image to copy from 
         * @param dx1, dy1: the x and y coordinates of the (desired corner) of the output rectangle
         * @param borderSize, borderSize: the width and height of the output rectangle
         * @param sx1, sx2: the x and y coordinates of the (desired corner) of the source rectangle
         * @param borderSize, borderSize: the width and height of the source rectangle
         * @param null: ImageObserver, which is not needed
        */
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

        /**
         * apply the ConvolveOp to the padded image and crop the output
         * image to remove the padded pixels.
         */
        ConvolveOp op = new ConvolveOp(kernel);
        BufferedImage output = op.filter(paddedInput, null);

        if(rescaled){
                output = NormalizeImage.rescaleImage(output);
        }

        /*
         * returns a cropped image that excludes the border pixels, The output image has
         * the same size as the input image.
         */
        return output.getSubimage(borderSize, borderSize, input.getWidth(), input.getHeight());
    }
    

}
