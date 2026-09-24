package com.hotel.view;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Hotel Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Welcome to Hotel Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton btnManageRooms = new JButton("Manage Rooms");
        btnManageRooms.setFont(new Font("Arial", Font.PLAIN, 18));
        btnManageRooms.addActionListener(e -> new RoomView(this).setVisible(true));

        JButton btnManageGuests = new JButton("Manage Guests");
        btnManageGuests.setFont(new Font("Arial", Font.PLAIN, 18));
        btnManageGuests.addActionListener(e -> new GuestView(this).setVisible(true));

        JButton btnBooking = new JButton("Room Booking (Check-In)");
        btnBooking.setFont(new Font("Arial", Font.PLAIN, 18));
        btnBooking.addActionListener(e -> new BookingView(this).setVisible(true));

        JButton btnReports = new JButton("Management Reports");
        btnReports.setFont(new Font("Arial", Font.PLAIN, 18));
        btnReports.addActionListener(e -> generateReport());

        buttonPanel.add(btnManageRooms);
        buttonPanel.add(btnManageGuests);
        buttonPanel.add(btnBooking);
        buttonPanel.add(btnReports);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void generateReport() {
        // We will implement JasperReports generation here
        JOptionPane.showMessageDialog(this, "Generating report...", "Info", JOptionPane.INFORMATION_MESSAGE);
        ReportViewer.showReport();
    }
}
