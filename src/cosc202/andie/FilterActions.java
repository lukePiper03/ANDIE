
package cosc202.andie;

import java.util.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood.
 * This includes a mean filter (a simple blur) in the sample code, but more
 * operations will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Hannah Srzich and Eszter Scarlett-Herbert, adapted from Steven Mills
 * @version 1.0
 */
public class FilterActions {

   /** A list of actions for the Filter menu. */
   protected ArrayList<Action> actions;

   /**
    * <p>
    * Create a set of Filter menu actions.
    * </p>
    */
   public FilterActions() {
      actions = new ArrayList<Action>();
      actions.add(new GaussianBlurFilterAction(Andie.bundle.getString("GBFilter"), null,
            Andie.bundle.getString("gausDesc"), Integer.valueOf(KeyEvent.VK_K)));
      actions.add(new MeanFilterAction(Andie.bundle.getString("meanFilter"), null, Andie.bundle.getString("meanDesc"),
            Integer.valueOf(KeyEvent.VK_L)));
      actions.add(new MedianFilterAction(Andie.bundle.getString("medianFilter"), null,
            Andie.bundle.getString("medianDesc"), Integer.valueOf(KeyEvent.VK_M)));
      actions.add(new SharpenFilterAction(Andie.bundle.getString("sharpenFilter"), null,
            Andie.bundle.getString("sharpDesc"), Integer.valueOf(KeyEvent.VK_N)));
      actions.add(new SobelFilterAction(Andie.bundle.getString("sobelFilter"), null,
            Andie.bundle.getString("sobelDesc"), Integer.valueOf(KeyEvent.VK_P)));
      actions.add(new EmbossFilterAction(Andie.bundle.getString("embossFilter"), null,
            Andie.bundle.getString("embossDesc"), Integer.valueOf(KeyEvent.VK_J)));
   }

   /**
    * <p>
    * Create a menu containing the list of Filter actions.
    * This will then create a mnemonic key, which acts as a keyboard shortcut,
    * that, when pressed along
    * with the appropriate modifier keys, triggers the menu item action.
    * The accelerator key, on the other hand, represents a keyboard shortcut that
    * combines one or more modifier keys (such as Ctrl, Shift)
    * with a specific key or character. It allows the user to perform the
    * associated action directly from the keyboard without navigating through the
    * menu structure.
    * </p>
    * 
    * @return The filter menu UI element.
    */
   public JMenu createMenu() {
      JMenu filterMenu = new JMenu(Andie.bundle.getString("filter"));
      for (Action action : actions) {
         JMenuItem menuItem = new JMenuItem(action);
         menuItem.setMnemonic(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue());
         menuItem.setAccelerator(KeyStroke.getKeyStroke(((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue(),
               KeyEvent.CTRL_DOWN_MASK));
         filterMenu.add(menuItem);
      }
      return filterMenu;
   }

   /**
    * A method to get the radius from the user through a JSlider.
    * 
    * @param minRadius
    * @param maxRadius
    * @param title
    * @return int radius, selected by user
    * 
    */
   public int getSliderRadius(int minRadius, int maxRadius, String title) {
      // Show J Slider
      JSlider slider = new JSlider();
      // all settings for this JSlider
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.setMaximum(maxRadius);
      slider.setMinimum(minRadius);
      slider.setValue((minRadius + maxRadius) / 2);
      slider.setMajorTickSpacing(1);
      slider.setSnapToTicks(true);
      Dimension d = slider.getPreferredSize();
      slider.setPreferredSize(new Dimension(d.width + 200, d.height));
      // make option pane
      int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);

      if (select == JOptionPane.CANCEL_OPTION) {
         return -999;
      } else {
         return (int) slider.getValue();
      }
   }

   /**
    * A method to get the slider object.
    * 
    * @param minRadius
    * @param maxRadius
    * @param title
    * @return slider
    */
   public JSlider getSlider(int minRadius, int maxRadius, String title) {
      // Show J Slider
      JSlider slider = new JSlider();
      // all settings for this JSlider
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.setMaximum(maxRadius);
      slider.setMinimum(minRadius);
      slider.setValue((minRadius + maxRadius) / 2);
      slider.setMajorTickSpacing(1);
      slider.setSnapToTicks(true);
      Dimension d = slider.getPreferredSize();
      slider.setPreferredSize(new Dimension(d.width + 200, d.height));

      return slider;
   }

   /**
    * A method to get the slider object for the Sobel filter i.e. V and H labels.
    * 
    * @param minRadius
    * @param maxRadius
    * @param title
    * @return slider
    */
   public JSlider getSliderVH(int minRadius, int maxRadius, String title) {
      // Create a JSlider
      JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 2, 1);

      // Create a dictionary to hold the labels
      Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

      JLabel verticalLabel = new JLabel(Andie.bundle.getString("vertical"));
      JLabel horizontalLabel = new JLabel(Andie.bundle.getString("horizontal"));

      // Set the font of the labels
      InputStream stream = Andie.class.getClassLoader().getResourceAsStream("lexend-semibold.ttf");
      Font labelFont;
      try {
         labelFont = Font.createFont(Font.TRUETYPE_FONT, stream);
         labelFont = labelFont.deriveFont(Font.BOLD, 13);
         verticalLabel.setFont(labelFont);
         horizontalLabel.setFont(labelFont);
      } catch (FontFormatException | IOException e) {
         e.printStackTrace();
      }
      
      // Add the labels to the dictionary
      labelTable.put(1, verticalLabel);
      labelTable.put(2, horizontalLabel);

      // Set the labels on the slider
      slider.setLabelTable(labelTable);
      slider.setPaintLabels(true);

      // all settings for this JSlider
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.setMaximum(maxRadius);
      slider.setMinimum(minRadius);
      slider.setValue((minRadius + maxRadius) / 2);
      slider.setMajorTickSpacing(1);
      slider.setSnapToTicks(true);
      Dimension d = slider.getPreferredSize();
      slider.setPreferredSize(new Dimension(d.width + 200, d.height));

      return slider;
   }

