package tutorial;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @file KeyboardListener.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 09 October, 2018
 */

public class KeyboardListener implements KeyListener, FocusListener {
    // 87 W
    // 65 A
    // 83 S
    // 68 D
    // 38 up
    // 37 d
    // 40 l
    // 39 r
    public boolean[] keys = new boolean[120];

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode < keys.length)
            keys[keyCode] = true;
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode < keys.length)
            keys[keyCode] = false;
    }

    public void focusLost(FocusEvent e) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public void focusGained(FocusEvent e) {
    }

    public boolean up() {
        return (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]);
    }

    public boolean down() {
        return (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]);
    }

    public boolean left() {
        return (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]);
    }

    public boolean right() {
        return (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]);
    }

}