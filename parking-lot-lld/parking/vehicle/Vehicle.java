package parking.vehicle;

// Abstraction: Vehicle defines the contract; subtypes extend without modifying this class (OCP)
public abstract class Vehicle {

    private final String vehicleNumber;
    private final VehicleType vehicleType;

    protected Vehicle(final String vehicleNumber, final VehicleType vehicleType) {
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number cannot be blank");
        }
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() { return vehicleNumber; }
    public VehicleType getVehicleType() { return vehicleType; }

    @Override
    public String toString() {
        return vehicleType + "[" + vehicleNumber + "]";
    }
}
