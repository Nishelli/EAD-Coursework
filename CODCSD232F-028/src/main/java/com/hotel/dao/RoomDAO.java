package com.hotel.dao;

import com.hotel.model.Room;
import com.hotel.util.RoomNotAvailableException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_number, room_type, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setDouble(3, room.getPrice());
            stmt.setString(4, room.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET room_type=?, price=?, status=? WHERE room_number=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, room.getRoomType());
            stmt.setDouble(2, room.getPrice());
            stmt.setString(3, room.getStatus());
            stmt.setString(4, room.getRoomNumber());
            stmt.executeUpdate();
        }
    }

    public void deleteRoom(String roomNumber) throws SQLException {
        String query = "DELETE FROM rooms WHERE room_number=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roomNumber);
            stmt.executeUpdate();
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getString("status")
                ));
            }
        }
        return rooms;
    }

    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE status='Available'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getString("status")
                ));
            }
        }
        return rooms;
    }

    public void updateRoomStatus(int roomId, String status) throws SQLException, RoomNotAvailableException {
        // First check current status if we are booking
        if (status.equals("Booked")) {
            String checkQuery = "SELECT status FROM rooms WHERE id = ?";
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, roomId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    if (!rs.getString("status").equals("Available")) {
                        throw new RoomNotAvailableException("Room is no longer available.");
                    }
                }
            }
        }

        String query = "UPDATE rooms SET status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        }
    }
}