   /**
    * Creates a slider with the specified minimum and maximum values, title, and
    * target image panel.
    * The slider is associated with a specific filter type and supports timed
    * updates.
    * Solely used for the Sobel filter.
    *
    * @param min    the minimum value of the slider
    * @param max    the maximum value of the slider
    * @param title  the title or label for the slider
    * @param target the ImagePanel to apply the filter to
    */
   public void makeSliderVH(int min, int max, String title, ImagePanel target) {

      EditableImage realImage = target.getImage();

      // get slider object
      JSlider slider = getSliderVH(min, max, title);

      Andie.colorFontSlider(slider);
      Andie.setOptionStyle();

      // update the image on screen as the slider moves
      slider.addChangeListener(new ChangeListener() {
         // keep track of the last value of the slider
         int lastSilderValue = 1;

         public void stateChanged(ChangeEvent ce) {
            // Update for mouse listener bug
            Andie.chooserOperating = true;
            // create a deep copy of the editable image (so that we don't change the actual
            // editable image)
            EditableImage copyImage = realImage.deepCopyEditable();
            // set the target to have this new copy of the actual image.
            target.setImage(copyImage);
            // apply the median filter to the new copy of the actual image.
            if (slider.getValue() == 0) { // No change to apply.
               return;
            }
            // update the image if the difference between the last slider value and new
            // slider value is greater than or equal to 2
            if (Math.abs(lastSilderValue - slider.getValue()) >= 1) {
               target.getImage().apply(new SobelFilter(slider.getValue()));
               target.repaint();
               target.getParent().revalidate();
               // update the last value of the slider
               lastSilderValue = slider.getValue();
            }
         }
      });

      // make option pane
      int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);
      // Update for mouse listener bug
      Andie.chooserOperating = false;
      // reset image as the origional image
      target.setImage(realImage);

      // if user presses cancel
      if (select == JOptionPane.CANCEL_OPTION) {
         // re paint the original image
         target.repaint();
         target.getParent().revalidate();
         return;
      }

