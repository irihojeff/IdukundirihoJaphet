// Abstract base class for all vehicles
abstract class Vehicle {
    // Private fields for encapsulation
    private String vehicleId;
    private String ownerName;
    private int yearOfFabrication;
    private String registrationNumber;
    private double baseTaxRate;
    private String vehicleType;
    
    // Constructor
    public Vehicle(String vehicleId, String ownerName, int yearOfFabrication, 
                  String registrationNumber, double baseTaxRate, String vehicleType) {
        setVehicleId(vehicleId);
        setOwnerName(ownerName);
        setYearOfFabrication(yearOfFabrication);
        setRegistrationNumber(registrationNumber);
        setBaseTaxRate(baseTaxRate);
        setVehicleType(vehicleType);
    }
    
    // Abstract methods that all child classes must implement
    public abstract double calculateTax();
    public abstract void generateTaxReport();
    
    // Method to validate year of fabrication
    private void validateYearOfFabrication(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year > currentYear) {
            throw new IllegalArgumentException("Year of fabrication cannot be in the future");
        }
        if (year < 1900) {
            throw new IllegalArgumentException("Year of fabrication must be after 1900");
        }
    }
    
    // Getters and Setters with validation
    public String getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(String vehicleId) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }
        this.vehicleId = vehicleId;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }
        this.ownerName = ownerName;
    }
    
    public int getYearOfFabrication() {
        return yearOfFabrication;
    }
    
    public void setYearOfFabrication(int yearOfFabrication) {
        validateYearOfFabrication(yearOfFabrication);
        this.yearOfFabrication = yearOfFabrication;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        this.registrationNumber = registrationNumber;
    }
    
    public double getBaseTaxRate() {
        return baseTaxRate;
    }
    
    public void setBaseTaxRate(double baseTaxRate) {
        if (baseTaxRate < 0) {
            throw new IllegalArgumentException("Base tax rate cannot be negative");
        }
        this.baseTaxRate = baseTaxRate;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        if (vehicleType == null || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be empty");
        }
        this.vehicleType = vehicleType;
    }
    
    // Calculate vehicle age
    protected int getVehicleAge() {
        int currentYear = java.time.Year.now().getValue();
        return currentYear - yearOfFabrication;
    }
    
    // Common toString method to display basic vehicle info
    @Override 
    public String toString() {
        return String.format("Vehicle ID: %s\nOwner: %s\nType: %s\nYear: %d\nRegistration: %s\nBase Tax Rate: $%.2f", 
                              vehicleId, ownerName, vehicleType, yearOfFabrication, registrationNumber, baseTaxRate);
    }
}

// Concrete implementation for Car
class Car extends Vehicle {
    private boolean isElectric;
    
    public Car(String vehicleId, String ownerName, int yearOfFabrication, 
              String registrationNumber, double baseTaxRate, boolean isElectric) {
        super(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, "Car");
        this.isElectric = isElectric;
    }
    
    public boolean isElectric() {
        return isElectric;
    }
    
    public void setElectric(boolean electric) {
        isElectric = electric;
    }
    
    @Override
    public double calculateTax() {
        double tax = getBaseTaxRate();
        
        // Apply 20% discount for electric cars
        if (isElectric) {
            tax *= 0.8; // 20% discount
        }
        
        // Reduce base tax by 10% if older than 10 years
        if (getVehicleAge() > 10) {
            tax *= 0.9; // 10% reduction
        }
        
        return tax;
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("=== TAX REPORT: CAR ===");
        System.out.println(toString());
        System.out.println("Electric: " + (isElectric ? "Yes" : "No"));
        System.out.println("Vehicle Age: " + getVehicleAge() + " years");
        System.out.println("Applied Tax Rules:");
        
        if (isElectric) {
            System.out.println("- Electric vehicle discount: 20%");
        }
        
        if (getVehicleAge() > 10) {
            System.out.println("- Vehicle age reduction: 10%");
        }
        
        System.out.printf("Total Annual Tax: $%.2f\n", calculateTax());
        System.out.println("======================");
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nElectric: " + (isElectric ? "Yes" : "No");
    }
}

// Concrete implementation for Truck
class Truck extends Vehicle {
    private double loadCapacity; // in tons
    
    public Truck(String vehicleId, String ownerName, int yearOfFabrication, 
                String registrationNumber, double baseTaxRate, double loadCapacity) {
        super(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, "Truck");
        setLoadCapacity(loadCapacity);
    }
    
    public double getLoadCapacity() {
        return loadCapacity;
    }
    
