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

public class AudioPlayer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private float volume;
    private volatile boolean looping;
    private AudioInputStream audio;

    public AudioPlayer() {
        setVolume(Float.parseFloat(Options.getValue(Options.Setting.VOLUME).trim()));
    }

    public AudioPlayer(File file) {
        super();
        setAudio(file);
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        LOGGER.log(Level.FINER, "Changing volume from " + this.volume + " to " + volume);
        if (volume < 0f || volume > 1f) {
            LOGGER.log(Level.WARNING, "Volume of " + volume + " is not in the range of 0.0 to 1.0");
            return;
        }

        this.volume = volume;
    }

    private void modifyVolume(Clip clip) {
        LOGGER.log(Level.FINEST, "Modifying volume of clip.");

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    @Override
    public synchronized void run() {
        try {
            AudioInputStream audio = this.audio;
            float volume = this.volume;
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            modifyVolume(clip);
            while (looping) {
                this.wait(clip.getMicrosecondLength() / 1000);

                if (this.audio != audio) {
                    audio = this.audio;
                    clip.close();
                    clip.open(audio);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                if (this.volume != volume) {
                    volume = this.volume;
                    modifyVolume(clip);
                }
            }

            clip.stop();
            clip.close();
            audio.close();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            ex.printStackTrace();
        } catch (LineUnavailableException | InterruptedException | IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void playAudio(File file) {
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
        } catch (UnsupportedAudioFileException | FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        } catch (LineUnavailableException | IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setAudio(File file) {
        LOGGER.log(Level.FINE, "Setting audio to " + file.getName());
        try {
            if (!file.exists())
                throw new FileNotFoundException("Could not find file: " + file.getAbsolutePath());

            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            this.audio = audio;
        } catch (UnsupportedAudioFileException | FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    public void loopAudio() {
        if (!looping) {
            looping = true;
            LOGGER.log(Level.FINE, "Starting audio.");
            new Thread(this).start();
        }
    }

    public synchronized void endLoop() {
        LOGGER.log(Level.FINE, "Ending audio.");
        looping = false;
        this.notify();
    }
}