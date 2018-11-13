import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @file KeyboardListener.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class KeyboardListener implements KeyListener, FocusListener {
    // public boolean[] keys = new boolean[128];
    // public ArrayList<Boolean> keys = new ArrayList<Boolean>();
    int[] keys;
    boolean[] keyStates;

    public KeyboardListener() {
        this(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Z);
    }

    public KeyboardListener(int upKey, int downKey, int leftKey, int rightKey, int actionKey) {
        // TODO: make sure key is in key boolean array
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

    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < 5; i++) {
            keyStates[i] = false;
        }
    }

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

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (int i = 0; i < 5; i++) {
            if (keyCode == keys[i])
                keyStates[i] = false;
        }
    }

    public boolean up() {
        return (keyStates[0]);
    }

    public boolean down() {
        return (keyStates[1]);
    }

    public boolean left() {
        return (keyStates[2]);
    }

    public boolean right() {
        return (keyStates[3]);
    }

    private int counter = 120;
    private int cooldown = 120;

    public boolean action() {
        counter++;
        if (keyStates[4])
            if (counter >= cooldown) {
                counter = 0;
                return true;
            }
        return false;
    }

}