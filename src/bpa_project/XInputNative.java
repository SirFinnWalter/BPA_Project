package bpa_project;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @file XInput.java
 * @author Dakota Taylor
 * @createdOn Sunday, 02 December, 2018
 */

public class XInputNative {
    private final static int[] dwPacketNumber = new int[4];

    /**
     * 
     * @param playerNum
     * @param data
     */
    public static XInputButtons getInput(int playerNum) {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16);
        buffer.order(ByteOrder.nativeOrder());
        if (XInputNative.getState(playerNum, buffer) != 0) {
            // System.out.println(playerNum + ": " + XInputNative.getState(playerNum,
            // buffer));
            return null;
        }

        /**
         * NOTE: Structure of ByteBuffer <code> 
          * 
          * typedef struct _XINPUT_STATE {}
          * DWORD dwPacketNumber;
          * XINPUT_GAMEPAD Gamepad;
          * } XINPUT_STATE, *PXINPUT_STATE;
          * 
          * typedef struct _XINPUT_GAMEPAD { 
          * WORD wButtons; BYTE bLeftTrigger;
          * BYTE bRightTrigger;
          * SHORT sThumbLX;
          * SHORT sThumbLY;
          * SHORT sThumbRX;
          * SHORTsThumbRY;
          * } XINPUT_GAMEPAD, *PXINPUT_GAMEPAD;
          * </code>
         */

        int dwPacketNumber = buffer.getInt();
        if (dwPacketNumber == XInputNative.dwPacketNumber[playerNum]) {
            return null;
        }
        XInputNative.dwPacketNumber[playerNum] = dwPacketNumber;

        final short wButtons = buffer.getShort();

        XInputButtons buttons = new XInputButtons();
        buttons.up = (wButtons & XINPUT_GAMEPAD.DPAD_UP) != 0;
        buttons.down = (wButtons & XINPUT_GAMEPAD.DPAD_DOWN) != 0;
        buttons.left = (wButtons & XINPUT_GAMEPAD.DPAD_LEFT) != 0;
        buttons.right = (wButtons & XINPUT_GAMEPAD.DPAD_RIGHT) != 0;
        buttons.start = (wButtons & XINPUT_GAMEPAD.START) != 0;
        buttons.back = (wButtons & XINPUT_GAMEPAD.BACK) != 0;
        buttons.leftThumb = (wButtons & XINPUT_GAMEPAD.LEFT_THUMB) != 0;
        buttons.rightThumb = (wButtons & XINPUT_GAMEPAD.RIGHT_THUMB) != 0;
        buttons.rightShoulder = (wButtons & XINPUT_GAMEPAD.RIGHT_SHOULDER) != 0;
        buttons.leftShoulder = (wButtons & XINPUT_GAMEPAD.LEFT_SHOULDER) != 0;
        buttons.a = (wButtons & XINPUT_GAMEPAD.A) != 0;
        buttons.b = (wButtons & XINPUT_GAMEPAD.B) != 0;
        buttons.x = (wButtons & XINPUT_GAMEPAD.X) != 0;
        buttons.y = (wButtons & XINPUT_GAMEPAD.Y) != 0;

        final byte bLeftTrigger = buffer.get();
        final byte bRightTrigger = buffer.get();
        final short sThumbLX = buffer.getShort();
        // if (sThumbLX > 26044)
        if (sThumbLX > 7849)
            buttons.right = true;
        else if (sThumbLX < -7849)
            buttons.left = true;
        final short sThumbLY = buffer.getShort();
        if (sThumbLY > 7849)
            buttons.up = true;
        else if (sThumbLY < -7849)
            buttons.down = true;

        final short sThumbRX = buffer.getShort();
        final short sThumbRY = buffer.getShort();

        return buttons;

    }

    public static native int getState(int playerNum, ByteBuffer data);

    final static class XINPUT_GAMEPAD {
        final static short DPAD_UP = 0x0001;
        final static short DPAD_DOWN = 0x0002;
        final static short DPAD_LEFT = 0x0004;
        final static short DPAD_RIGHT = 0x0008;
        final static short START = 0x0010;
        final static short BACK = 0x0020;
        final static short LEFT_THUMB = 0x0040;
        final static short RIGHT_THUMB = 0x0080;
        final static short LEFT_SHOULDER = 0x0100;
        final static short RIGHT_SHOULDER = 0x0200;
        final static short A = 0x1000;
        final static short B = 0x2000;
        final static short X = 0x4000;
        final static short Y = (short) 0x8000;
    }
}