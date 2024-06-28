package com.flexwin;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public class FlexWindow extends JFrame implements NativeKeyListener {
    private static final String PREF_FULLSCREEN_HOTKEY = "fullscreenHotkey";
    private static final String PREF_CENTER_HOTKEY = "centerHotkey";
    private static final String VERSION = "1.0.0";
    private Preferences prefs;
    private JTextField fullscreenHotkeyField;
    private JTextField centerHotkeyField;
    private TrayIcon trayIcon;

    public FlexWindow() {
        prefs = Preferences.userNodeForPackage(FlexWindow.class);
        initUI();
        initTray();
        registerNativeHook();
    }

    private void initUI() {
        setTitle("Flex Window");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel fullscreenLabel = new JLabel("Fullscreen Hotkey:");
        fullscreenHotkeyField = new JTextField(prefs.get(PREF_FULLSCREEN_HOTKEY, "F11"));
        JLabel centerLabel = new JLabel("Center Hotkey:");
        centerHotkeyField = new JTextField(prefs.get(PREF_CENTER_HOTKEY, "F12"));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prefs.put(PREF_FULLSCREEN_HOTKEY, fullscreenHotkeyField.getText());
                prefs.put(PREF_CENTER_HOTKEY, centerHotkeyField.getText());
                JOptionPane.showMessageDialog(FlexWindow.this, "Hotkeys saved!");
            }
        });

        panel.add(fullscreenLabel);
        panel.add(fullscreenHotkeyField);
        panel.add(centerLabel);
        panel.add(centerHotkeyField);
        panel.add(new JLabel());
        panel.add(saveButton);

        JLabel versionLabel = new JLabel("Version: " + VERSION, SwingConstants.CENTER);
        versionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panel, BorderLayout.CENTER);
        add(versionLabel, BorderLayout.SOUTH);
    }

    private void initTray() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            openItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            popup.add(openItem);
            popup.add(exitItem);
            trayIcon = new TrayIcon(image, "Window Manager", popup);
            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("System tray not supported!");
        }
    }

    private void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String fullscreenHotkey = prefs.get(PREF_FULLSCREEN_HOTKEY, "F11");
        String centerHotkey = prefs.get(PREF_CENTER_HOTKEY, "F12");

        if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(fullscreenHotkey)) {
            handleFullscreenAction();
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(centerHotkey)) {
            handleCenterAction();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    private void handleFullscreenAction() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOWMAXIMIZED);
    }

    private void handleCenterAction() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE);

        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);

        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;

        User32.INSTANCE.MoveWindow(hwnd, x, y, width, height, true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlexWindow wm = new FlexWindow();
                wm.setVisible(true);
            }
        });
    }
}
