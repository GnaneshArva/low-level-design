package parking.vehicle;

// Factory Pattern: centralizes parking.vehicle creation; new types added here without touching callers (OCP)
public final class VehicleFactory {

    private VehicleFactory() {}

    public static Vehicle create(final VehicleType type, final String vehicleNumber) {
        return switch (type) {
            case BIKE  -> new Bike(vehicleNumber);
            case CAR   -> new Car(vehicleNumber);
            case TRUCK -> new Truck(vehicleNumber);
        };
    }
}
