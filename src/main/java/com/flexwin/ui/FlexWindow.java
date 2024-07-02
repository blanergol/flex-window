package com.flexwin.ui;

import com.flexwin.config.ConfigManager;
import com.flexwin.hotkeys.HotkeyListener;
import com.flexwin.startup.StartupManager;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;

public class FlexWindow extends JFrame {
    private static final String SECTION_HOTKEYS = "Hotkeys";
    private static final String SECTION_SETTINGS = "Settings";
    private static final String KEY_FULLSCREEN = "fullscreenHotkey";
    private static final String KEY_CENTER = "centerHotkey";
    private static final String KEY_AUTOSTART = "autostart";
    private static final String VERSION = "1.0.0";

    private ConfigManager configManager;
    private JTextField fullscreenHotkeyField;
    private JTextField centerHotkeyField;
    private JCheckBox autostartCheckBox;
    private TrayIcon trayIcon;
    private StartupManager startupManager;
    private TrayManager trayManager;

    public FlexWindow() {
        configManager = new ConfigManager();
        startupManager = new StartupManager();
        initUI();
        trayManager = new TrayManager(this); // Инициализация TrayManager
        new HotkeyListener(this).registerNativeHook();
    }

    private void initUI() {
        setTitle("Flex Window");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUIFont(new FontUIResource("Segoe UI", Font.PLAIN, 14));
        JPanel panel = createPanel();
        JLabel versionLabel = createVersionLabel();

        add(panel, BorderLayout.CENTER);
        add(versionLabel, BorderLayout.SOUTH);
    }

    public static void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel fullscreenLabel = new JLabel("Fullscreen Hotkey:");
        fullscreenHotkeyField = new JTextField(configManager.getConfigValue(SECTION_HOTKEYS, KEY_FULLSCREEN, "Ctrl+Alt+C"));
        JLabel centerLabel = new JLabel("Center Hotkey:");
        centerHotkeyField = new JTextField(configManager.getConfigValue(SECTION_HOTKEYS, KEY_CENTER, "Ctrl+Alt+Enter"));
        JLabel autostartLabel = new JLabel("Run at startup:");
        autostartCheckBox = new JCheckBox();
        autostartCheckBox.setSelected(Boolean.parseBoolean(configManager.getConfigValue(SECTION_SETTINGS, KEY_AUTOSTART, "false")));

        JButton saveButton = createSaveButton();

        panel.add(fullscreenLabel);
        panel.add(fullscreenHotkeyField);
        panel.add(centerLabel);
        panel.add(centerHotkeyField);
        panel.add(autostartLabel);
        panel.add(autostartCheckBox);
        panel.add(new JLabel());
        panel.add(saveButton);

        saveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        saveButton.setBackground(new Color(0, 120, 215));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);

        return panel;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            configManager.setConfigValue(SECTION_HOTKEYS, KEY_FULLSCREEN, fullscreenHotkeyField.getText());
            configManager.setConfigValue(SECTION_HOTKEYS, KEY_CENTER, centerHotkeyField.getText());
            boolean autostart = autostartCheckBox.isSelected();
            configManager.setConfigValue(SECTION_SETTINGS, KEY_AUTOSTART, Boolean.toString(autostart));
            if (autostart) {
                startupManager.addAppToStartup(getAppPath());
            } else {
                startupManager.removeAppFromStartup();
            }
            configManager.saveConfig();
            JOptionPane.showMessageDialog(FlexWindow.this, "Settings saved!");
        });
        return saveButton;
    }

    private JLabel createVersionLabel() {
        JLabel versionLabel = new JLabel("Version: " + VERSION, SwingConstants.CENTER);
        versionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return versionLabel;
    }

    public void handleFullscreenAction() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_SHOWMAXIMIZED);
    }

    public void handleCenterAction() {
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

    private String getAppPath() {
        return "\"" + System.getProperty("java.home") + "\\bin\\javaw.exe\" -jar \"" +
                new File(FlexWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath() + "\"";
    }
}
