package bpa_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

    private volatile boolean looping;
    private AudioInputStream audio;

    public AudioPlayer() {
    }

    public AudioPlayer(File file) {
        setAudio(file);
    }

    @Override
    public synchronized void run() {
        try {
            AudioInputStream audio = this.audio;
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            while (looping) {
                this.wait(clip.getMicrosecondLength() / 1000);

                if (this.audio != audio) {
                    audio = this.audio;
                    clip.close();
                    clip.open(audio);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }

            clip.stop();
            clip.close();
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
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(file)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            clip.addLineListener(new LineListener() {

                @Override
                public void update(LineEvent e) {
                    if (e.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
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
        try {
            LOGGER.log(Level.FINE, "Setting audio to " + file.getName());
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            this.audio = audio;
        } catch (UnsupportedAudioFileException | FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    public void loopAudio() {
        looping = true;
        LOGGER.log(Level.FINE, "Starting audio");
        new Thread(this).start();
    }

    public synchronized void endLoop() {
        LOGGER.log(Level.FINE, "Ending audio");
        looping = false;
        this.notify();

    }
}