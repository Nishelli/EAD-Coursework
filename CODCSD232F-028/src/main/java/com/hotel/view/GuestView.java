package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Guest;
import com.hotel.util.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GuestView extends JDialog {

    private HotelController controller;
    private JTextField txtName, txtPhone, txtEmail, txtIdCard;
    private JTable guestTable;
    private DefaultTableModel tableModel;

    public GuestView(JFrame parent) {
        super(parent, "Manage Guests", true);
        this.controller = new HotelController();

        setSize(700, 500);
        setLocationRelativeTo(parent);

        initComponents();
        loadGuests();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("ID Card / Passport:"));
        txtIdCard = new JTextField();
        formPanel.add(txtIdCard);

        JButton btnAdd = new JButton("Add Guest");
        btnAdd.addActionListener(e -> addGuest());
        formPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Guest");
        btnUpdate.addActionListener(e -> updateGuest());
        formPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Guest");
        btnDelete.addActionListener(e -> deleteGuest());
        formPanel.add(btnDelete);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"ID", "Name", "Phone", "Email", "ID Card"};
        tableModel = new DefaultTableModel(columns, 0);
        guestTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(guestTable), BorderLayout.CENTER);
        guestTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && guestTable.getSelectedRow() != -1) {
            int row = guestTable.getSelectedRow();

        txtName.setText(guestTable.getValueAt(row, 1).toString());
        txtPhone.setText(guestTable.getValueAt(row, 2).toString());
        txtEmail.setText(guestTable.getValueAt(row, 3).toString());
        txtIdCard.setText(guestTable.getValueAt(row, 4).toString());
    }
});

        add(mainPanel);
    }

    private void addGuest() {
        try {
            String name = txtName.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String idCard = txtIdCard.getText();

            controller.addGuest(name, phone, email, idCard);
            JOptionPane.showMessageDialog(this, "Guest added successfully!");
            loadGuests();

            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtIdCard.setText("");
        } catch (InvalidInputException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateGuest() {
    try {
        String name = txtName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String idCard = txtIdCard.getText();

        controller.updateGuest(name, phone, email, idCard);

        JOptionPane.showMessageDialog(this, "Guest updated successfully!");
        loadGuests();

    } catch (InvalidInputException | SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void deleteGuest() {
    try {
        String idCard = txtIdCard.getText();

        if (idCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID Card / Passport number.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this guest?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteGuest(idCard);

            JOptionPane.showMessageDialog(this, "Guest deleted successfully!");
            loadGuests();

            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtIdCard.setText("");
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void loadGuests() {
        try {
            tableModel.setRowCount(0);
            List<Guest> guests = controller.getAllGuests();
            for (Guest g : guests) {
                tableModel.addRow(new Object[]{g.getId(), g.getName(), g.getPhone(), g.getEmail(), g.getIdentityCard()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load guests: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
