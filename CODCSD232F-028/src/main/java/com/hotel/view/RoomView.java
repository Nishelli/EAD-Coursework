package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Room;
import com.hotel.util.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomView extends JDialog {

    private HotelController controller;
    private JTextField txtRoomNumber, txtPrice;
    private JComboBox<String> cmbRoomType;
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomView(JFrame parent) {
        super(parent, "Manage Rooms", true);
        this.controller = new HotelController();
        
        setSize(700, 500);
        setLocationRelativeTo(parent);
        
        initComponents();
        loadRooms();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Room Number:"));
        txtRoomNumber = new JTextField();
        formPanel.add(txtRoomNumber);

        formPanel.add(new JLabel("Room Type:"));
        cmbRoomType = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        formPanel.add(cmbRoomType);

        formPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        JButton btnAdd = new JButton("Add Room");
        btnAdd.addActionListener(e -> addRoom());
        formPanel.add(btnAdd);
        
        JButton btnUpdate = new JButton("Update Room");
        btnUpdate.addActionListener(e -> updateRoom());
        formPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Room");
        btnDelete.addActionListener(e -> deleteRoom());
        formPanel.add(btnDelete);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"ID", "Room Number", "Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
            int row = roomTable.getSelectedRow();

            txtRoomNumber.setText(roomTable.getValueAt(row, 1).toString());
            cmbRoomType.setSelectedItem(roomTable.getValueAt(row, 2).toString());
            txtPrice.setText(roomTable.getValueAt(row, 3).toString());
    }
});
        add(mainPanel);
    }

    private void addRoom() {
        try {
            String roomNumber = txtRoomNumber.getText();
            String type = (String) cmbRoomType.getSelectedItem();
            double price = Double.parseDouble(txtPrice.getText());

            controller.addRoom(roomNumber, type, price);
            JOptionPane.showMessageDialog(this, "Room added successfully!");
            loadRooms();
            
            txtRoomNumber.setText("");
            txtPrice.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRooms() {
        try {
            tableModel.setRowCount(0);
            List<Room> rooms = controller.getAllRooms();
            for (Room r : rooms) {
                tableModel.addRow(new Object[]{r.getId(), r.getRoomNumber(), r.getRoomType(), r.getPrice(), r.getStatus()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load rooms: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateRoom() {
    try {
        String roomNumber = txtRoomNumber.getText();
        String type = (String) cmbRoomType.getSelectedItem();
        double price = Double.parseDouble(txtPrice.getText());

        controller.updateRoom(roomNumber, type, price, "Available");

        JOptionPane.showMessageDialog(this, "Room updated successfully!");
        loadRooms();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (InvalidInputException | SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void deleteRoom() {
    try {
        String roomNumber = txtRoomNumber.getText();

        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a room number.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Room " + roomNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteRoom(roomNumber);

            JOptionPane.showMessageDialog(this, "Room deleted successfully!");
            loadRooms();

            txtRoomNumber.setText("");
            txtPrice.setText("");
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}

