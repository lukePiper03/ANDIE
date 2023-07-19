/**
 * The cosc202.andie package contains classes that define actions to change the language preferences in the GUI application. 
 * The actions include English, Maori, Italian, Samoan, and French. These classes use the Abstract Window Toolkit (AWT) and Swing libraries 
 * to create menus with language options.
 * 
 * @since 1.0
 */
package cosc202.andie;

import java.util.*;
import java.awt.FontFormatException;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

/**
 * The LanguageActions class represents a set of actions for the Language menu to change the language preference settings in the GUI application. 
 * It provides a list of actions for the menu, and a method to create the menu with the list of actions. The class uses the Abstract Window 
 * Toolkit (AWT) and Swing libraries to create the menu.
 * 
 * @author Hannah Srzich adapted from Steven Mills' Action classes
 * @version 1.0
 * @since 1.0
 */
public class LanguageActions {
    
    /** A list of actions for the Language menu. */
    protected ArrayList<Action> actions;
    //Locale defaultLanguage = new Locale(Andie.prefs.get("language", "en"));
    
    /**
    * Creates a new set of actions for the Language menu.
    * 
    * @since 1.0
     */
   public LanguageActions() {
        actions = new ArrayList<Action>();
        actions.add(new EnglishAction(Andie.bundle.getString("english"), null, Andie.bundle.getString("englishDesc"), Integer.valueOf(KeyEvent.VK_0)));
        actions.add(new MaoriAction(Andie.bundle.getString("maori"), null, Andie.bundle.getString("maoriDesc"), Integer.valueOf(KeyEvent.VK_1)));
        actions.add(new ItalianAction(Andie.bundle.getString("italian"), null, Andie.bundle.getString("italianDesc"), Integer.valueOf(KeyEvent.VK_2)));
        actions.add(new SamoanAction(Andie.bundle.getString("samoan"), null, Andie.bundle.getString("samoanDesc"), Integer.valueOf(KeyEvent.VK_3)));
        actions.add(new FrenchAction(Andie.bundle.getString("french"), null, Andie.bundle.getString("frenchDesc"), Integer.valueOf(KeyEvent.VK_4)));
    }
    /**
     * <p>
     * Create a menu contianing the list of Language actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu languageMenu = new JMenu(Andie.bundle.getString("language"));
        for(Action action: actions) {
            languageMenu.add(new JMenuItem(action));
        }
        return languageMenu;
    }
    /**
     * The EnglishAction class represents an action to change the language preference to English.
     * 
     * @see ImageAction
     * @since 1.0
     */
    public class EnglishAction extends ImageAction {
         /**
         * Creates a new action to change the language preference to English.
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @since 1.0
         */
        public EnglishAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the EnglishAction is triggered. 
         * </p>
         * 
         * <p>
         * Handles the action event triggered by a user selecting a new language.
         * Removes the current language preference and sets the new language as the default
         * locale, and then reloads the resource bundle and recreates the GUI to reflect the
         * new language.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.prefs.remove("language");
            Andie.prefs.put("language", "en");
            Locale.setDefault(new Locale(Andie.prefs.get("language", "en")));
            Andie.bundle = ResourceBundle.getBundle("MessageBundle");
            try {
                Andie.makeJMenu(true);
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * The MaoriAction class represents an action to change the language preference to Maori.
     * 
     * @see ImageAction
     * @since 1.0
     */
    public class MaoriAction extends ImageAction {
         /**
         * Creates a new action to change the language preference to Maori.
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @since 1.0
         */
        public MaoriAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
         /**
         * <p>
         * Callback for when the MaoriAction is triggered. 
         * </p>
         * 
         * <p>
         * Handles the action event triggered by a user selecting a new language.
         * Removes the current language preference and sets the new language as the default
         * locale, and then reloads the resource bundle and recreates the GUI to reflect the
         * new language.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e)  {
            Andie.prefs.remove("language");
            Andie.prefs.put("language", "mi");
            Locale.setDefault(new Locale(Andie.prefs.get("language", "mi")));
            Andie.bundle = ResourceBundle.getBundle("MessageBundle");
            try {
                Andie.makeJMenu(true);
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
        }
    }
     /**
     * The ItalianAction class represents an action to change the language preference to Italian.
     * 
     * @see ImageAction
     * @since 1.0
     */
    public class ItalianAction extends ImageAction {
         /**
         * Creates a new action to change the language preference to Italian.
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @since 1.0
         */
        public ItalianAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the ItalianAction is triggered. 
         * </p>
         * 
         * <p>
         * Handles the action event triggered by a user selecting a new language.
         * Removes the current language preference and sets the new language as the default
         * locale, and then reloads the resource bundle and recreates the GUI to reflect the
         * new language.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.prefs.remove("language");
            Andie.prefs.put("language", "it");
            Locale.setDefault(new Locale(Andie.prefs.get("language", "it")));
            Andie.bundle = ResourceBundle.getBundle("MessageBundle");
            try {
                Andie.makeJMenu(true);
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
        }
    }
     /**
     * The SamoanAction class represents an action to change the language preference to Samoan.
     * 
     * @see ImageAction
     * @since 1.0
     */
    public class SamoanAction extends ImageAction {
        /**
         * Creates a new action to change the language preference to Samoan.
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @since 1.0
         */
        public SamoanAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the SamoanAction is triggered. 
         * </p>
         * 
         * <p>
         * Handles the action event triggered by a user selecting a new language.
         * Removes the current language preference and sets the new language as the default
         * locale, and then reloads the resource bundle and recreates the GUI to reflect the
         * new language.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.prefs.remove("language");
            Andie.prefs.put("language", "sm");
            Locale.setDefault(new Locale(Andie.prefs.get("language", "sm")));
            Andie.bundle = ResourceBundle.getBundle("MessageBundle");
            try {
                Andie.makeJMenu(true);
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
        }
    }

     /**
     * The FrenchAction class represents an action to change the language preference to French.
     * 
     * @see ImageAction
     * @since 1.0
     */
    public class FrenchAction extends ImageAction {
         /**
         * Creates a new action to change the language preference to French.
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @since 1.0
         */
        public FrenchAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the FrenchAction is triggered. 
         * </p>
         * 
         * <p>
         * Handles the action event triggered by a user selecting a new language.
         * Removes the current language preference and sets the new language as the default
         * locale, and then reloads the resource bundle and recreates the GUI to reflect the
         * new language.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Andie.prefs.remove("language");
            Andie.prefs.put("language", "fc");
            Locale.setDefault(new Locale(Andie.prefs.get("language", "fc")));
            Andie.bundle = ResourceBundle.getBundle("MessageBundle");
            try {
                Andie.makeJMenu(true);
            } catch (IOException | FontFormatException e1) {
                e1.printStackTrace();
            }
        }
    }
}