    public void setLoadCapacity(double loadCapacity) {
        if (loadCapacity <= 0) {
            throw new IllegalArgumentException("Load capacity must be greater than 0");
        }
        this.loadCapacity = loadCapacity;
    }
    
    @Override
    public double calculateTax() {
        double tax = getBaseTaxRate();
        
        // Trucks older than 15 years → 15% extra tax
        if (getVehicleAge() > 15) {
            tax *= 1.15; // 15% increase
        }
        
        // Load capacity > 10 tons → increase tax by 25%
        if (loadCapacity > 10) {
            tax *= 1.25; // 25% increase
        }
        
        return tax;
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("=== TAX REPORT: TRUCK ===");
        System.out.println(toString());
        System.out.println("Vehicle Age: " + getVehicleAge() + " years");
        System.out.println("Applied Tax Rules:");
        
        if (getVehicleAge() > 15) {
            System.out.println("- Age surcharge: 15%");
        }
        
        if (loadCapacity > 10) {
            System.out.println("- Heavy load capacity surcharge: 25%");
        }
        
        System.out.printf("Total Annual Tax: $%.2f\n", calculateTax());
        System.out.println("======================");
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format("\nLoad Capacity: %.2f tons", loadCapacity);
    }
}

// Concrete implementation for Motorcycle
class Motorcycle extends Vehicle {
    private int engineCapacity; // in cc
    
    public Motorcycle(String vehicleId, String ownerName, int yearOfFabrication, 
                     String registrationNumber, double baseTaxRate, int engineCapacity) {
        super(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, "Motorcycle");
        setEngineCapacity(engineCapacity);
    }
    
    public int getEngineCapacity() {
        return engineCapacity;
    }
    
    public void setEngineCapacity(int engineCapacity) {
        if (engineCapacity <= 0) {
            throw new IllegalArgumentException("Engine capacity must be greater than 0");
        }
        this.engineCapacity = engineCapacity;
    }
    
    @Override
    public double calculateTax() {
        double tax = getBaseTaxRate();
        
        // Engine capacity > 500cc → 20% extra tax
        if (engineCapacity > 500) {
            tax *= 1.2; // 20% increase
        }
        
        // Age-based depreciation: 5% reduction every 5 years
        int ageFactor = getVehicleAge() / 5;
        double ageDiscount = 0.05 * ageFactor;
        
        // Apply age-based reduction (cap at maximum 25% reduction for very old motorcycles)
        tax *= (1 - Math.min(ageDiscount, 0.25));
        
        return tax;
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("=== TAX REPORT: MOTORCYCLE ===");
        System.out.println(toString());
        System.out.println("Vehicle Age: " + getVehicleAge() + " years");
        System.out.println("Applied Tax Rules:");
        
        if (engineCapacity > 500) {
            System.out.println("- High engine capacity surcharge: 20%");
        }
        
        int ageFactor = getVehicleAge() / 5;
        if (ageFactor > 0) {
            System.out.println("- Age-based depreciation: " + (ageFactor * 5) + "% reduction");
        }
        
        System.out.printf("Total Annual Tax: $%.2f\n", calculateTax());
        System.out.println("======================");
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nEngine Capacity: " + engineCapacity + " cc";
    }
}

// Concrete implementation for Bus
class Bus extends Vehicle {
    private int passengerCapacity;
    
    public Bus(String vehicleId, String ownerName, int yearOfFabrication, 
              String registrationNumber, double baseTaxRate, int passengerCapacity) {
        super(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, "Bus");
        setPassengerCapacity(passengerCapacity);
    }
    
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    
    public void setPassengerCapacity(int passengerCapacity) {
        if (passengerCapacity <= 0) {
            throw new IllegalArgumentException("Passenger capacity must be greater than 0");
        }
        this.passengerCapacity = passengerCapacity;
    }
    
    @Override
    public double calculateTax() {
        double tax = getBaseTaxRate();
        
        // Tax increases by 2% per every 10 passengers
        double passengerFactor = passengerCapacity / 10.0;
        tax *= (1 + (0.02 * passengerFactor));
        
        // Age > 20 years → increase tax by 10%
        if (getVehicleAge() > 20) {
            tax *= 1.1; // 10% increase
        }
        
        return tax;
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("=== TAX REPORT: BUS ===");
        System.out.println(toString());
        System.out.println("Vehicle Age: " + getVehicleAge() + " years");
        System.out.println("Applied Tax Rules:");
        
        double passengerFactor = passengerCapacity / 10.0;
        System.out.printf("- Passenger capacity increase: %.1f%%\n", (passengerFactor * 2));
        
        if (getVehicleAge() > 20) {
            System.out.println("- Age surcharge: 10%");
        }
        
        System.out.printf("Total Annual Tax: $%.2f\n", calculateTax());
        System.out.println("======================");
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nPassenger Capacity: " + passengerCapacity;
    }
}

