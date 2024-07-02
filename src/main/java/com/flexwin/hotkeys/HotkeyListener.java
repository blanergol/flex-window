package com.flexwin.hotkeys;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.flexwin.ui.FlexWindow;
import java.util.prefs.Preferences;

public class HotkeyListener implements NativeKeyListener {
    private Preferences prefs;
    private FlexWindow flexWindow;

    public HotkeyListener(FlexWindow flexWindow) {
        this.flexWindow = flexWindow;
        prefs = Preferences.userNodeForPackage(HotkeyListener.class);
    }

    public void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String fullscreenHotkey = prefs.get("centerHotkey", "Ctrl+Alt+Enter");
        String centerHotkey = prefs.get("fullscreenHotkey", "Ctrl+Alt+C");

        if (isHotkeyPressed(e, fullscreenHotkey)) {
            flexWindow.handleFullscreenAction();
        } else if (isHotkeyPressed(e, centerHotkey)) {
            flexWindow.handleCenterAction();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    private boolean isHotkeyPressed(NativeKeyEvent e, String hotkey) {
        String[] keys = hotkey.split("\\+");
        boolean ctrl = false, alt = false, key = false;

        for (String k : keys) {
            switch (k.trim().toLowerCase()) {
                case "ctrl":
                    ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
                    break;
                case "alt":
                    alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
                    break;
                default:
                    key = NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(k.trim());
                    break;
            }
        }
        return ctrl && alt && key;
    }
}
