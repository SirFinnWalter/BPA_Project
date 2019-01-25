package bpaproject;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * @file KeyboardListener.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * Process keyboard events and relays information of a key state for movement
 * and action. Upon losing focus, all key states are state to {@code false}.
 */
public class KeyboardListener implements KeyListener, FocusListener {
    private int[] keys;
    public boolean[] keyStates;

    /**
     * Sets keyCodes provided into the keys array.
     * 
     * @param upKey     The keyCode to move up.
     * @param downKey   The keyCode to move down.
     * @param leftKey   The keyCode to move left.
     * @param rightKey  The keyCode to move right.
     * @param actionKey The keyCode to act.
     */
    public KeyboardListener(int upKey, int downKey, int leftKey, int rightKey, int bombKey, int actionKey) {
        keys = new int[] { upKey, downKey, leftKey, rightKey, bombKey, actionKey };
        keyStates = new boolean[6];
        Arrays.fill(keyStates, false);
    }

    /**
     * Must be implemented
     */
    @Override
    public void focusGained(FocusEvent e) {
    }

    /**
     * Must be implemented
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Sets every key state to false to prevent movement when not in focus.
     */
    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < 6; i++) {
            keyStates[i] = false;
        }
    }

    /**
     * Checks if the keyCode matches one of the keys. If it does, then set the
     * corresponding key state to true.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (int i = 0; i < 6; i++) {
            if (keyCode == keys[i])
                keyStates[i] = true;
        }
        // if (keyCode < keys.length)
        // keys[keyCode] = true;
    }

    /**
     * Checks if the keyCode matches one of the keys. If it does, then set the
     * corresponding key state to false.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (int i = 0; i < 6; i++) {
            if (keyCode == keys[i])
                keyStates[i] = false;
        }
    }

    /**
     * @return The key state of up
     */
    public boolean up() {
        return (keyStates[0]);
    }

    /**
     * @return The key state of down
     */
    public boolean down() {
        return (keyStates[1]);
    }

    /**
     * @return The key state of left
     */
    public boolean left() {
        return (keyStates[2]);
    }

    /**
     * @return The key state of right
     */
    public boolean right() {
        return (keyStates[3]);
    }

    /**
     * @return The key state of bomb
     */
    public boolean bomb() {
        return (keyStates[4]);
    }

    /**
     * @return The key state of action
     */
    public boolean action() {
        return (keyStates[5]);
    }

}