// Concrete implementation for SUV
class SUV extends Vehicle {
    private boolean fourWheelDrive;
    
    public SUV(String vehicleId, String ownerName, int yearOfFabrication, 
              String registrationNumber, double baseTaxRate, boolean fourWheelDrive) {
        super(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, "SUV");
        this.fourWheelDrive = fourWheelDrive;
    }
    
    public boolean isFourWheelDrive() {
        return fourWheelDrive;
    }
    
    public void setFourWheelDrive(boolean fourWheelDrive) {
        this.fourWheelDrive = fourWheelDrive;
    }
    
    @Override
    public double calculateTax() {
        double tax = getBaseTaxRate();
        
        // If 4WD → increase tax by 10%
        if (fourWheelDrive) {
            tax *= 1.1; // 10% increase
        }
        
        // Age > 10 years → decrease tax by 5%
        if (getVehicleAge() > 10) {
            tax *= 0.95; // 5% decrease
        }
        
        return tax;
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("=== TAX REPORT: SUV ===");
        System.out.println(toString());
        System.out.println("Vehicle Age: " + getVehicleAge() + " years");
        System.out.println("Applied Tax Rules:");
        
        if (fourWheelDrive) {
            System.out.println("- Four wheel drive surcharge: 10%");
        }
        
        if (getVehicleAge() > 10) {
            System.out.println("- Age-based reduction: 5%");
        }
        
        System.out.printf("Total Annual Tax: $%.2f\n", calculateTax());
        System.out.println("======================");
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nFour Wheel Drive: " + (fourWheelDrive ? "Yes" : "No");
    }
}

// Main class for user interaction
public class VehicleTaxManagementSystem {
    private static final java.util.ArrayList<Vehicle> vehicles = new java.util.ArrayList<>();
    private static final java.util.Scanner scanner = new java.util.Scanner(System.in);
    
    public static void main(String[] args) {
        boolean exit = false;
        
        System.out.println("Welcome to Vehicle Tax Management System");
        
        while (!exit) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        registerNewVehicle();
                        break;
                    case 2:
                        viewRegisteredVehicles();
                        break;
                    case 3:
                        calculateTaxForAllVehicles();
                        break;
                    case 4:
                        generateTaxReports();
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Thank you for using Vehicle Tax Management System.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n===== VEHICLE TAX MANAGEMENT SYSTEM =====");
        System.out.println("1. Register a new vehicle");
        System.out.println("2. View registered vehicles");
        System.out.println("3. Calculate tax for all vehicles");
        System.out.println("4. Generate tax reports");
        System.out.println("5. Exit");
        System.out.println("=========================================");
    }
    
