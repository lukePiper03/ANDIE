package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;

/**
 * ImageOperation to apply a Sobel filter.
 * 
 * <p>
 * A Sobel filter is used for edge detection in image processing. It applies a convolution
 * operation using Sobel kernels to enhance the differences between neighboring pixel values.
 * 
 * It does so by calculating the gradient magnitude of the image intensity at each pixel, which 
 * represents the rate of change of intensity in the image. 
 * </p>
 * 
 * <p>
 * This implementation is adapted from the MeanFilter class by Steven Mills. And was built
 * with CoPiolet on, however, the option for use of online resource use was switched off.
 * </p>
 * 
 * @author Hannah Srzich
 * @version 1.0
 */
public class SobelFilter implements ImageOperation, java.io.Serializable {  

    private int[][] kernelH = {{-1/2, 0, 1/2}, {-1, 0, 1}, {-1/2, 0, 1/2}}; // Horizontal kernel
    private int[][] kernelV = {{-1/2, -1, -1/2}, {0, 0, 0}, {1/2, 1, 1/2}}; // Vertical kernel
    private int[][] kernel = kernelV; // Set to kernel1 by default

    /**
     * Default constructor for the Sobel filter.
     */
    public SobelFilter() {
    }

    /**
     * Constructor for the Sobel filter with the specified Sobel type.
     *
     * @param sobelType The Sobel type to be used: 1 for horizontal, 2 for vertical.
     */
    public SobelFilter(int sobelType) {
        if(sobelType == 1) {
            this.kernel = kernelH;;
        }else if(sobelType == 2){
          this.kernel = kernelV;
        }
    }

    /**
     * Applies the Sobel filter to the given image.
     *
     * @param image The input image to which the filter will be applied.
     * @return The filtered output image.
     */
    public BufferedImage apply(BufferedImage image) {
      // Extend the image by 3 pixels on each size
      image = ImageEdgeExtension.extendImage(image);

      int width = image.getWidth();
      int height = image.getHeight();
      BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  
      // Define the Sobel kernel
      int[][] kernel = this.kernel;
  
      // Apply the kernel to each pixel
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int sumR = 0;
          int sumG = 0;
          int sumB = 0;
  
          // Apply the kernel to the neighboring pixels
          for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              int dx = x + j - 1;
              int dy = y + i - 1;
  
              if (dx < 0 || dx >= width || dy < 0 || dy >= height) {
                continue;
              }
  
              Color c = new Color(image.getRGB(dx, dy));
              sumR += c.getRed() * kernel[i][j];
              sumG += c.getGreen() * kernel[i][j];
              sumB += c.getBlue() * kernel[i][j];
            }
          }
  
          // Calculate the new pixel color values to account for negatives
          int newR = Math.min(Math.max(sumR + 128, 0), 255);
          int newG = Math.min(Math.max(sumG + 128, 0), 255);
          int newB = Math.min(Math.max(sumB + 128, 0), 255);
  
          // Set the new pixel color values
          Color newColor = new Color(newR, newG, newB);
          newImage.setRGB(x, y, newColor.getRGB());
        }
      }
      // crop the image back to origional size
      newImage = ImageEdgeExtension.cropImage(newImage);

      return newImage;
    }

}