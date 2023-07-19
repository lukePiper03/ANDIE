package cosc202.andie;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.Border;
import javax.imageio.*;
import java.util.prefs.Preferences;
import java.util.*;


/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various
 * image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills edited by Hannah Srzich
 * @version 1.0
 */
public class Andie {

    public static JFrame frame;
    public static ImagePanel imagePanel;
    public static Preferences prefs;
    public static ResourceBundle bundle;
    static ImageIcon icon;
    public static boolean chooserOperating;
    public static JMenuBar menuBar;
    public static SoundEffect sound = new SoundEffect();

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an
     * {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save,
     * edit, etc.
     * These operations are implemented {@link ImageOperation}s and triggerd via
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * 
     * @throws Exception if something goes wrong.
     */
    public static void createAndShowGUI() throws Exception {
        // Play start up sound, play the audio in a separate thread
        Thread audioThread = new Thread(() -> sound.playStartUpSound());
        audioThread.start();

        // Bug fix for chooser
        chooserOperating = false;

        // Code for obtaining the screen size adapted from:
        // https://alvinalexander.com/blog/post/jfc-swing/how-determine-get-screen-size-java-swing-app/
        prefs = Preferences.userNodeForPackage(Andie.class);
        prefs.remove("language");
        Locale.setDefault(new Locale(prefs.get("language", "en")));
        bundle = ResourceBundle.getBundle("MessageBundle");

        // Gets the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Set the font and color of the title bar
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("ANDIE");
        // Customize the colors of the window decorations
        Color backgroundColor = new Color(23, 22, 29);
        frame.setBackground(backgroundColor);

        // Set the size of the frame to be 90% of the screen size, so that it is not a
        // tiny window
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        frame.setPreferredSize(new Dimension(width, height));

        Image image = ImageIO.read(Andie.class.getClassLoader().getResource("icon1.png"));
        ImageIcon icon = new ImageIcon(); // dont delete this, it means sleek look, no image icon (otherwise it goes to weird java default guy)
        Andie.icon = icon;
        frame.setIconImage(image);

        // The main content area is an ImagePanel
        imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);

        imagePanel.setBackground(backgroundColor);

        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);

        makeJMenu(false);
        createToolBar();

        // Set the size of the frame to be the size of the image
        frame.pack();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = 0;
        frame.setLocation(x, y);

