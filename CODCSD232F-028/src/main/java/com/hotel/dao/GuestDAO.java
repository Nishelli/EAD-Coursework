package com.hotel.dao;

import com.hotel.model.Guest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public void addGuest(Guest guest) throws SQLException {
        String query = "INSERT INTO guests (name, phone, email, identity_card) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getPhone());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getIdentityCard());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                guest.setId(rs.getInt(1));
            }
        }
    }

    public void updateGuest(Guest guest) throws SQLException {
        String query = "UPDATE guests SET name=?, phone=?, email=? WHERE identity_card=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getPhone());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getIdentityCard());
            stmt.executeUpdate();
        }
    }

    public void deleteGuest(String identityCard) throws SQLException {
        String query = "DELETE FROM guests WHERE identity_card=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, identityCard);
            stmt.executeUpdate();
        }
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                guests.add(new Guest(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("identity_card")
                ));
            }
        }
        return guests;
    }
}
