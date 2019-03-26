#include "josh127/libwin32/libwin32.hh"
#include "josh127/libwin32/jni/dev_josh127_libwin32_User32.h"
#include <Windows.h>

static WNDCLASSEXW WindowClass;
static MSG Message;

static LRESULT CALLBACK WndProc(HWND window, UINT message, WPARAM wParam, LPARAM lParam) {
    switch (message) {
        case WM_DESTROY: {
            PostQuitMessage(0);
            return 0;
        }
    }

    return DefWindowProcW(window, message, wParam, lParam);
}

JNIEXPORT jint JNICALL Java_dev_josh127_libwin32_User32_nativeInit
  (JNIEnv *, jclass)
{
    WindowClass.cbSize = sizeof(WNDCLASSEXW);
    WindowClass.style = CS_HREDRAW | CS_VREDRAW;
    WindowClass.lpfnWndProc = WndProc;
    WindowClass.cbClsExtra = 0;
    WindowClass.cbWndExtra = 0;
    WindowClass.hInstance = GetModuleHandleW(0);
    WindowClass.hIcon = LoadIcon(0, IDI_APPLICATION);
    WindowClass.hCursor = LoadCursor(0, IDC_ARROW);
    WindowClass.hbrBackground = (HBRUSH) GetStockObject(WHITE_BRUSH);
    WindowClass.lpszMenuName = 0;
    WindowClass.lpszClassName = L"MAINWINDOW";
    WindowClass.hIconSm = LoadIcon(0, IDI_APPLICATION);
    return RegisterClassExW(&WindowClass) ? -1 : __LINE__;
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_User32_createWindow
  (JNIEnv *env, jclass clazz, jint width, jint height, jstring title)
{
    RECT rect{ };
    rect.left = 0;
    rect.top = 0;
    rect.right = width;
    rect.bottom = height;
    AdjustWindowRect(&rect, WS_OVERLAPPEDWINDOW, FALSE);
    const jchar* titleChars = env->GetStringChars(title, nullptr);
    HWND window{ CreateWindowExW(
        0,
        L"MAINWINDOW",
        reinterpret_cast<LPCWSTR>(titleChars),
        WS_OVERLAPPEDWINDOW | WS_VISIBLE,
        CW_USEDEFAULT,
        CW_USEDEFAULT,
        rect.right - rect.left,
        rect.bottom - rect.top,
        0, 0, GetModuleHandle(0), 0) };
    env->ReleaseStringChars(title, titleChars);
    return NativePtr::Create(env, window);
}

JNIEXPORT jboolean JNICALL Java_dev_josh127_libwin32_User32_peekEvent
  (JNIEnv *env, jclass clazz)
{
    PeekMessageW(&Message, 0, 0, 0, PM_REMOVE);
    return Message.message != WM_QUIT;
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_User32_dispatchEvents
  (JNIEnv *env, jclass clazz)
{
    TranslateMessage(&Message);
    DispatchMessageW(&Message);
}