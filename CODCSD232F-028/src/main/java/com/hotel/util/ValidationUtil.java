package com.hotel.util;

public class ValidationUtil {

    public static void validateStringNotEmpty(String str, String fieldName) throws InvalidInputException {
        if (str == null || str.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
    }

    public static void validatePositiveNumber(double number, String fieldName) throws InvalidInputException {
        if (number <= 0) {
            throw new InvalidInputException(fieldName + " must be a positive number.");
        }
    }

    public static void validateDate(java.sql.Date checkIn, java.sql.Date checkOut) throws InvalidInputException {
        if (checkIn == null || checkOut == null) {
            throw new InvalidInputException("Dates cannot be null.");
        }
        if (checkOut.before(checkIn) || checkOut.equals(checkIn)) {
            throw new InvalidInputException("Check-out date must be after check-in date.");
        }
    }
}
