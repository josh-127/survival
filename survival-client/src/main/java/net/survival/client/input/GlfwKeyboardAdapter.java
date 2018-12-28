package net.survival.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import survival.input.Key;

public class GlfwKeyboardAdapter extends KeyboardAdapter implements GLFWKeyCallbackI
{
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        Key convertedKey = null;

        switch (key) {
        case GLFW.GLFW_KEY_SPACE:         convertedKey = Key.SPACE;         break;
        case GLFW.GLFW_KEY_APOSTROPHE:    convertedKey = Key.APOSTROPHE;    break;
        case GLFW.GLFW_KEY_COMMA:         convertedKey = Key.COMMA;         break;
        case GLFW.GLFW_KEY_MINUS:         convertedKey = Key.MINUS;         break;
        case GLFW.GLFW_KEY_PERIOD:        convertedKey = Key.PERIOD;        break;
        case GLFW.GLFW_KEY_SLASH:         convertedKey = Key.SLASH;         break;
        case GLFW.GLFW_KEY_0:             convertedKey = Key._0;            break;
        case GLFW.GLFW_KEY_1:             convertedKey = Key._1;            break;
        case GLFW.GLFW_KEY_2:             convertedKey = Key._2;            break;
        case GLFW.GLFW_KEY_3:             convertedKey = Key._3;            break;
        case GLFW.GLFW_KEY_4:             convertedKey = Key._4;            break;
        case GLFW.GLFW_KEY_5:             convertedKey = Key._5;            break;
        case GLFW.GLFW_KEY_6:             convertedKey = Key._6;            break;
        case GLFW.GLFW_KEY_7:             convertedKey = Key._7;            break;
        case GLFW.GLFW_KEY_8:             convertedKey = Key._8;            break;
        case GLFW.GLFW_KEY_9:             convertedKey = Key._9;            break;
        case GLFW.GLFW_KEY_SEMICOLON:     convertedKey = Key.SEMICOLON;     break;
        case GLFW.GLFW_KEY_EQUAL:         convertedKey = Key.EQUAL;         break;
        case GLFW.GLFW_KEY_A:             convertedKey = Key.A;             break;
        case GLFW.GLFW_KEY_B:             convertedKey = Key.B;             break;
        case GLFW.GLFW_KEY_C:             convertedKey = Key.C;             break;
        case GLFW.GLFW_KEY_D:             convertedKey = Key.D;             break;
        case GLFW.GLFW_KEY_E:             convertedKey = Key.E;             break;
        case GLFW.GLFW_KEY_F:             convertedKey = Key.F;             break;
        case GLFW.GLFW_KEY_G:             convertedKey = Key.G;             break;
        case GLFW.GLFW_KEY_H:             convertedKey = Key.H;             break;
        case GLFW.GLFW_KEY_I:             convertedKey = Key.I;             break;
        case GLFW.GLFW_KEY_J:             convertedKey = Key.J;             break;
        case GLFW.GLFW_KEY_K:             convertedKey = Key.K;             break;
        case GLFW.GLFW_KEY_L:             convertedKey = Key.L;             break;
        case GLFW.GLFW_KEY_M:             convertedKey = Key.M;             break;
        case GLFW.GLFW_KEY_N:             convertedKey = Key.N;             break;
        case GLFW.GLFW_KEY_O:             convertedKey = Key.O;             break;
        case GLFW.GLFW_KEY_P:             convertedKey = Key.P;             break;
        case GLFW.GLFW_KEY_Q:             convertedKey = Key.Q;             break;
        case GLFW.GLFW_KEY_R:             convertedKey = Key.R;             break;
        case GLFW.GLFW_KEY_S:             convertedKey = Key.S;             break;
        case GLFW.GLFW_KEY_T:             convertedKey = Key.T;             break;
        case GLFW.GLFW_KEY_U:             convertedKey = Key.U;             break;
        case GLFW.GLFW_KEY_V:             convertedKey = Key.V;             break;
        case GLFW.GLFW_KEY_W:             convertedKey = Key.W;             break;
        case GLFW.GLFW_KEY_X:             convertedKey = Key.X;             break;
        case GLFW.GLFW_KEY_Y:             convertedKey = Key.Y;             break;
        case GLFW.GLFW_KEY_Z:             convertedKey = Key.Z;             break;
        case GLFW.GLFW_KEY_LEFT_BRACKET:  convertedKey = Key.LEFT_BRACKET;  break;
        case GLFW.GLFW_KEY_BACKSLASH:     convertedKey = Key.BACKSLASH;     break;
        case GLFW.GLFW_KEY_RIGHT_BRACKET: convertedKey = Key.RIGHT_BRACKET; break;
        case GLFW.GLFW_KEY_GRAVE_ACCENT:  convertedKey = Key.GRAVE_ACCENT;  break;
        case GLFW.GLFW_KEY_WORLD_1:       convertedKey = Key.WORLD_1;       break;
        case GLFW.GLFW_KEY_WORLD_2:       convertedKey = Key.WORLD_2;       break;
        case GLFW.GLFW_KEY_ESCAPE:        convertedKey = Key.ESCAPE;        break;
        case GLFW.GLFW_KEY_ENTER:         convertedKey = Key.ENTER;         break;
        case GLFW.GLFW_KEY_TAB:           convertedKey = Key.TAB;           break;
        case GLFW.GLFW_KEY_BACKSPACE:     convertedKey = Key.BACKSPACE;     break;
        case GLFW.GLFW_KEY_INSERT:        convertedKey = Key.INSERT;        break;
        case GLFW.GLFW_KEY_DELETE:        convertedKey = Key.DELETE;        break;
        case GLFW.GLFW_KEY_RIGHT:         convertedKey = Key.RIGHT;         break;
        case GLFW.GLFW_KEY_LEFT:          convertedKey = Key.LEFT;          break;
        case GLFW.GLFW_KEY_DOWN:          convertedKey = Key.DOWN;          break;
        case GLFW.GLFW_KEY_UP:            convertedKey = Key.UP;            break;
        case GLFW.GLFW_KEY_PAGE_UP:       convertedKey = Key.PAGE_UP;       break;
        case GLFW.GLFW_KEY_PAGE_DOWN:     convertedKey = Key.PAGE_DOWN;     break;
        case GLFW.GLFW_KEY_HOME:          convertedKey = Key.HOME;          break;
        case GLFW.GLFW_KEY_END:           convertedKey = Key.END;           break;
        case GLFW.GLFW_KEY_CAPS_LOCK:     convertedKey = Key.CAPS_LOCK;     break;
        case GLFW.GLFW_KEY_SCROLL_LOCK:   convertedKey = Key.SCROLL_LOCK;   break;
        case GLFW.GLFW_KEY_NUM_LOCK:      convertedKey = Key.NUM_LOCK;      break;
        case GLFW.GLFW_KEY_PRINT_SCREEN:  convertedKey = Key.PRINT_SCREEN;  break;
        case GLFW.GLFW_KEY_PAUSE:         convertedKey = Key.PAUSE;         break;
        case GLFW.GLFW_KEY_F1:            convertedKey = Key.F1;            break;
        case GLFW.GLFW_KEY_F2:            convertedKey = Key.F2;            break;
        case GLFW.GLFW_KEY_F3:            convertedKey = Key.F3;            break;
        case GLFW.GLFW_KEY_F4:            convertedKey = Key.F4;            break;
        case GLFW.GLFW_KEY_F5:            convertedKey = Key.F5;            break;
        case GLFW.GLFW_KEY_F6:            convertedKey = Key.F6;            break;
        case GLFW.GLFW_KEY_F7:            convertedKey = Key.F7;            break;
        case GLFW.GLFW_KEY_F8:            convertedKey = Key.F8;            break;
        case GLFW.GLFW_KEY_F9:            convertedKey = Key.F9;            break;
        case GLFW.GLFW_KEY_F10:           convertedKey = Key.F10;           break;
        case GLFW.GLFW_KEY_F11:           convertedKey = Key.F11;           break;
        case GLFW.GLFW_KEY_F12:           convertedKey = Key.F12;           break;
        case GLFW.GLFW_KEY_F13:           convertedKey = Key.F13;           break;
        case GLFW.GLFW_KEY_F14:           convertedKey = Key.F14;           break;
        case GLFW.GLFW_KEY_F15:           convertedKey = Key.F15;           break;
        case GLFW.GLFW_KEY_F16:           convertedKey = Key.F16;           break;
        case GLFW.GLFW_KEY_F17:           convertedKey = Key.F17;           break;
        case GLFW.GLFW_KEY_F18:           convertedKey = Key.F18;           break;
        case GLFW.GLFW_KEY_F19:           convertedKey = Key.F19;           break;
        case GLFW.GLFW_KEY_F20:           convertedKey = Key.F20;           break;
        case GLFW.GLFW_KEY_F21:           convertedKey = Key.F21;           break;
        case GLFW.GLFW_KEY_F22:           convertedKey = Key.F22;           break;
        case GLFW.GLFW_KEY_F23:           convertedKey = Key.F23;           break;
        case GLFW.GLFW_KEY_F24:           convertedKey = Key.F24;           break;
        case GLFW.GLFW_KEY_F25:           convertedKey = Key.F25;           break;
        case GLFW.GLFW_KEY_KP_0:          convertedKey = Key.KP_0;          break;
        case GLFW.GLFW_KEY_KP_1:          convertedKey = Key.KP_1;          break;
        case GLFW.GLFW_KEY_KP_2:          convertedKey = Key.KP_2;          break;
        case GLFW.GLFW_KEY_KP_3:          convertedKey = Key.KP_3;          break;
        case GLFW.GLFW_KEY_KP_4:          convertedKey = Key.KP_4;          break;
        case GLFW.GLFW_KEY_KP_5:          convertedKey = Key.KP_5;          break;
        case GLFW.GLFW_KEY_KP_6:          convertedKey = Key.KP_6;          break;
        case GLFW.GLFW_KEY_KP_7:          convertedKey = Key.KP_7;          break;
        case GLFW.GLFW_KEY_KP_8:          convertedKey = Key.KP_8;          break;
        case GLFW.GLFW_KEY_KP_9:          convertedKey = Key.KP_9;          break;
        case GLFW.GLFW_KEY_KP_DECIMAL:    convertedKey = Key.KP_DECIMAL;    break;
        case GLFW.GLFW_KEY_KP_DIVIDE:     convertedKey = Key.KP_DIVIDE;     break;
        case GLFW.GLFW_KEY_KP_MULTIPLY:   convertedKey = Key.KP_MULTIPLY;   break;
        case GLFW.GLFW_KEY_KP_SUBTRACT:   convertedKey = Key.KP_SUBTRACT;   break;
        case GLFW.GLFW_KEY_KP_ADD:        convertedKey = Key.KP_ADD;        break;
        case GLFW.GLFW_KEY_KP_ENTER:      convertedKey = Key.KP_ENTER;      break;
        case GLFW.GLFW_KEY_KP_EQUAL:      convertedKey = Key.KP_EQUAL;      break;
        case GLFW.GLFW_KEY_LEFT_SHIFT:    convertedKey = Key.LEFT_SHIFT;    break;
        case GLFW.GLFW_KEY_LEFT_CONTROL:  convertedKey = Key.LEFT_CONTROL;  break;
        case GLFW.GLFW_KEY_LEFT_ALT:      convertedKey = Key.LEFT_ALT;      break;
        case GLFW.GLFW_KEY_LEFT_SUPER:    convertedKey = Key.LEFT_SUPER;    break;
        case GLFW.GLFW_KEY_RIGHT_SHIFT:   convertedKey = Key.RIGHT_SHIFT;   break;
        case GLFW.GLFW_KEY_RIGHT_CONTROL: convertedKey = Key.RIGHT_CONTROL; break;
        case GLFW.GLFW_KEY_RIGHT_ALT:     convertedKey = Key.RIGHT_ALT;     break;
        case GLFW.GLFW_KEY_RIGHT_SUPER:   convertedKey = Key.RIGHT_SUPER;   break;
        case GLFW.GLFW_KEY_MENU:          convertedKey = Key.MENU;          break;
        }

        if (action == GLFW.GLFW_PRESS)
            pressKey(convertedKey);
        if (action == GLFW.GLFW_RELEASE)
            releaseKey(convertedKey);
    }
}