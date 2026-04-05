package com.elevator.core;

import com.elevator.exception.NoElevatorAvailableException;
import com.elevator.request.ElevatorRequest;
import com.elevator.strategy.RequestScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ElevatorController: SRP — responsible only for request dispatch.
 *
 * It delegates:
 *  - "which elevator?" → RequestScheduler (strategy)
 *  - "how to move?" → Elevator / ElevatorState
 *  - "who to notify?" → ElevatorObserver list
 *
 * DIP: depends on RequestScheduler abstraction, not a concrete algorithm.
 */
public class ElevatorController {

    private final List<Elevator> elevators;
    private final RequestScheduler scheduler;
    private final List<ElevatorObserver> observers = new ArrayList<>();

    public ElevatorController(List<Elevator> elevators, RequestScheduler scheduler) {
        this.elevators = new ArrayList<>(elevators);
        this.scheduler = scheduler;
    }

    /**
     * Assigns an incoming request to the best elevator.
     */
    public void dispatch(ElevatorRequest request) {
        List<Elevator> available = elevators.stream()
            .filter(Elevator::isAvailable)
            .toList();

        Optional<Elevator> chosen = scheduler.selectElevator(available, request);

        if (chosen.isEmpty()) {
            throw new NoElevatorAvailableException(
                "No elevator available to handle request: " + request);
        }

        Elevator elevator = chosen.get();
        elevator.addRequest(request);

        System.out.printf("[Controller] Dispatched %s → Elevator %d%n",
            request, elevator.getElevatorId());

        notifyAssigned(elevator.getElevatorId(), request);
        notifyStatusChanged(elevator);
    }

    /**
     * Advances all elevators by one simulation tick.
     */
    public void tick() {
        for (Elevator elevator : elevators) {
            elevator.processNextStep();
        }
    }

    /**
     * Runs all elevators until they are all idle (no more requests).
     */
    public void runUntilIdle() {
        int maxTicks = 1000; // safety guard against infinite loops
        int ticks = 0;
        while (elevators.stream().anyMatch(e -> !e.isIdle()) && ticks++ < maxTicks) {
            tick();
        }
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public List<Elevator> getElevators() {
        return Collections.unmodifiableList(elevators);
    }

    public void printStatus() {
        System.out.println("\n===== Elevator System Status =====");
        for (Elevator e : elevators) {
            System.out.println("  " + e);
        }
        System.out.println("==================================\n");
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void notifyAssigned(int elevatorId, ElevatorRequest request) {
        for (ElevatorObserver o : observers) {
            o.onRequestAssigned(elevatorId, request);
        }
    }

    private void notifyStatusChanged(Elevator elevator) {
        for (ElevatorObserver o : observers) {
            o.onElevatorStatusChanged(elevator);
        }
    }
}
