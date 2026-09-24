package com.hotel.controller;

import com.hotel.dao.BookingDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.util.InvalidInputException;
import com.hotel.util.RoomNotAvailableException;
import com.hotel.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class HotelController {

    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private BookingDAO bookingDAO;

    public HotelController() {
        this.roomDAO = new RoomDAO();
        this.guestDAO = new GuestDAO();
        this.bookingDAO = new BookingDAO();
    }

    // --- Room Operations ---
    public void addRoom(String roomNumber, String roomType, double price) throws InvalidInputException, SQLException {
        ValidationUtil.validateStringNotEmpty(roomNumber, "Room Number");
        ValidationUtil.validateStringNotEmpty(roomType, "Room Type");
        ValidationUtil.validatePositiveNumber(price, "Price");

        Room room = new Room(0, roomNumber, roomType, price, "Available");
        roomDAO.addRoom(room);
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public List<Room> getAvailableRooms() throws SQLException {
        return roomDAO.getAvailableRooms();
    }
    
    public void updateRoom(String roomNumber, String roomType, double price, String status)
        throws InvalidInputException, SQLException {

    ValidationUtil.validateStringNotEmpty(roomNumber, "Room Number");
    ValidationUtil.validateStringNotEmpty(roomType, "Room Type");
    ValidationUtil.validatePositiveNumber(price, "Price");

    Room room = new Room(0, roomNumber, roomType, price, status);
    roomDAO.updateRoom(room);
}

    public void deleteRoom(String roomNumber) throws SQLException {
    roomDAO.deleteRoom(roomNumber);
}   

    // --- Guest Operations ---
    public void addGuest(String name, String phone, String email, String idCard) throws InvalidInputException, SQLException {
        ValidationUtil.validateStringNotEmpty(name, "Name");
        ValidationUtil.validateStringNotEmpty(phone, "Phone");
        ValidationUtil.validateStringNotEmpty(idCard, "Identity Card");

        Guest guest = new Guest(0, name, phone, email, idCard);
        guestDAO.addGuest(guest);
    }
    
    public List<Guest> getAllGuests() throws SQLException {
        return guestDAO.getAllGuests();
    }
    public void updateGuest(String name, String phone, String email, String idCard)
        throws InvalidInputException, SQLException {

    ValidationUtil.validateStringNotEmpty(name, "Name");
    ValidationUtil.validateStringNotEmpty(phone, "Phone");
    ValidationUtil.validateStringNotEmpty(idCard, "Identity Card");

    Guest guest = new Guest(0, name, phone, email, idCard);
    guestDAO.updateGuest(guest);
}

    public void deleteGuest(String idCard) throws SQLException {
    guestDAO.deleteGuest(idCard);
}

    // --- Booking Operations ---
    public void createBooking(Guest guest, Room room, java.sql.Date checkIn, java.sql.Date checkOut) 
            throws InvalidInputException, RoomNotAvailableException, SQLException {
        
        ValidationUtil.validateDate(checkIn, checkOut);
        if (guest == null || room == null) {
            throw new InvalidInputException("Guest and Room must be selected.");
        }

        // Calculate amount (simplified: 1 day minimum)
        long diff = checkOut.getTime() - checkIn.getTime();
        long days = (diff / (1000 * 60 * 60 * 24));
        if (days == 0) days = 1;
        double totalAmount = days * room.getPrice();

        Booking booking = new Booking(0, guest.getId(), room.getId(), checkIn, checkOut, totalAmount);
        
        // Transaction style logic (though we rely on basic JDBC here without explicit start/commit for simplicity, 
        // to strictly follow standard exception handling)
        roomDAO.updateRoomStatus(room.getId(), "Booked");
        bookingDAO.addBooking(booking);
    }
}
