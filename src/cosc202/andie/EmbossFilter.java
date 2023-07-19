package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;

/**
 * The EmbossFilter class represents an image operation for applying an emboss filter to an image.
 * It enhances the differences between neighbouring pixel values by simulating the effect of embossing.
 * The filter can be applied using two different methods: apply() and apply2().
 *
 * This class is highly adapted from the MeanFilter class by Steven Mills.
 * 
 * @author Hannah Srzich
 * @version 1.0
 */
public class EmbossFilter implements ImageOperation, java.io.Serializable {  
    
    // Eight basic emboss filters
    private int[][] kernel1 = {{0, 0, 0}, {1, 0, -1}, {0, 0, 0}};
    private int[][] kernel2 = {{1, 0, 0}, {0, 0, 0}, {0, 0, -1}};
    private int[][] kernel3 = {{0, 1, 0}, {0, 0, 0}, {0, -1, 0}};
    private int[][] kernel4 = {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}};
    private int[][] kernel5 = {{0, 0, 0}, {-1, 0, 1}, {0, 0, 0}};
    private int[][] kernel6 = {{-1, 0, 0}, {0, 0, 0}, {0, 0, 1}};
    private int[][] kernel7 = {{0, -1, 0}, {0, 0, 0}, {0, 1, 0}};
    private int[][] kernel8 = {{0, 0, -1}, {0, 0, 0}, {1, 0, 0}};
    private int[][] kernel = kernel1; // Set to kernel1 by default

    /**
     * Constructs a new EmbossFilter with default settings.
     */
    public EmbossFilter() {
    }

    /**
     * Constructs a new EmbossFilter with the specified emboss type.
     *
     * @param embossType the type of emboss effect to apply (1-8)
     */
    public EmbossFilter(int embossType) {
      if(embossType == 1){
        this.kernel = kernel1;
      }else if(embossType == 2){
        this.kernel = kernel2;
      }else if(embossType == 3){
        this.kernel = kernel3;
      }else if(embossType == 4){
        this.kernel = kernel4;
      }else if(embossType == 5){
        this.kernel = kernel5;
      }else if(embossType == 6){
        this.kernel = kernel6;
      }else if(embossType == 7){
        this.kernel = kernel7;
      }else if(embossType == 8){
        this.kernel = kernel8;
      }
      // Or else default is kernel 1
    }

    /**
     * Applies the emboss filter to the given image, with negative pixel 
     * handling that makes entire image grey to see edges clearer.
     *
     * @param image the BufferedImage to apply the filter to
     * @return a new BufferedImage with the emboss effect applied
     */
    public BufferedImage apply(BufferedImage image) {
      // Extend the image by 3 pixels on each size
      image = ImageEdgeExtension.extendImage(image);

      int width = image.getWidth();
      int height = image.getHeight();
      BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  
      // Define the emboss kernel
      int[][] kernel = this.kernel;
  
      // Apply the kernel to each pixel
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int sumR = 0;
          int sumG = 0;
          int sumB = 0;
  
          // Apply the kernel to the neighbouring pixels
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
  
          // Calculate the new pixel colour values to deal with negatives
          int newR = Math.min(Math.max(sumR + 128, 0), 255);
          int newG = Math.min(Math.max(sumG + 128, 0), 255);
          int newB = Math.min(Math.max(sumB + 128, 0), 255);
  
          // Set the new pixel colour values
          Color newColor = new Color(newR, newG, newB);
          newImage.setRGB(x, y, newColor.getRGB());
        }
      }
      // Crop the image back to original size
      newImage = ImageEdgeExtension.cropImage(newImage);
      return newImage;
  }
}
