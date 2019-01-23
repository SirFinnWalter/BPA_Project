package bpaproject.framecontent;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import bpaproject.GameWindow;

/**
 * @file WindowContent.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public abstract class WindowContent extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final long serialVersionUID = 8395917451549932189L;

    private GameWindow gw;

    public WindowContent(GameWindow gw) {
        if (gw != null)
            this.gw = gw;
        else {
            LOGGER.log(Level.SEVERE, "GameWindow cannot be null!");
            throw new NullPointerException("GameWindow cannot be null!");
        }
    }

    public void init() {
        gw.pack();
    }

    public void update() {
    }

    public void render() {
    }

    public GameWindow getGameWindow() {
        return this.gw;
    }
}
