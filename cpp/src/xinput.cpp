#define WIN32_LEAN_AND_MEAN // to avoid including unnecessary stuff

#include <Windows.h>
#include <xinput.h>
#include <ShlObj.h>
#include <cstdio>

#include "bpa_project_XInputNative.h"

// taken from https://github.com/speps/XInputDotNet/blob/master/XInputInterface/GamePad.cpp

#define X_INPUT_GET_STATE(name) DWORD WINAPI name(DWORD dwUserIndex, XINPUT_STATE *pState)
typedef X_INPUT_GET_STATE(x_input_get_state);
X_INPUT_GET_STATE(XInputGetStateStub)
{
    return (ERROR_DEVICE_NOT_CONNECTED);
}
static x_input_get_state *XInputGetState_ = XInputGetStateStub;
#define XInputGetState XInputGetState_

static bool loaded;
static void loadXInput()
{
    HMODULE XInputLibrary = LoadLibraryA("xinput1_4.dll");

    if (!XInputLibrary)
        XInputLibrary = LoadLibraryA("xinput1_3.dll");

    if (!XInputLibrary)
        XInputLibrary = LoadLibraryA("xinput9_1_0.dll");

    if (XInputLibrary)
    {
        XInputGetState = (x_input_get_state *)GetProcAddress(XInputLibrary, "XInputGetState");
        loaded = true;
    }
    else
    {
        printf_s("Failed to load XInput! Make sure you have the drivers installed");
    }
}

DWORD XInputGamePadGetState(DWORD dwUserIndex, XINPUT_STATE *pState)
{
    if (!loaded)
    {
        loadXInput();
    }
    return XInputGetState(dwUserIndex, pState);
}

/*
 * Class:     XInputNative
 * Method:    getState
 * Signature: (ILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_bpaproject_XInputNative_getState(JNIEnv *env, jclass cls, jint playerNum, jobject byteBuffer)
{
    void *bbuf = env->GetDirectBufferAddress(byteBuffer);
    XINPUT_STATE *state = (XINPUT_STATE *)bbuf;
    ZeroMemory(state, sizeof(XINPUT_STATE));

    if (!loaded)
        loadXInput();

    return XInputGetState(playerNum, state);
}