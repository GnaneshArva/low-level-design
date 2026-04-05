package com.elevator.facade;

import com.elevator.core.Elevator;
import com.elevator.core.ElevatorController;
import com.elevator.factory.ElevatorFactory;
import com.elevator.strategy.NearestElevatorStrategy;
import com.elevator.strategy.RequestScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for ElevatorSystem.
 * Separates object construction from representation — clean entry point for Main.
 */
public class ElevatorSystemBuilder {

    private int minFloor = 1;
    private int maxFloor = 10;
    private int elevatorCount = 3;
    private RequestScheduler scheduler = new NearestElevatorStrategy();
    private final List<Integer> startFloors = new ArrayList<>();

    public ElevatorSystemBuilder floors(int min, int max) {
        this.minFloor = min;
        this.maxFloor = max;
        return this;
    }

    public ElevatorSystemBuilder elevators(int count) {
        this.elevatorCount = count;
        return this;
    }

    public ElevatorSystemBuilder withScheduler(RequestScheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public ElevatorSystemBuilder withStartFloors(int... floors) {
        for (int f : floors) startFloors.add(f);
        return this;
    }

    public ElevatorSystem build() {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            int start = (i < startFloors.size()) ? startFloors.get(i) : minFloor;
            elevators.add(ElevatorFactory.createAtFloor(i + 1, minFloor, maxFloor, start));
        }
        ElevatorController controller = new ElevatorController(elevators, scheduler);
        return new ElevatorSystem(minFloor, maxFloor, controller);
    }
}
