package bpaproject.framecontent;

import java.io.File;

import javax.swing.JPanel;

import bpaproject.GameWindow;

/**
 * @file WindowContent.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

/**
 * A {@code FrameContent} class is an extension of {@code JPanel} that is used
 * to show content in a JFrame and requires a {@code GameWindow} to function
 * properly. Similar to a {@code GameObject} it has an init(), update(), and
 * render() function for controlling the content displayed in the window.
 */
public abstract class FrameContent extends JPanel {
    private static final long serialVersionUID = 8395917451549932189L;

    private GameWindow gw;

    public FrameContent(GameWindow gw) {
        this.gw = gw;
    }

    public void init() {
        if (!(this instanceof MainMenu))
            gw.getAudioPlayer().playAudio(new File("assets\\audio\\Selection.wav"));
    }

    public void update() {
    }

    public void render() {
    }

    public GameWindow getGameWindow() {
        return this.gw;
    }
}
