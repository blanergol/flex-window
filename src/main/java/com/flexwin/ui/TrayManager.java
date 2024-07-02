package com.flexwin.ui;

import javax.swing.*;
import java.awt.*;

public class TrayManager {
    private TrayIcon trayIcon;
    private FlexWindow flexWindow;

    public TrayManager(FlexWindow flexWindow) {
        this.flexWindow = flexWindow;
        initTray();
    }

    private void initTray() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
            PopupMenu popup = createTrayPopupMenu();

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

    private PopupMenu createTrayPopupMenu() {
        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(e -> {
            flexWindow.setVisible(true);
            flexWindow.setExtendedState(JFrame.NORMAL);
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        popup.add(openItem);
        popup.add(exitItem);

        return popup;
    }
}
