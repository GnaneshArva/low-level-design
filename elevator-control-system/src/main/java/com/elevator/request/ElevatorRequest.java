package com.elevator.request;

import com.elevator.core.Direction;

/**
 * Immutable value object representing a single elevator request.
 *
 * External requests carry a direction (the button pressed on the floor panel).
 * Internal requests carry a destination floor (the button pressed inside).
 * Emergency requests bypass normal queue ordering.
 */
public final class ElevatorRequest {

    private final int sourceFloor;
    private final int destinationFloor; // -1 if not yet known (external request)
    private final Direction direction;
    private final RequestType requestType;

    private ElevatorRequest(Builder builder) {
        this.sourceFloor = builder.sourceFloor;
        this.destinationFloor = builder.destinationFloor;
        this.direction = builder.direction;
        this.requestType = builder.requestType;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public boolean isEmergency() {
        return requestType == RequestType.EMERGENCY;
    }

    /**
     * Returns the floor the elevator should travel to next for this request.
     * For external requests without a destination, the target is the source floor.
     */
    public int getEffectiveTargetFloor() {
        return (destinationFloor >= 0) ? destinationFloor : sourceFloor;
    }

    @Override
    public String toString() {
        return String.format("ElevatorRequest{type=%s, src=%d, dst=%d, dir=%s}",
            requestType, sourceFloor, destinationFloor, direction);
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static Builder builder(int sourceFloor, RequestType type) {
        return new Builder(sourceFloor, type);
    }

    public static final class Builder {
        private final int sourceFloor;
        private final RequestType requestType;
        private int destinationFloor = -1;
        private Direction direction = Direction.IDLE;

        private Builder(int sourceFloor, RequestType requestType) {
            if (sourceFloor < 0) {
                throw new IllegalArgumentException("Source floor must be non-negative.");
            }
            this.sourceFloor = sourceFloor;
            this.requestType = requestType;
        }

        public Builder destinationFloor(int floor) {
            if (floor < 0) {
                throw new IllegalArgumentException("Destination floor must be non-negative.");
            }
            this.destinationFloor = floor;
            return this;
        }

        public Builder direction(Direction dir) {
            this.direction = dir;
            return this;
        }

        public ElevatorRequest build() {
            return new ElevatorRequest(this);
        }
    }
}
