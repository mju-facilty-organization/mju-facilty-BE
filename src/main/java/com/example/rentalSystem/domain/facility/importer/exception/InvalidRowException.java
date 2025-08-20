package com.example.rentalSystem.domain.facility.importer.exception;

public class InvalidRowException extends RuntimeException {
    private final int rowIndex;

    public InvalidRowException(int rowIndex, String message) {
        super(message);
        this.rowIndex = rowIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}