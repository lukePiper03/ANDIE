package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * Class to change the brightness and contrast of an image using an imageOperation
 */
public class Brightness implements ImageOperation, java.io.Serializable {
    double b;
    double c;
    

    /**
     * Constructer for a brightness object, sets the datafields b and c with the values entered by the user
     * @param b the brightness change as a % betweeeen -100 and 100, entered by the user.
     * @param c the contrast change as a % between -100 and 100, entered by the user.
     */
    public Brightness(double b, double c){
        this.b = b;
        this.c = c;
         
    }

        /**
         * method to return the brightness parameter of a brightness/ contrast operation
         * @return
         */
    protected double getBrightness(){
        return this.b;
    }

    /**
     * method to return the contrast of a brightness/co trast operation
     * @return
     */
    protected double getContrast(){
        return this.c;
    }

    /**
     * class to apply the brightness and contrast changes to an image
     * @param input the input image we wish to change
     * @return workingImage, the image that has had the brightness and contrast changes applied
     */
    public BufferedImage apply(BufferedImage input){
        try{
            BufferedImage workingImage = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            for(int y = 0; y < workingImage.getHeight(); y++){
                for(int x = 0; x < workingImage.getWidth(); x++){
                    int argb = input.getRGB(x, y);
                    int A = argb >> 24 & 0xff;
                    int R = argb >> 16 & 0xff;
                    int G = argb >> 8 & 0xff;
                    int B = argb & 0xff;
                    A = (int)((1 + c/100.0)*(A - 127.5) + 127.5*(1 + b/100.0));
                    if(A > 255){
                        A = 255;
                    }else if(A < 0){
                        A = 0;
                    }
                    R = (int)((1 + c/100.0)*(R - 127.5) + 127.5*(1 + b/100.0));
                    if(R > 255){
                        R = 255;
                    }else if(R < 0){
                        R = 0;
                    }
                    G = (int)((1 + c/100.0)*(G - 127.5) + 127.5*(1 + b/100.0));
                    if(G > 255){
                        G = 255;
                    }else if(G < 0){
                        G = 0;
                    }
                    B = (int)((1 + c/100.0)*(B - 127.5) + 127.5*(1 + b/100.0));
                    if(B > 255){
                        B = 255;
                    }else if(B < 0){
                        B = 0;
                    }
                    int newARGB = (A << 24) | (R << 16) | (G << 8) | B;
                    workingImage.setRGB(x, y, newARGB);
                }
            }
            return workingImage;
        }catch(NullPointerException e){
            //catches the case wheere the input image is null
            throw new NullPointerException("image is null, please try again with an image loaded."); 
        }catch(IllegalArgumentException e){
            //catches any ilegal argument exceptions that could occur when setting new rgb values,
            //incase the value we try to use is out of bounds
            throw new IllegalArgumentException("RGB value is out of range");
        }
    }
    
}