        // Check if user wants to exit when they press red exit button on jFrame
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // if there are no ops on the stack just exit
                if(imagePanel.getImage().getOpsSize() == 0){
                    // Play exit sound
                    sound.playExitSound();
                    System.exit(0);
                    return;
                }
                // Play error sound, play the audio in a separate thread
                Thread audioThread = new Thread(() -> sound.playErrorSound());
                audioThread.start();
                // If operations are there ask user if they want to exit
                String title = Andie.bundle.getString("exit");
                String message = Andie.bundle.getString("exitCheck") + "       ";
                int result = JOptionPane.showConfirmDialog(null,message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.icon);
                if(result == JOptionPane.YES_OPTION){
                    // Play exit sound
                    sound.playExitSound();
                    // Exit
                    System.exit(0);
                }
            }
        });

        frame.setVisible(true);
    }

    /**
     * Creates a JMenuBar with various types of menus for the user to perform
     * actions on the image.
     * The created menus are File, Edit, View, Filter, Colour, Transformation, and
     * Language menus.
     * The JMenuBar is added to the frame and set to be visible.
     * 
     * @param notFirstInstance if true, the menu bar is created for the outline version or languages, so repaint it
     * 
     * @throws IOException         this is needed for implementing the font
     * @throws FontFormatException this is needed for implementing the font
     */
    public static void makeJMenu(boolean notFirstInstance) throws IOException, FontFormatException {
        // Set the background color of the JMenuBar to black color
        // and change the text color to white
        // the use if UI manager is adapted from
        // https://stackoverflow.com/questions/15648030/change-background-and-text-color-of-jmenubar-and-jmenu-objects-inside-it-,
        // answered by Joaquin Gutierrez
        UIManager.put("Menu.foreground", Color.BLACK);

        // lexend font is a google font, under the open font lsicence, which means that
        // it is free to use for products & projects – print or digital, commercial or
        // otherwise
        InputStream stream = Andie.class.getClassLoader().getResourceAsStream("lexend-semibold.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
        font = font.deriveFont(Font.BOLD, 13);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);

        // create the border- this is specifically so the file and language buttons on
        // the menu stand out
        // creates a border for the outer edge of the menu (so the border doesnt
        // surround the outer most part of the menu)
        // creates a border for the inner edge of the menu (where the actual white
        // border will sit away from the text)
        // creates a compound border that combines the outer and inner borders (this
        // will be the actual frame around the items you see)
        Border outerBorder = BorderFactory.createLineBorder(new Color(238, 238, 238), 6);
        Border innerBorder = BorderFactory.createLineBorder(new Color(23, 22, 29), 2);
        Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);

        // this border is used so that there is some space betweent the right most
        // element in the menu
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 10);

        // add in menus for various types of action the user may perform.
        // then set the right most border so that when the menubar is created this space
        // will exist
        menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createCompoundBorder(menuBar.getBorder(), paddingBorder));


        // this creates the file menu actions with the surrounding compund border
        // this and the language menu are slighly different to other menu items as they
        // are converted to jmenu items in order to add the compound border, which is
        // applied
        FileActions fileActions = new FileActions();
        JMenu fileMenu = fileActions.createMenu();
        fileMenu.setBorder(compoundBorder);
        menuBar.add(fileMenu);

        // New menubar button for languages that change the language settings
        LanguageActions languageActions = new LanguageActions();
        JMenu languageMenu = languageActions.createMenu();
        languageMenu.setBorder(compoundBorder);
        menuBar.add(languageMenu);

        // this works as a sperator to create space till the edge of the menubar
        menuBar.add(Box.createHorizontalGlue());

        // creates the edit menu, with the edit actions
        EditActions editActions = new EditActions();
        menuBar.add(editActions.createMenu());

        // View actions control how the image is displayed, but do not alter its actual
        // content
        ViewActions viewActions = new ViewActions();
        menuBar.add(viewActions.createMenu());

        // New menubar button for actions that change the rotation of the image, and
        // resize it
        TransformationActions transformationActions = new TransformationActions();
        menuBar.add(transformationActions.createMenu());

        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        menuBar.add(colourActions.createMenu());

        // Filters apply a per-pixel operation to the image,
        FilterActions filterActions = new FilterActions();
        menuBar.add(filterActions.createMenu());

         // New menubar button for record actions to record operations on an image
        MacroActions macroActions = new MacroActions();
        menuBar.add(macroActions.createMenu());

        // New menubar button for drawing shapes that draws shapes
        DrawActions drawActions = new DrawActions();
        menuBar.add(drawActions.createMenu());

        // Increase the preferred height of the menu bar
        // i.e increase its height by 15 pixels
        Dimension preferredSize = menuBar.getPreferredSize();
        preferredSize.height += 15;
        menuBar.setPreferredSize(preferredSize);

        frame.setJMenuBar(menuBar);
        // This is so that in its first instance of creation it does not 
        // draw twice and make the window pop up twice (looks bad)
        if(notFirstInstance){
            frame.setVisible(true);
        }
    }

    // creates a new tool bar when the class is called in the main method
    public static JToolBar createToolBar() throws IOException {

        // creates a vertical tool bar
        // then the background color is set to a grey shade
        // and the border is aligned to the west of the frame
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setRollover(true);
        toolBar.setBackground(new Color(63, 64, 74));

        // an array of the image names IN ORDER that they are needed for the icons
        //Imgae source 1: By "https://github.com/siemens/ix-icons?ref=svgrepo.com", - Siemens, in MIT License via "https://www.svgrepo.com/" 
        //Image source 2: By "https://www.svgrepo.com/"- SVG Repo, in CC0 License License via "https://www.svgrepo.com/"
        //Image source 3 and 4: By "https://www.bypeople.com/minimal-free-pixel-perfect-icons/?ref=svgrepo.com"- Bypeople, in PD License via "https://www.svgrepo.com/"
        //Image source 5: By "https://github.com/krystonschwarze/coolicons?ref=svgrepo.com" - krystonschwarze, in CC Attribution License via "https://www.svgrepo.com/" 
        //Image source 6: By "https://buninux.gumroad.com/l/lfdy?ref=svgrepo.com"-Buninux, in PD License via "https://www.svgrepo.com/"
        //Image source 7: By "https://github.com/element-plus/element-plus-icons?ref=svgrepo.com"- Element Plus, in MIT License via "https://www.svgrepo.com/" 
        //Image source 8: By "https://gitlab.com/nuinalp/open-source/nuiverse/icons?ref=svgrepo.com"- Nuiverse Design in BSD License via "https://www.svgrepo.com/"
        //Image source 9: By <a href="https://dribbble.com/gokcekurt?ref=svgrepo.com"- Gokce Kurt, in CC Attribution License via "https://www.svgrepo.com/" 
        String[] imagePaths = { "open.png", "export.png", "zoomin.png", "zoomout.png",
                "undo2.png", "rotate2.png", "crop2.png", "sun3.png", "art2.png" };

        // this is an array of the action listener actions!! they will run as shortcuts
        // to the actions
        ActionListener[] actions = {
                new FileActions().new FileOpenAction("Open", null, "Open an image file", KeyEvent.VK_O),
                new FileActions().new FileExportAction("export", null, "export the image file", KeyEvent.VK_E),
                new ViewActions().new ZoomInAction("zoom-in", null, "zoom into the image", KeyEvent.VK_PLUS),
                new ViewActions().new ZoomOutAction("zoom-out", null, "zoom out of the image", KeyEvent.VK_MINUS),
                new EditActions().new UndoAction("undo", null, "undo", KeyEvent.VK_Z),
                new TransformationActions().new RotateImage90RightAction("rotate 90", null, "rotate 90", KeyEvent.VK_RIGHT),
                new TransformationActions().new CropImageAction("crop", null, "crop image", KeyEvent.VK_RIGHT),
                new ColourActions().new BrightnessAction("brightness", null, "increase brightness", KeyEvent.VK_B),
                new DrawActions().new DrawLineAction("draw", null, "apply drawing actions", KeyEvent.VK_D),
        };

        // a for loop which runs through both the arrays and creates the buttons, with
        // actions and images based on their position in the array
        // there will be a seperator between the bottons, the background will match the
        // becakground of the tool bar
        // the image size will be scaled down and the border for each botton removed
        for (int i = 0; i < imagePaths.length; i++) {
            BufferedImage icon = ImageIO.read(Andie.class.getClassLoader().getResource("pictures/" + imagePaths[i]));

            // Create a new image with bicubic interpolation
            BufferedImage resizedImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(icon, 0, 0, 30, 30, null);
            g2d.dispose();
            
            // Create an ImageIcon from the resized image
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            
            // Create a new button with the ImageIcon
            JButton button = new JButton(imageIcon);
            button.addActionListener(actions[i]);
            button.setBackground(new Color(63, 64, 74));
            button.setBorderPainted(false);
            toolBar.add(button);
            toolBar.addSeparator();
        }
        frame.add(toolBar, BorderLayout.WEST);
        // the tool bar is returned so can be displayed when the method is called
        return toolBar;
    }

    /**
     * Sets the option style for JOptionPane dialogs.
     * 
     * This method customizes the appearance of JOptionPane dialogs by changing various colors, fonts, and backgrounds.
     */
    public static void setOptionStyle(){
        
        // Set the frame's background color to white and text to black
        JFrame.setDefaultLookAndFeelDecorated(true);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.red));
        defaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.white));
        defaults.put("inactiveCaption", new javax.swing.plaf.ColorUIResource(Color.black));
        defaults.put("inactiveCaptionText", new javax.swing.plaf.ColorUIResource(Color.white));
        // change all colors 
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.BLACK); // text
        UIManager.put("OptionPane.buttonBackground", Color.WHITE);
        UIManager.put("OptionPane.buttonForeground", Color.WHITE); // text
        UIManager.put("OptionPane.errorDialog.titlePane.background", Color.WHITE);
        UIManager.put("OptionPane.errorDialog.titlePane.foreground", Color.BLACK); // text
        UIManager.put("OptionPane.questionDialog.titlePane.background", Color.WHITE);
        UIManager.put("OptionPane.questionDialog.titlePane.foreground", Color.BLACK); // text
        UIManager.put("OptionPane.warningDialog.titlePane.background", Color.WHITE);
        UIManager.put("OptionPane.warningDialog.titlePane.foreground", Color.BLACK); // text
        UIManager.put("OptionPane.errorDialog.titlePane.background", Color.WHITE);
        UIManager.put("OptionPane.errorDialog.titlePane.foreground", Color.BLACK); // text
        UIManager.put("Panel.foreground", Color.WHITE);
        UIManager.put("InternalFrame.activeTitleBackground", Color.black);
        UIManager.put("InternalFrame.activeTitleForeground", Color.WHITE);
        UIManager.put("OptionPane.border", BorderFactory.createLineBorder(Color.WHITE));
        // fonts 
        InputStream stream = Andie.class.getClassLoader().getResourceAsStream("lexend-semibold.ttf");
        Font font = null;
        try{
           font = Font.createFont(Font.TRUETYPE_FONT, stream);
           font = font.deriveFont(Font.BOLD, 13);
        }catch(Exception e){
           e.printStackTrace();
        }
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font); 
        UIManager.put("OptionPane.errorDialog.titleFont", font); 
        UIManager.put("OptionPane.questionDialog.titleFont", font); 
        UIManager.put("OptionPane.warningDialog.titleFont", font); 
     }

     /**
     * Sets the colour and font of the JColorChooser dialog box.
     * 
     * Derived from: https://coderanch.com/t/342116/java/set-font-JFileChooser
     * by Michael Dunn.
     */
     public static void colorFontSlider(JSlider slider){
        InputStream stream = Andie.class.getClassLoader().getResourceAsStream("lexend-semibold.ttf");
          Font font = null;
          try{
             font = Font.createFont(Font.TRUETYPE_FONT, stream);
             font = font.deriveFont(Font.PLAIN, 11);
          }catch(Exception e){
             e.printStackTrace();
        }
        slider.setForeground(Color.BLACK); // set the color of the numbers to black
        slider.setFont(font);
     }

    /**
     * Sets the colour and font of the JFileChooser dialog box.
     * 
     * Derived from: https://coderanch.com/t/342116/java/set-font-JFileChooser
     * by Michael Dunn.
     */
    public static void setJChooserFont(Component[] comp){
        InputStream stream = Andie.class.getClassLoader().getResourceAsStream("lexend-semibold.ttf");
        Font font = null;
        try{
             font = Font.createFont(Font.TRUETYPE_FONT, stream);
             font = font.deriveFont(Font.PLAIN, 12);
        }catch(Exception e){
             e.printStackTrace();
        }
        // set the font and font color of the dialog box
        Color fontColor = Color.BLACK;
        for(int x = 0; x < comp.length; x++){
            if(comp[x] instanceof Container) setJChooserFont(((Container)comp[x]).getComponents());
            try{
                comp[x].setFont(font);
                comp[x].setForeground(fontColor);
            }catch(Exception e){}//do nothing
        }
    }
    
    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}
