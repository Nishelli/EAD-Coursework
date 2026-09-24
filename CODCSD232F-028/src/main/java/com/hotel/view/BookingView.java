package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.util.InvalidInputException;
import com.hotel.util.RoomNotAvailableException;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class BookingView extends JDialog {

    private HotelController controller;
    private JComboBox<Guest> cmbGuests;
    private JComboBox<Room> cmbRooms;
    private JTextField txtCheckIn, txtCheckOut;

    public BookingView(JFrame parent) {
        super(parent, "Room Booking", true);
        this.controller = new HotelController();

        setSize(400, 300);
        setLocationRelativeTo(parent);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Select Guest:"));
        cmbGuests = new JComboBox<>();
        formPanel.add(cmbGuests);

        formPanel.add(new JLabel("Select Room:"));
        cmbRooms = new JComboBox<>();
        formPanel.add(cmbRooms);

        formPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        txtCheckIn = new JTextField();
        formPanel.add(txtCheckIn);

        formPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        txtCheckOut = new JTextField();
        formPanel.add(txtCheckOut);

        JButton btnBook = new JButton("Book Room");
        btnBook.addActionListener(e -> processBooking());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        formPanel.add(btnBook);
        formPanel.add(btnCancel);

        add(formPanel);
    }

    private void loadData() {
        try {
            List<Guest> guests = controller.getAllGuests();
            for (Guest g : guests) {
                cmbGuests.addItem(g);
            }

            List<Room> rooms = controller.getAvailableRooms();
            for (Room r : rooms) {
                cmbRooms.addItem(r);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processBooking() {
        try {
            Guest selectedGuest = (Guest) cmbGuests.getSelectedItem();
            Room selectedRoom = (Room) cmbRooms.getSelectedItem();
            
            Date checkIn = Date.valueOf(txtCheckIn.getText());
            Date checkOut = Date.valueOf(txtCheckOut.getText());

            controller.createBooking(selectedGuest, selectedRoom, checkIn, checkOut);
            
            JOptionPane.showMessageDialog(this, "Booking Successful!");
            dispose();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException | RoomNotAvailableException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
