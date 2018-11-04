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
        return (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]);
    }

    public boolean down() {
        return (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]);
    }

    public boolean left() {
        return (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]);
    }

    public boolean right() {
        return (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]);
    }

    public boolean action() {
        return (keys[KeyEvent.VK_Z] || keys[KeyEvent.VK_CONTROL]);
    }

}