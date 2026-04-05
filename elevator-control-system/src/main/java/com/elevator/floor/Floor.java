package com.elevator.floor;

import com.elevator.core.Direction;
import com.elevator.exception.InvalidFloorException;

/**
 * Floor represents a physical floor in the building.
 * It owns the up/down panel buttons — SRP.
 */
public class Floor {

    private final int floorNumber;
    private final int minFloor;
    private final int maxFloor;

    private boolean upButtonPressed;
    private boolean downButtonPressed;

    public Floor(int floorNumber, int minFloor, int maxFloor) {
        if (floorNumber < minFloor || floorNumber > maxFloor) {
            throw new InvalidFloorException(
                String.format("Floor %d is out of building range [%d, %d].", floorNumber, minFloor, maxFloor));
        }
        this.floorNumber = floorNumber;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    public void pressButton(Direction direction) {
        if (direction == Direction.UP) {
            if (floorNumber == maxFloor) {
                throw new InvalidFloorException("Cannot press UP on the topmost floor.");
            }
            upButtonPressed = true;
        } else if (direction == Direction.DOWN) {
            if (floorNumber == minFloor) {
                throw new InvalidFloorException("Cannot press DOWN on the ground floor.");
            }
            downButtonPressed = true;
        }
    }

    public void clearButton(Direction direction) {
        if (direction == Direction.UP)   upButtonPressed = false;
        if (direction == Direction.DOWN) downButtonPressed = false;
    }

    public boolean isUpButtonPressed()   { return upButtonPressed; }
    public boolean isDownButtonPressed() { return downButtonPressed; }
    public int getFloorNumber()          { return floorNumber; }

    @Override
    public String toString() {
        return String.format("Floor{number=%d, up=%s, down=%s}",
            floorNumber, upButtonPressed, downButtonPressed);
    }
}