      // create and apply the filter
      target.getImage().apply(new SobelFilter(slider.getValue()));
      target.repaint();
      target.getParent().revalidate();
   }

   /**
    * Creates a slider with the specified minimum and maximum values, title, and
    * target image panel.
    * The slider is associated with a specific filter type and supports timed
    * updates.
    *
    * @param min        the minimum value of the slider
    * @param max        the maximum value of the slider
    * @param title      the title or label for the slider
    * @param target     the ImagePanel to apply the filter to
    * @param filterType the type of filter to apply:
    *                   1 - Mean filter
    *                   2 - Median filter
    *                   3 - Gaussian blur filter
    *                   4 - Emboss filter
    *                   5 - Sobel filter
    * @param timed      flag indicating whether timed updates should be used for
    *                   the median filter
    */
   public void makeSlider(int min, int max, String title, ImagePanel target, int filterType, boolean timed) {
      // Note, it does not include Sharpen filter, because it does not have a value
      // Mean filter = 1
      // Median filter = 2
      // Gaussian blur filter =3
      // Emboss filter = 4
      // Sobel filter = 5

      EditableImage realImage = target.getImage();

      // get slider object
      JSlider slider = getSlider(min, max, title);

      Andie.colorFontSlider(slider);

      // update the image on screen as the slider moves
      slider.addChangeListener(new ChangeListener() {
         // keep track of the last value of the slider
         int lastSilderValue = 0;

         public void stateChanged(ChangeEvent ce) {
            // Update for mouse listener bug
            Andie.chooserOperating = true;
            // timer for the median filter due to lagging
            long startTime = System.nanoTime();
            // create a deep copy of the editable image (so that we don't change the actual
            // editable image)
            EditableImage copyImage = realImage.deepCopyEditable();
            // set the target to have this new copy of the actual image.
            target.setImage(copyImage);
            // apply the median filter to the new copy of the actual image.
            if (slider.getValue() == 0) { // No change to apply.
               return;
            }
            // update the image if the difference between the last slider value and new
            // slider value is greater than or equal to 2
            if (Math.abs(lastSilderValue - slider.getValue()) >= 1) {
               if (filterType == 1) {
                  target.getImage().apply(new MeanFilter(slider.getValue()));
               } else if (filterType == 2) {
                  long endTime = System.nanoTime(); // get the end time in nanoseconds
                  long elapsedTime = endTime - startTime; // calculate the elapsed time in nanoseconds
                  double elapsedSeconds = (double) elapsedTime / 1000000000; // convert to seconds
                  if (elapsedSeconds >= 0.02) {
                     // if time is more than 1/5 seconds, apply the median filter
                     // reset the timer
                     startTime = System.nanoTime();
                     target.getImage().apply(new MedianFilter(slider.getValue()));
                  }
               } else if (filterType == 3) {
                  target.getImage().apply(new GaussianBlurFilter(slider.getValue()));
               } else if (filterType == 4) {
                  target.getImage().apply(new EmbossFilter(slider.getValue()));
               } else if (filterType == 5) {
                  target.getImage().apply(new SobelFilter(slider.getValue()));
               }
               target.repaint();
               target.getParent().revalidate();
               // update the last value of the slider
               lastSilderValue = slider.getValue();
            }
         }
      });

      // make option pane
      int select = JOptionPane.showOptionDialog(null, slider, title, JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, Andie.icon, null, null);
      Andie.chooserOperating = false;
      // reset image as the original image
      target.setImage(realImage);

      // if user presses cancel
      if (select == JOptionPane.CANCEL_OPTION) {
         // re paint the original image
         target.repaint();
         target.getParent().revalidate();
         return;
      }

      // create and apply the filter
      if (filterType == 1) {
         target.getImage().apply(new MeanFilter(slider.getValue()));
      } else if (filterType == 2) {
         target.getImage().apply(new MedianFilter(slider.getValue()));
      } else if (filterType == 3) {
         target.getImage().apply(new GaussianBlurFilter(slider.getValue()));
      } else if (filterType == 4) {
         target.getImage().apply(new EmbossFilter(slider.getValue()));
      } else if (filterType == 5) {
         target.getImage().apply(new SobelFilter(slider.getValue()));
      }
      target.repaint();
      target.getParent().revalidate();
   }

   /**
    * <p>
    * Action to blur an image with a mean filter.
    * </p>
    * 
    * @see MeanFilter
    */
   public class MeanFilterAction extends ImageAction {
      private int minRadius = 1;
      private int maxRadius = 10;
      private String title = Andie.bundle.getString("meanFilter");

      /**
       * <p>
       * Create a new mean-filter action.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * <p>
       * Callback for when the convert-to-grey action is triggered.
       * </p>
       * 
       * <p>
       * This method is called whenever the MeanFilterAction is triggered.
       * It prompts the user for a filter radius, then apply's an appropriately sized
       * {@link MeanFilter}.
       * </p>
       * 
       * @param e The event triggering this callback.
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("meanError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }

         makeSlider(minRadius, maxRadius, title, target, 1, false);
      }
   }

   public class SharpenFilterAction extends ImageAction {
      /**
       * <p>
       * Creates a new Sharpen-filter action.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * <p>
       * This method is called whenever the SharpnessFilterAction is triggered.
       * this applies the method on the image with a new SharpenFilter, in turn which
       * creates a new image that has been sharpened
       * using the SharpenFilter script.
       * The revalidate method is called to ensure that the layout of the parent
       * container (if any) is
       * updated to reflect the new image
       * </p>
       * 
       * @param e The event triggering this callback.
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("sharpError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }
         // Create and apply the filter
         target.getImage().apply(new SharpenFilter(true));
         target.repaint();
         target.getParent().revalidate();
      }
   }

   /**
    * The EmbossFilterAction class extends the ImageAction class and represents an
    * action for applying the Emboss filter
    * to an image. It creates a slider with specific parameters and applies the
    * filter to the target image panel.
    */
   public class EmbossFilterAction extends ImageAction {

      private int minEmbossType = 1;
      private int maxEmbossType = 8;
      private String title = Andie.bundle.getString("embossFilter");

      /**
       * Constructs a new EmbossFilterAction with the specified name, icon,
       * description, and mnemonic.
       *
       * @param name     the name of the action
       * @param icon     the icon for the action
       * @param desc     the description of the action
       * @param mnemonic the mnemonic for the action
       */
      EmbossFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * Performs the action when triggered by an event.
       *
       * @param e the action event
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("embossError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }
         // Create and apply the filter with slider and listener
         makeSlider(minEmbossType, maxEmbossType, title, target, 4, false);
      }
   }

   /**
    * The SobelFilterAction class extends the ImageAction class and represents an
    * action for applying the Sobel filter
    * to an image. It creates a slider with specific parameters and applies the
    * filter to the target image panel.
    */
   public class SobelFilterAction extends ImageAction {

      private int minSobelType = 1;
      private int maxSobelType = 2;
      private String title = Andie.bundle.getString("sobelFilter");

      /**
       * Constructs a new SobelFilterAction with the specified name, icon,
       * description, and mnemonic.
       *
       * @param name     the name of the action
       * @param icon     the icon for the action
       * @param desc     the description of the action
       * @param mnemonic the mnemonic for the action
       */
      SobelFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * Performs the action when triggered by an event.
       *
       * @param e the action event
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("sobelError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }
         // Create and apply the filter with slider and listener
         // makeSlider(minSobelType, maxSobelType, title, target, 5, false);
         makeSliderVH(minSobelType, maxSobelType, title, target);
      }
   }

   /**
    * The GaussianBlurFilterAction class extends the ImageAction class and
    * represents an action for applying the Gaussian blur filter
    * to an image. It creates a slider with specific parameters and applies the
    * filter to the target image panel.
    */
   public class GaussianBlurFilterAction extends ImageAction {

      private int minRadius = 1;
      private int maxRadius = 10;
      private String title = Andie.bundle.getString("GBFilter");

      /**
       * Constructs a new GaussianBlurFilterAction with the specified name, icon,
       * description, and mnemonic.
       *
       * @param name     the name of the action
       * @param icon     the icon for the action
       * @param desc     the description of the action
       * @param mnemonic the mnemonic for the action
       */
      GaussianBlurFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * Performs the action when triggered by an event. It sets the option style,
       * checks if the target image panel has an image,
       * displays an error message if there is no image, and creates a slider with the
       * specified parameters. The filter is then
       * applied to the target image panel using the slider and listener.
       *
       * @param e the action event
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("gausError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }

         // Create and apply the filter with slider and listener
         makeSlider(minRadius, maxRadius, title, target, 3, false);
      }
   }

   /**
    * <p>
    * Action to blur an image with a median filter.
    * </p>
    * 
    * @see MedianFilter
    */
   public class MedianFilterAction extends ImageAction {
      private int minRadius = 1;
      private int maxRadius = 6;
      private String title = Andie.bundle.getString("medianFilter");

      /**
       * <p>
       * Create a new MedianFilterAction.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      /**
       * <p>
       * This is the action performed method. It is activated when the user clicks the
       * button.
       * This is done using a while method, so until the user clicks cancel or types
       * invalid input this will loop while true creates an infinite loop that will
       * keep prompting
       * the user till it is broken via a return or a break.
       * </p>
       * 
       * @param e The event triggering this callback.
       */
      public void actionPerformed(ActionEvent e) {
         Andie.setOptionStyle();
         if (!target.getImage().hasImage()) {
            // Play error sound, play the audio in a separate thread
            Thread audioThread = new Thread(() -> Andie.sound.playErrorSound());
            audioThread.start();
            String title = Andie.bundle.getString("medianError");
            String message = Andie.bundle.getString("filterErrorMessage")+ "       ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, Andie.icon);
            return;
         }
         // note that timed is true, because the median filter lags
         makeSlider(minRadius, maxRadius, title, target, 2, true);
      }
   }
}
