import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @file KeyboardListener.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class KeyboardListener implements KeyListener, FocusListener {
    public boolean[] keys = new boolean[128];
    int upKey, downKey, leftKey, rightKey, actionKey;

    public KeyboardListener() {
        this(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Z);
    }

    public KeyboardListener(int upKey, int downKey, int leftKey, int rightKey, int actionKey) {
        // TODO: make sure key is in key boolean array
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.actionKey = actionKey;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length)
            keys[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length)
            keys[keyCode] = false;
    }

    public boolean up() {
        return (keys[upKey]);
    }

    public boolean down() {
        return (keys[downKey]);
    }

    public boolean left() {
        return (keys[leftKey]);
    }

    public boolean right() {
        return (keys[rightKey]);
    }

    private int counter = 120;
    private int cooldown = 120;

    public boolean action() {
        counter++;
        if (keys[actionKey])
            if (counter >= cooldown) {
                counter = 0;
                return true;
            }
        return false;
    }

}