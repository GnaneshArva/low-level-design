package com.elevator.core;

import com.elevator.exception.ElevatorNotAvailableException;
import com.elevator.exception.InvalidFloorException;
import com.elevator.request.ElevatorRequest;
import com.elevator.request.RequestType;
import com.elevator.state.*;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayDeque;

/**
 * Elevator encapsulates all per-elevator logic.
 *
 * Design decisions:
 * - Implements ElevatorStateContext so states can manipulate the elevator
 *   without exposing the full Elevator API (ISP).
 * - Uses a priority queue: emergency requests jump to the front;
 *   remaining requests are served in SCAN (look) order.
 * - Direction is derived from movement, not stored redundantly.
 */
public class Elevator implements ElevatorStateContext {

    private final int id;
    private final int minFloor;
    private final int maxFloor;

    private int currentFloor;
    private ElevatorState state;

    // Emergency requests are drained first (FIFO among themselves).
    private final Queue<ElevatorRequest> emergencyQueue = new ArrayDeque<>();

    // Normal requests sorted by effective target floor for SCAN-like traversal.
    private final PriorityQueue<ElevatorRequest> normalQueue =
        new PriorityQueue<>(Comparator.comparingInt(ElevatorRequest::getEffectiveTargetFloor));

    public Elevator(int id, int minFloor, int maxFloor, int startFloor) {
        if (startFloor < minFloor || startFloor > maxFloor) {
            throw new InvalidFloorException("Start floor out of building range.");
        }
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = startFloor;
        this.state = new IdleState();
    }

    // -------------------------------------------------------------------------
    // ElevatorStateContext contract
    // -------------------------------------------------------------------------

    @Override
    public void setState(ElevatorState newState) {
        System.out.printf("  [Elevator %d] State: %s → %s%n",
            id, this.state.getStateName(), newState.getStateName());
        this.state = newState;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    @Override
    public int getElevatorId() {
        return id;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Enqueues a request. Maintenance elevators reject all requests.
     */
    public void addRequest(ElevatorRequest request) {
        validateFloor(request.getEffectiveTargetFloor());

        if (state instanceof MaintenanceState) {
            throw new ElevatorNotAvailableException(
                "Elevator " + id + " is in maintenance mode.");
        }

        if (request.isEmergency()) {
            emergencyQueue.add(request);
        } else {
            normalQueue.add(request);
        }

        // Transition idle elevator toward the new request
        state.handleRequest(request, this);
    }

    /**
     * Processes the next pending request, moving the elevator one step.
     * Returns true if there was work to do.
     */
    public boolean processNextStep() {
        if (state instanceof MaintenanceState) {
            return false;
        }

        ElevatorRequest next = peekNextRequest();
        if (next == null) {
            state.stop(this); // transition to idle
            return false;
        }

        int target = next.getEffectiveTargetFloor();

        if (currentFloor == target) {
            // Arrived — dequeue and serve
            pollNextRequest();
            System.out.printf("  [Elevator %d] Arrived at floor %d ✓ (%s)%n",
                id, currentFloor, next.getRequestType());

            // If an internal request, the destination is the same as source: mark served.
            // If an external request, the passenger boards here; their internal request
            // will be added separately by the simulation.

            if (isIdle()) {
                state.stop(this);
            }
            return true;
        }

        if (target > currentFloor) {
            state.moveUp(this);
            System.out.printf("  [Elevator %d] Moving UP → floor %d (target: %d)%n",
                id, currentFloor, target);
        } else {
            state.moveDown(this);
            System.out.printf("  [Elevator %d] Moving DOWN → floor %d (target: %d)%n",
                id, currentFloor, target);
        }

        validateBounds();
        return true;
    }

    public void setMaintenance() {
        this.state = new MaintenanceState();
        System.out.printf("  [Elevator %d] Set to MAINTENANCE mode.%n", id);
    }

    public void restoreFromMaintenance() {
        this.state = new IdleState();
        System.out.printf("  [Elevator %d] Restored from maintenance.%n", id);
    }

    public boolean isAvailable() {
        return !(state instanceof MaintenanceState);
    }

    public boolean isIdle() {
        return state instanceof IdleState && emergencyQueue.isEmpty() && normalQueue.isEmpty();
    }

    public int getPendingRequestCount() {
        return emergencyQueue.size() + normalQueue.size();
    }

    public String getStateName() {
        return state.getStateName();
    }

    public Direction getDirection() {
        if (state instanceof MovingUpState)   return Direction.UP;
        if (state instanceof MovingDownState) return Direction.DOWN;
        return Direction.IDLE;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private ElevatorRequest peekNextRequest() {
        if (!emergencyQueue.isEmpty()) return emergencyQueue.peek();
        return normalQueue.peek();
    }

    private ElevatorRequest pollNextRequest() {
        if (!emergencyQueue.isEmpty()) return emergencyQueue.poll();
        return normalQueue.poll();
    }

    private void validateFloor(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            throw new InvalidFloorException(
                String.format("Floor %d is out of range [%d, %d].", floor, minFloor, maxFloor));
        }
    }

    private void validateBounds() {
        if (currentFloor < minFloor || currentFloor > maxFloor) {
            throw new InvalidFloorException(
                "Elevator " + id + " moved out of building bounds: floor " + currentFloor);
        }
    }

    @Override
    public String toString() {
        return String.format(
            "Elevator{id=%d, floor=%d, state=%s, direction=%s, pendingRequests=%d}",
            id, currentFloor, getStateName(), getDirection(), getPendingRequestCount());
    }
}
