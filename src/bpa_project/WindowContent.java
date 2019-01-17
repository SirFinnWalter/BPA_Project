package bpa_project;

import javax.swing.JPanel;

/**
 * @file WindowContent.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public abstract class WindowContent extends JPanel {
    private boolean running;
    private GameWindow gw;

    public WindowContent(GameWindow gw) {
        this.gw = gw;
    }

    public void init() {
        this.running = true;

    }

    public void update() {
    }

    public void render() {
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public GameWindow getGameWindow() {
        return this.gw;
    }
}
