package com.elevator.facade;

import com.elevator.core.Direction;
import com.elevator.core.Elevator;
import com.elevator.core.ElevatorController;
import com.elevator.core.ElevatorObserver;
import com.elevator.exception.InvalidFloorException;
import com.elevator.floor.Floor;
import com.elevator.request.ElevatorRequest;
import com.elevator.request.RequestType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Facade pattern: ElevatorSystem is the single entry point for external clients.
 *
 * Hides the complexity of:
 *  - Floor management
 *  - Request construction
 *  - Controller dispatch
 *
 * Client code never touches Elevator, ElevatorController, or ElevatorRequest directly.
 */
public class ElevatorSystem {

    private final int minFloor;
    private final int maxFloor;
    private final ElevatorController controller;
    private final List<Floor> floors;

    public ElevatorSystem(int minFloor, int maxFloor, ElevatorController controller) {
        if (minFloor >= maxFloor) {
            throw new IllegalArgumentException("minFloor must be less than maxFloor.");
        }
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.controller = controller;
        this.floors = buildFloors(minFloor, maxFloor);
    }

    // -------------------------------------------------------------------------
    // External requests — from floor panels
    // -------------------------------------------------------------------------

    /**
     * A person on a floor presses the UP or DOWN button.
     */
    public void requestElevator(int floor, Direction direction) {
        validateFloor(floor);
        Floor f = floors.get(floor - minFloor);
        f.pressButton(direction);

        ElevatorRequest request = ElevatorRequest
            .builder(floor, RequestType.EXTERNAL)
            .direction(direction)
            .build();

        controller.dispatch(request);
    }

    // -------------------------------------------------------------------------
    // Internal requests — from inside the elevator cab
    // -------------------------------------------------------------------------

    /**
     * A passenger inside an elevator presses a destination floor button.
     *
     * @param elevatorId    the elevator they are in
     * @param sourceFloor   their current floor (boarding floor)
     * @param destFloor     the floor they want to go to
     */
    public void selectDestination(int elevatorId, int sourceFloor, int destFloor) {
        validateFloor(sourceFloor);
        validateFloor(destFloor);

        ElevatorRequest request = ElevatorRequest
            .builder(sourceFloor, RequestType.INTERNAL)
            .destinationFloor(destFloor)
            .direction(destFloor > sourceFloor ? Direction.UP : Direction.DOWN)
            .build();

        // Internal request goes directly to the specific elevator
        controller.getElevators().stream()
            .filter(e -> e.getElevatorId() == elevatorId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No elevator with id " + elevatorId))
            .addRequest(request);

        System.out.printf("[System] Internal request: Elevator %d → floor %d%n", elevatorId, destFloor);
    }

    // -------------------------------------------------------------------------
    // Emergency
    // -------------------------------------------------------------------------

    /**
     * Emergency override — dispatched to nearest available elevator immediately.
     */
    public void triggerEmergency(int floor) {
        validateFloor(floor);

        ElevatorRequest request = ElevatorRequest
            .builder(floor, RequestType.EMERGENCY)
            .destinationFloor(floor)
            .build();

        controller.dispatch(request);
        System.out.printf("[System] EMERGENCY triggered on floor %d!%n", floor);
    }

    // -------------------------------------------------------------------------
    // Maintenance
    // -------------------------------------------------------------------------

    public void setMaintenanceMode(int elevatorId) {
        findElevator(elevatorId).setMaintenance();
    }

    public void restoreFromMaintenance(int elevatorId) {
        findElevator(elevatorId).restoreFromMaintenance();
    }

    // -------------------------------------------------------------------------
    // Simulation controls
    // -------------------------------------------------------------------------

    public void tick() {
        controller.tick();
    }

    public void runUntilIdle() {
        controller.runUntilIdle();
    }

    public void printStatus() {
        controller.printStatus();
    }

    public void addObserver(ElevatorObserver observer) {
        controller.addObserver(observer);
    }

    public List<Floor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void validateFloor(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            throw new InvalidFloorException(
                String.format("Floor %d is outside building range [%d, %d].", floor, minFloor, maxFloor));
        }
    }

    private Elevator findElevator(int id) {
        return controller.getElevators().stream()
            .filter(e -> e.getElevatorId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No elevator with id: " + id));
    }

    private List<Floor> buildFloors(int min, int max) {
        List<Floor> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(new Floor(i, min, max));
        }
        return list;
    }
}
