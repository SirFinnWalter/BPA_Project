import java.nio.ByteBuffer;

/**
 * @file XInput.java
 * @author Dakota Taylor
 * @createdOn Sunday, 02 December, 2018
 */

public class XInputNative {

    public static native int getState(int playerNum, ByteBuffer data);
}