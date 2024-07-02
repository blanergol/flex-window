package com.flexwin;

import com.flexwin.ui.FlexWindow;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            FlexWindow window = new FlexWindow();
            window.setVisible(true);
        });
    }
}
