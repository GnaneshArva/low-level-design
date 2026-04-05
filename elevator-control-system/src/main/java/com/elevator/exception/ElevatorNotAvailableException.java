package com.elevator.exception;

public class ElevatorNotAvailableException extends RuntimeException {
    public ElevatorNotAvailableException(String message) {
        super(message);
    }
}
