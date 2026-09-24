package com.hotel.main;

import com.formdev.flatlaf.FlatLightLaf;
import com.hotel.view.MainDashboard;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Apply FlatLaf for a modern look
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);
        });
    }
}
