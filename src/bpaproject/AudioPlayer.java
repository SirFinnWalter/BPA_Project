package bpaproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @file AudioThread.java
 * @author Dakota Taylor
 * @createdOn Monday, 21 January, 2019
 */

/**
 * The class {@code AudioPlayer} is for playing and looping audio. As many audio
 * can be played at once, but only one may be looping continously. The volume is
 * retrieved from the VOLUME setting in {@code Options} and can be changed at
 * anytime.
 */
public class AudioPlayer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private float volume = 1.0f;
    private volatile boolean looping;
    private AudioInputStream audio;

    /**
     * Sets the volume of the AudioPlayer to the value of Setting.VOLUME
     */
    public AudioPlayer() {
        setVolume(Integer.parseInt(Options.getValue(Options.Setting.VOLUME).trim()));
    }

    /**
     * Sets the volume of the AudioPlayer to the value of Setting.VOLUME and sets
     * the looping audio to the file.
     * 
     * @param file
     */
    public AudioPlayer(File file) {
        super();
        setAudio(file);
    }

    /**
     * Returns the volume converted to a scale of 0 to 100. Conversion makes it
     * easier to compare volume values and easier to write as a percentage.
     * 
     * @return the volume on a scale of 0 to 100
     */
    public int getVolume() {
        return Math.round(this.volume * 100);
    }

    /**
     * Sets a volume in the range of 0 to 100 to the volume.
     * <p>
     * If the volume is outside of the range nothing is changed. Otherwise, converts
     * the volume to a scale of 0.0 to 1.0 so it can be used properly by the
     * {@code AudioPlayer}
     * 
     * @param volume the volume to set
     */
    public void setVolume(int volume) {
        LOGGER.log(Level.FINER, "Changing volume from " + getVolume() + " to " + volume);
        if (volume < 0 || volume > 100) {
            LOGGER.log(Level.WARNING, "Volume of " + volume + " is not in the range of 0 to 100");
            return;
        }
        this.volume = volume / 100.0f;

        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Gets the {@code clip} gain control and sets it value to the volume convert
     * into decibels.
     * 
     * @param clip The clip
     */
    private void modifyVolume(Clip clip) {
        LOGGER.log(Level.FINEST, "Modifying volume of clip.");

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    /**
     * Creates and starts a {@code Clip} based on the audio if it exist. Plays the
     * audio on loop then waits until notified or the clip duration passed.
     * <p>
     * If the audio changes, then closes the current audio and starts the clip with
     * the new audio. If the volume changes, then modifiy the clip volume.
     */
    @Override
    public void run() {
        try {
            AudioInputStream audio = null;
            float volume = this.volume;
            Clip clip = AudioSystem.getClip();

            while (looping) {
                if (this.audio != audio && this.audio != null) {
                    audio = this.audio;
                    clip.close();
                    clip.open(audio);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    modifyVolume(clip);
                }
                if (this.volume != volume) {
                    volume = this.volume;
                    modifyVolume(clip);
                }

                synchronized (this) {
                    this.wait(clip.getMicrosecondLength() / 1000);
                }
            }

            clip.stop();
            clip.close();
            audio.close();
        } catch (LineUnavailableException | InterruptedException | IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        } catch (Exception ex) {
            GameWindow.crash(ex);
        }
    }

    /**
     * Creates and starts a {@code Clip} based on the audio if it exist.
     */
    public void playAudio() {
        if (getVolume() != 0 && audio != null) {
            LOGGER.log(Level.FINER, "Playing audio.");
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
                modifyVolume(clip);
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent e) {
                        if (e.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    }
                });
            } catch (IOException | LineUnavailableException ex) {
                LOGGER.log(Level.WARNING, ex.toString(), ex);
            }

        }
    }

    /**
     * Creates and starts a {@code Clip} based on the file.
     * 
     * @param file The audio file to play.
     */
    public void playAudio(File file) {
        if (getVolume() != 0) {
            LOGGER.log(Level.FINEST, "Playing " + file.getName());
            try {
                if (!file.exists())
                    throw new FileNotFoundException("Could not find file: " + file.getAbsolutePath());

                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
                modifyVolume(clip);
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent e) {
                        if (e.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    }
                });
                audio.close();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                LOGGER.log(Level.WARNING, ex.toString(), ex);
            }
        }
    }

    /**
     * If the audio is not looping, then starts a new Thread to continuously loop
     * the audio.
     */
    public void loopAudio() {
        if (!looping) {
            looping = true;
            LOGGER.log(Level.FINE, "Starting audio.");
            new Thread(this).start();
        }
    }

    /**
     * Sets the looping to false and notifies the AudioPlayer to close the audio.
     */
    public void endLoop() {
        LOGGER.log(Level.FINE, "Ending audio.");
        looping = false;
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Creates an {@code AudioInputStream} based on the file and sets it to the
     * audio. The audio can either be looped or played.
     * 
     * @param file The audio file to set
     */
    public void setAudio(File file) {
        LOGGER.log(Level.FINE, "Setting audio to " + file.getName());
        try {
            if (!file.exists())
                throw new FileNotFoundException("Could not find file: " + file.getAbsolutePath());

            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            this.audio = audio;
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }
    }
}