    private static void registerNewVehicle() {
        System.out.println("\n----- VEHICLE REGISTRATION -----");
        System.out.println("Select vehicle type:");
        System.out.println("1. Car");
        System.out.println("2. Truck");
        System.out.println("3. Motorcycle");
        System.out.println("4. Bus");
        System.out.println("5. SUV");
        
        int vehicleTypeChoice = 0;
        boolean validChoice = false;
        
        while (!validChoice) {
            try {
                vehicleTypeChoice = getIntInput("Enter your choice: ");
                if (vehicleTypeChoice >= 1 && vehicleTypeChoice <= 5) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
        
        // Get common attributes with individual validation for each field
        String vehicleId = getUniqueVehicleId();
        String ownerName = getValidInput("Enter owner name: ", VehicleTaxManagementSystem::validateOwnerName);
        int yearOfFabrication = getValidYear();
        String registrationNumber = getUniqueRegistrationNumber();
        double baseTaxRate = getValidBaseTaxRate();
        
        // Now handle type-specific attributes
        try {
            Vehicle vehicle = null;
            
            switch (vehicleTypeChoice) {
                case 1: // Car
                    boolean isElectric = getValidBooleanInput("Is the car electric (true/false): ");
                    vehicle = new Car(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, isElectric);
                    break;
                case 2: // Truck
                    double loadCapacity = getValidPositiveDouble("Enter load capacity (in tons): ", "Load capacity must be greater than 0");
                    vehicle = new Truck(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, loadCapacity);
                    break;
                case 3: // Motorcycle
                    int engineCapacity = getValidPositiveInt("Enter engine capacity (in cc): ", "Engine capacity must be greater than 0");
                    vehicle = new Motorcycle(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, engineCapacity);
                    break;
                case 4: // Bus
                    int passengerCapacity = getValidPositiveInt("Enter passenger capacity: ", "Passenger capacity must be greater than 0");
                    vehicle = new Bus(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, passengerCapacity);
                    break;
                case 5: // SUV
                    boolean fourWheelDrive = getValidBooleanInput("Is the SUV four-wheel drive (true/false): ");
                    vehicle = new SUV(vehicleId, ownerName, yearOfFabrication, registrationNumber, baseTaxRate, fourWheelDrive);
                    break;
            }
            
            vehicles.add(vehicle);
            System.out.println("Vehicle registered successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
    
    private static void viewRegisteredVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles registered yet.");
            return;
        }
        
        System.out.println("\n----- REGISTERED VEHICLES -----");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println("\nVehicle #" + (i + 1) + ":");
            System.out.println(vehicles.get(i));
            System.out.println("-----------------------------");
        }
    }
    
    private static void calculateTaxForAllVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles registered yet.");
            return;
        }
        
        System.out.println("\n----- TAX CALCULATION -----");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            System.out.printf("Vehicle #%d (%s - %s): $%.2f\n", 
                             (i + 1), vehicle.getVehicleType(), vehicle.getRegistrationNumber(), vehicle.calculateTax());
        }
    }
    
    private static void generateTaxReports() {
        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles registered yet.");
            return;
        }
        
        System.out.println("\n----- VEHICLE TAX REPORTS -----");
        for (Vehicle vehicle : vehicles) {
            vehicle.generateTaxReport();
            System.out.println();
        }
    }
    
    // Improved input validation methods that only re-prompt for specific inputs
    
    private static String getUniqueVehicleId() {
        while (true) {
            try {
                String vehicleId = getStringInput("Enter vehicle ID: ");
                
                // Check for duplicate IDs
                for (Vehicle v : vehicles) {
                    if (v.getVehicleId().equalsIgnoreCase(vehicleId)) {
                        throw new IllegalArgumentException("Vehicle ID already exists. Please enter a unique ID.");
                    }
                }
                
                return vehicleId;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String getUniqueRegistrationNumber() {
        while (true) {
            try {
                String registrationNumber = getStringInput("Enter registration number: ");
                
                // Check for duplicate registration numbers
                for (Vehicle v : vehicles) {
                    if (v.getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
                        throw new IllegalArgumentException("Registration number already exists. Please enter a unique number.");
                    }
                }
                
                return registrationNumber;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String getStringInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter a valid integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("yes") || input.equals("y") || input.equals("1")) {
                return true;
            } else if (input.equals("false") || input.equals("no") || input.equals("n") || input.equals("0")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }
    }
    
    // New improved validation methods
    
    private static String validateOwnerName(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }
        return input;
    }
    
    private static int getValidYear() {
        while (true) {
            try {
                int year = getIntInput("Enter year of fabrication: ");
                int currentYear = java.time.Year.now().getValue();
                
                if (year > currentYear) {
                    throw new IllegalArgumentException("Year of fabrication cannot be in the future");
                }
                if (year < 1900) {
                    throw new IllegalArgumentException("Year of fabrication must be after 1900");
                }
                
                return year;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static double getValidBaseTaxRate() {
        while (true) {
            try {
                double rate = getDoubleInput("Enter base tax rate: $");
                if (rate < 0) {
                    throw new IllegalArgumentException("Base tax rate cannot be negative");
                }
                return rate;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static boolean getValidBooleanInput(String prompt) {
        while (true) {
            try {
                return getBooleanInput(prompt);
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please try again.");
            }
        }
    }
    
    private static double getValidPositiveDouble(String prompt, String errorMessage) {
        while (true) {
            try {
                double value = getDoubleInput(prompt);
                if (value <= 0) {
                    throw new IllegalArgumentException(errorMessage);
                }
                return value;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static int getValidPositiveInt(String prompt, String errorMessage) {
        while (true) {
            try {
                int value = getIntInput(prompt);
                if (value <= 0) {
                    throw new IllegalArgumentException(errorMessage);
                }
                return value;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    // Generic input validation method with custom validator
    private static <T> T getValidInput(String prompt, java.util.function.Function<String, T> validator) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return validator.apply(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}