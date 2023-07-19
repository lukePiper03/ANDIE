package cosc202.andie;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * A class that plays sound effects including a open sound, an exit sound, and an error sound.
 * 
 * @author Hannah Srzich
 */
public class SoundEffect {

    /**
     * Constructs a new SoundEffect object.
     */
    public SoundEffect(){ }

     /**
     * Plays the exit sound
     * 
     * Sound Effect by <a href="https://pixabay.com/users/universfield-28281460/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=132113">UNIVERSFIELD</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=132113">Pixabay</a>
     * Note, free of license and free for use sound.
     */
    public void playExitSound(){
        // Start up sound
        try {
           // Load mp3 file
           String soundFilePath = "exit_sound.wav";
           AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Andie.class.getClassLoader().getResourceAsStream(soundFilePath));
           AudioFormat format = audioInputStream.getFormat();
           DataLine.Info info = new DataLine.Info(Clip.class, format);
           Clip clip = (Clip) AudioSystem.getLine(info);

           // Open the audio input stream and start playing the sound
           clip.open(audioInputStream);
           clip.start();
           
           // Wait for 0.55 seconds
           Thread.sleep(550);
           
           // Stop playing the sound and close the clip
           clip.stop();
           clip.close();

       } catch (Exception e) {
           e.printStackTrace();
       }
       // Mark the thread for deletion
       Thread.currentThread().interrupt();
   }


     /**
     * Plays the error sound
     * 
     * Sound Effect by <a href="https://pixabay.com/users/universfield-28281460/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=132113">UNIVERSFIELD</a> from <a href="https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=132113">Pixabay</a>
     * Note, free of license and free for use sound.
     */
    public void playErrorSound(){
        // Start up sound
        try {
           // Load mp3 file
           String soundFilePath = "error_sound.wav";
           AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Andie.class.getClassLoader().getResourceAsStream(soundFilePath));
           AudioFormat format = audioInputStream.getFormat();
           DataLine.Info info = new DataLine.Info(Clip.class, format);
           Clip clip = (Clip) AudioSystem.getLine(info);

           // Open the audio input stream and start playing the sound
           clip.open(audioInputStream);
           clip.start();
           
           // Wait for 5 seconds
           Thread.sleep(5000);
           
           // Stop playing the sound and close the clip
           clip.stop();
           clip.close();

       } catch (Exception e) {
           e.printStackTrace();
       }
       // Mark the thread for deletion
       Thread.currentThread().interrupt();
   }

    /**
     * Plays the start up sound.
     * 
     * Sound file from: https://pixabay.com/sound-effects/search/mac%20startup/, free of license and free for use sound.
     */
    public void playStartUpSound(){
         // Start up sound
         try {
            // Wait for 2 seconds
            Thread.sleep(2000);
            // Load mp3 file
            String soundFilePath = "startup_sound.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Andie.class.getClassLoader().getResourceAsStream(soundFilePath));
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);

            // Open the audio input stream and start playing the sound
            clip.open(audioInputStream);
            clip.start();
            
            // Wait for 5 seconds
            Thread.sleep(5000);
            
            // Stop playing the sound and close the clip
            clip.stop();
            clip.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Mark the thread for deletion
        Thread.currentThread().interrupt();
    }
}
