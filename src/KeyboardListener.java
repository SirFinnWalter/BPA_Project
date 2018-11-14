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
    int[] keys;
    boolean[] keyStates;

    /**
     * Sets keyCodes provided into the keys array.
     * 
     * @param upKey     The keyCode to move up.
     * @param downKey   The keyCode to move down.
     * @param leftKey   The keyCode to move left.
     * @param rightKey  The keyCode to move right.
     * @param actionKey The keyCode to act.
     */
    public KeyboardListener(int upKey, int downKey, int leftKey, int rightKey, int actionKey) {
        keys = new int[] { upKey, downKey, leftKey, rightKey, actionKey };
        keyStates = new boolean[5];
        Arrays.fill(keyStates, false);
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Sets every key state to false to prevent movement when not in focus.
     */
    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < 5; i++) {
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
        for (int i = 0; i < 5; i++) {
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
        for (int i = 0; i < 5; i++) {
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

    private int counter = 120;
    private int cooldown = 120;

    /**
     * Returns the key state of action. Has a cooldown of two frames before it may
     * be actived again.
     * 
     * @return The key state of action.
     */
    public boolean action() {
        counter++;
        if (keyStates[4] && counter >= cooldown) {
            counter = 0;
            return true;
        }
        return false;
    }

}