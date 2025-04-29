import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

// Interface definitions
interface TaxCalculable {
    double calculateTax();
}

interface Receiptable {
    void generateReceipt();
}

// Abstract base class for all tax declarations
abstract class TaxDeclaration implements TaxCalculable, Receiptable {
    // Private fields for encapsulation
    private String declarationId;
    private String taxpayerName;
    private String taxpayerTIN;
    private LocalDate declarationDate;
    private double taxAmount;
    private boolean isPaid;
    
    // Constructor
    public TaxDeclaration(String declarationId, String taxpayerName, 
                         String taxpayerTIN, LocalDate declarationDate, 
                         double taxAmount, boolean isPaid) {
        setDeclarationId(declarationId);
        setTaxpayerName(taxpayerName);
        setTaxpayerTIN(taxpayerTIN);
        setDeclarationDate(declarationDate);
        setTaxAmount(taxAmount);
        setPaid(isPaid);
    }
    
    // Abstract methods that all child classes must implement
    public abstract double calculateTax();
    public abstract boolean validateDeclaration();
    public abstract void generateReceipt();
    public abstract double enforceCompliance();
    
    // Validation for declaration date
    private void validateDeclarationDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Declaration date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Declaration date cannot be in the future");
        }
    }
    
    // Validation for TIN
    private void validateTIN(String tin) {
        if (tin == null || tin.trim().isEmpty()) {
            throw new IllegalArgumentException("TIN cannot be empty");
        }
        if (tin.length() != 9) {
            throw new IllegalArgumentException("TIN must be exactly 9 digits");
        }
        if (!tin.matches("\\d{9}")) {
            throw new IllegalArgumentException("TIN must contain only digits");
        }
    }
    
    // Getters and Setters with validation
    public String getDeclarationId() {
        return declarationId;
    }
    
    public void setDeclarationId(String declarationId) {
        if (declarationId == null || declarationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Declaration ID cannot be empty");
        }
        this.declarationId = declarationId;
    }
    
    public String getTaxpayerName() {
        return taxpayerName;
    }
    
    public void setTaxpayerName(String taxpayerName) {
        if (taxpayerName == null || taxpayerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Taxpayer name cannot be empty");
        }
        this.taxpayerName = taxpayerName;
    }
    
    public String getTaxpayerTIN() {
        return taxpayerTIN;
    }
    
    public void setTaxpayerTIN(String taxpayerTIN) {
        validateTIN(taxpayerTIN);
        this.taxpayerTIN = taxpayerTIN;
    }
    
    public LocalDate getDeclarationDate() {
        return declarationDate;
    }
    
    public void setDeclarationDate(LocalDate declarationDate) {
        validateDeclarationDate(declarationDate);
        this.declarationDate = declarationDate;
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(double taxAmount) {
        if (taxAmount < 0) {
            throw new IllegalArgumentException("Tax amount cannot be negative");
        }
        this.taxAmount = taxAmount;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean paid) {
        this.isPaid = paid;
    }
    
    // Common toString method to display basic declaration info
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Declaration ID: %s\nTaxpayer: %s\nTIN: %s\nDate: %s\nAmount: RWF %.2f\nPaid: %s", 
                              declarationId, taxpayerName, taxpayerTIN, 
                              declarationDate.format(formatter), taxAmount, 
                              isPaid ? "Yes" : "No");
    }
}

// Class for PAYE Tax Declaration
class PAYEDeclaration extends TaxDeclaration {
    private double grossSalary;
    private int numDependents;
    
    public PAYEDeclaration(String declarationId, String taxpayerName, 
                          String taxpayerTIN, LocalDate declarationDate, 
                          double taxAmount, boolean isPaid, 
                          double grossSalary, int numDependents) {
        super(declarationId, taxpayerName, taxpayerTIN, declarationDate, taxAmount, isPaid);
        setGrossSalary(grossSalary);
        setNumDependents(numDependents);
    }
    
    public double getGrossSalary() {
        return grossSalary;
    }
    
    public void setGrossSalary(double grossSalary) {
        if (grossSalary <= 0) {
            throw new IllegalArgumentException("Gross salary must be greater than 0");
        }
        this.grossSalary = grossSalary;
    }
    
    public int getNumDependents() {
        return numDependents;
    }
    
    public void setNumDependents(int numDependents) {
        if (numDependents < 0) {
            throw new IllegalArgumentException("Number of dependents cannot be negative");
        }
        this.numDependents = numDependents;
    }
    
    @Override
    public double calculateTax() {
        // Progressive tax bracket calculation
        double taxableIncome = grossSalary - (numDependents * 10000); // Deduction per dependent
        
        if (taxableIncome <= 30000) {
            return taxableIncome * 0.0; // 0% for first 30,000
        } else if (taxableIncome <= 100000) {
            return (taxableIncome - 30000) * 0.2; // 20% for 30,001-100,000
        } else {
            return (70000 * 0.2) + ((taxableIncome - 100000) * 0.3); // 30% for above 100,000
        }
    }
    
    @Override
    public boolean validateDeclaration() {
        return grossSalary > 0;
    }
    
    @Override
    public void generateReceipt() {
        System.out.println("\n======= PAYE TAX RECEIPT =======");
        System.out.println(toString());
        System.out.println("Gross Salary: RWF " + String.format("%.2f", grossSalary));
        System.out.println("Number of Dependents: " + numDependents);
        System.out.println("Dependent Deductions: RWF " + String.format("%.2f", numDependents * 10000.0));
        System.out.println("Tax Due: RWF " + String.format("%.2f", getTaxAmount()));
        System.out.println("Compliance Status: " + (isPaid() ? "Paid" : "Unpaid"));
        
        if (!isPaid()) {
            double penalty = enforceCompliance();
            if (penalty > 0) {
                System.out.println("Late Payment Penalty: RWF " + String.format("%.2f", penalty));
                System.out.println("Total Amount Due: RWF " + String.format("%.2f", getTaxAmount() + penalty));
            }
        }
        System.out.println("===============================");
    }
    
    @Override
    public double enforceCompliance() {
        if (!isPaid()) {
            // Check if payment is overdue (after 15th of following month)
            LocalDate dueDate = getDeclarationDate().plusMonths(1).withDayOfMonth(15);
            
            if (LocalDate.now().isAfter(dueDate)) {
                long daysLate = Period.between(dueDate, LocalDate.now()).getDays();
                
                // 2% penalty plus 0.1% per day late
                double penaltyPercent = 2.0 + (daysLate * 0.1);
                // Cap at 20%
                penaltyPercent = Math.min(penaltyPercent, 20.0);
                
                return getTaxAmount() * (penaltyPercent / 100.0);
            }
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nType: PAYE";
    }
}

// Class for VAT Declaration
class VATDeclaration extends TaxDeclaration {
    private static final double VAT_RATE = 0.18; // 18% VAT in Rwanda
    
    private double taxableSales;
    private double taxablePurchases;
    
    public VATDeclaration(String declarationId, String taxpayerName, 
                         String taxpayerTIN, LocalDate declarationDate, 
                         double taxAmount, boolean isPaid, 
                         double taxableSales, double taxablePurchases) {
        super(declarationId, taxpayerName, taxpayerTIN, declarationDate, taxAmount, isPaid);
        setTaxableSales(taxableSales);
        setTaxablePurchases(taxablePurchases);
    }
    
    public double getTaxableSales() {
        return taxableSales;
    }
    
    public void setTaxableSales(double taxableSales) {
        if (taxableSales < 0) {
            throw new IllegalArgumentException("Taxable sales cannot be negative");
        }
        this.taxableSales = taxableSales;
    }
    
    public double getTaxablePurchases() {
        return taxablePurchases;
    }
    
    public void setTaxablePurchases(double taxablePurchases) {
        if (taxablePurchases < 0) {
            throw new IllegalArgumentException("Taxable purchases cannot be negative");
        }
        this.taxablePurchases = taxablePurchases;
    }
    
    @Override
    public double calculateTax() {
        // VAT Output (on sales) minus VAT Input (on purchases)
        double outputVAT = taxableSales * VAT_RATE;
        double inputVAT = taxablePurchases * VAT_RATE;
        
        return Math.max(0, outputVAT - inputVAT);
    }
    
    @Override
    public boolean validateDeclaration() {
        // Basic validation: ensure taxable sales is not suspiciously lower than purchases
        return taxableSales >= 0 && taxablePurchases >= 0;
    }
    
    @Override
    public void generateReceipt() {
        System.out.println("\n======= VAT RECEIPT =======");
        System.out.println(toString());
        System.out.println("Taxable Sales: RWF " + String.format("%.2f", taxableSales));
        System.out.println("Output VAT (18%): RWF " + String.format("%.2f", taxableSales * VAT_RATE));
        System.out.println("Taxable Purchases: RWF " + String.format("%.2f", taxablePurchases));
        System.out.println("Input VAT (18%): RWF " + String.format("%.2f", taxablePurchases * VAT_RATE));
        System.out.println("Net VAT Due: RWF " + String.format("%.2f", getTaxAmount()));
        System.out.println("Compliance Status: " + (isPaid() ? "Paid" : "Unpaid"));
        
        if (!isPaid()) {
            double penalty = enforceCompliance();
            if (penalty > 0) {
                System.out.println("Late Declaration Penalty: RWF " + String.format("%.2f", penalty));
                System.out.println("Total Amount Due: RWF " + String.format("%.2f", getTaxAmount() + penalty));
            }
        }
        
        // Flag if sales/purchase ratio is suspicious
        if (validateDeclaration() && taxableSales > 0 && taxablePurchases / taxableSales > 0.9) {
            System.out.println("WARNING: High purchase-to-sales ratio detected. May be flagged for audit.");
        }
        
        System.out.println("==========================");
    }
    
    @Override
    public double enforceCompliance() {
        if (!isPaid()) {
            // VAT is due by the 15th of the following month
            LocalDate dueDate = getDeclarationDate().plusMonths(1).withDayOfMonth(15);
            
            if (LocalDate.now().isAfter(dueDate)) {
                // 10% penalty for late declaration plus 1.5% per month late
                int monthsLate = Period.between(dueDate, LocalDate.now()).getMonths();
                double penaltyPercent = 10.0 + (monthsLate * 1.5);
                
                return getTaxAmount() * (penaltyPercent / 100.0);
            }
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nType: VAT";
    }
}

// Class for Withholding Tax Declaration
class WithholdingTaxDeclaration extends TaxDeclaration {
    public enum Category {
        RENT(15.0),
        DIVIDENDS(15.0),
        INTEREST(15.0),
        PROFESSIONAL_SERVICES(15.0),
        IMPORTS(5.0),
        PUBLIC_TENDER(3.0);
        
        private final double taxRate;
        
        Category(double taxRate) {
            this.taxRate = taxRate;
        }
        
        public double getTaxRate() {
            return taxRate;
        }
    }
    
    private Category category;
    private double baseAmount;
    
    public WithholdingTaxDeclaration(String declarationId, String taxpayerName, 
                                    String taxpayerTIN, LocalDate declarationDate, 
                                    double taxAmount, boolean isPaid, 
                                    Category category, double baseAmount) {
        super(declarationId, taxpayerName, taxpayerTIN, declarationDate, taxAmount, isPaid);
        setCategory(category);
        setBaseAmount(baseAmount);
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Withholding tax category cannot be null");
        }
        this.category = category;
    }
    
    public double getBaseAmount() {
        return baseAmount;
    }
    
    public void setBaseAmount(double baseAmount) {
        if (baseAmount <= 0) {
            throw new IllegalArgumentException("Base amount must be greater than 0");
        }
        this.baseAmount = baseAmount;
    }
    
    @Override
    public double calculateTax() {
        return baseAmount * (category.getTaxRate() / 100.0);
    }
    
    @Override
    public boolean validateDeclaration() {
        return baseAmount > 0 && category != null;
    }
    
    @Override
    public void generateReceipt() {
        System.out.println("\n======= WITHHOLDING TAX RECEIPT =======");
        System.out.println(toString());
        System.out.println("Category: " + category.name());
        System.out.println("Tax Rate: " + String.format("%.1f%%", category.getTaxRate()));
        System.out.println("Base Amount: RWF " + String.format("%.2f", baseAmount));
        System.out.println("Tax Due: RWF " + String.format("%.2f", getTaxAmount()));
        System.out.println("Compliance Status: " + (isPaid() ? "Paid" : "Unpaid"));
        
        if (!isPaid()) {
            double penalty = enforceCompliance();
            if (penalty > 0) {
                System.out.println("Non-Declaration Penalty: RWF " + String.format("%.2f", penalty));
                System.out.println("Total Amount Due: RWF " + String.format("%.2f", getTaxAmount() + penalty));
            }
        }
        System.out.println("====================================");
    }
    
    @Override
    public double enforceCompliance() {
        if (!isPaid()) {
            // Withholding tax is due within 15 days of the transaction
            LocalDate dueDate = getDeclarationDate().plusDays(15);
            
            if (LocalDate.now().isAfter(dueDate)) {
                // 50% penalty for failure to withhold and 10% per month late
                int monthsLate = Period.between(dueDate, LocalDate.now()).getMonths();
                double penaltyPercent = 50.0 + (monthsLate * 10.0);
                // Cap at 100%
                penaltyPercent = Math.min(penaltyPercent, 100.0);
                
                return getTaxAmount() * (penaltyPercent / 100.0);
            }
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nType: Withholding Tax - " + category.name();
    }
}

// Taxpayer class with encapsulation
class Taxpayer {
    public enum TaxpayerType {
        INDIVIDUAL, COMPANY
    }
    
    private String tin;
    private String name;
    private TaxpayerType type;
    private int complianceScore;
    private List<TaxDeclaration> declarations;
    
    public Taxpayer(String tin, String name, TaxpayerType type) {
        setTin(tin);
        setName(name);
        setType(type);
        this.complianceScore = 100; // Start with perfect score
        this.declarations = new ArrayList<>();
    }
    
    public String getTin() {
        return tin;
    }
    
    public void setTin(String tin) {
        if (tin == null || tin.trim().isEmpty()) {
            throw new IllegalArgumentException("TIN cannot be empty");
        }
        if (tin.length() != 9) {
            throw new IllegalArgumentException("TIN must be exactly 9 digits");
        }
        if (!tin.matches("\\d{9}")) {
            throw new IllegalArgumentException("TIN must contain only digits");
        }
        this.tin = tin;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }
    
    public TaxpayerType getType() {
        return type;
    }
    
    public void setType(TaxpayerType type) {
        if (type == null) {
            throw new IllegalArgumentException("Taxpayer type cannot be null");
        }
        this.type = type;
    }
    
    public int getComplianceScore() {
        return complianceScore;
    }
    
    public void setComplianceScore(int complianceScore) {
        if (complianceScore < 0) {
            complianceScore = 0;
        } else if (complianceScore > 100) {
            complianceScore = 100;
        }
        this.complianceScore = complianceScore;
    }
    
    public void addDeclaration(TaxDeclaration declaration) {
        // Check for duplicate declarations (same type in same period)
        for (TaxDeclaration existing : declarations) {
            if (existing.getClass().equals(declaration.getClass()) && 
                isSamePeriod(existing.getDeclarationDate(), declaration.getDeclarationDate())) {
                throw new IllegalArgumentException("Duplicate declaration for the same period");
            }
        }
        
        declarations.add(declaration);
        
        // Update compliance score based on payment status
        if (!declaration.isPaid()) {
            decreaseComplianceScore(5); // Decrease score by 5 points for each unpaid declaration
        }
    }
    
    private boolean isSamePeriod(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && 
               date1.getMonth() == date2.getMonth();
    }
    
    public void decreaseComplianceScore(int points) {
        setComplianceScore(complianceScore - points);
    }
    
    public void increaseComplianceScore(int points) {
        setComplianceScore(complianceScore + points);
    }
    
    public List<TaxDeclaration> getDeclarations() {
        return new ArrayList<>(declarations); // Return a copy to protect encapsulation
    }
    
    public String generateComplianceReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n=== COMPLIANCE REPORT ===\n");
        report.append("Taxpayer: ").append(name).append("\n");
        report.append("TIN: ").append(tin).append("\n");
        report.append("Type: ").append(type).append("\n");
        report.append("Compliance Score: ").append(complianceScore).append("\n");
        report.append("\nDeclarations:\n");
        
        double totalTax = 0;
        double totalPenalties = 0;
        
        if (declarations.isEmpty()) {
            report.append("No declarations found.\n");
        } else {
            for (TaxDeclaration declaration : declarations) {
                report.append("- ").append(declaration.getDeclarationId())
                      .append(" (").append(declaration.getClass().getSimpleName()).append("): ")
                      .append("RWF ").append(String.format("%.2f", declaration.getTaxAmount()));
                
                if (declaration.isPaid()) {
                    report.append(" [PAID]\n");
                } else {
                    double penalty = declaration.enforceCompliance();
                    report.append(" [UNPAID] - Penalty: RWF ")
                          .append(String.format("%.2f", penalty)).append("\n");
                    totalPenalties += penalty;
                }
                
                totalTax += declaration.getTaxAmount();
            }
        }
        
        report.append("\nTotal Tax Due: RWF ").append(String.format("%.2f", totalTax)).append("\n");
        report.append("Total Penalties: RWF ").append(String.format("%.2f", totalPenalties)).append("\n");
        report.append("Total Amount Due: RWF ").append(String.format("%.2f", totalTax + totalPenalties)).append("\n");
        
        report.append("===========================\n");
        return report.toString();
    }
    
    @Override
    public String toString() {
        return "Taxpayer: " + name + " (TIN: " + tin + ", Type: " + type + ")";
    }
}

// TaxOfficer class with encapsulation
class TaxOfficer {
    private String officerId;
    private String fullName;
    private String assignedRegion;
    private List<TaxDeclaration> auditsConducted;
    
    public TaxOfficer(String officerId, String fullName, String assignedRegion) {
        setOfficerId(officerId);
        setFullName(fullName);
        setAssignedRegion(assignedRegion);
        this.auditsConducted = new ArrayList<>();
    }
    
    public String getOfficerId() {
        return officerId;
    }
    
    public void setOfficerId(String officerId) {
        if (officerId == null || officerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Officer ID cannot be empty");
        }
        this.officerId = officerId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName;
    }
    
    public String getAssignedRegion() {
        return assignedRegion;
    }
    
    public void setAssignedRegion(String assignedRegion) {
        if (assignedRegion == null || assignedRegion.trim().isEmpty()) {
            throw new IllegalArgumentException("Assigned region cannot be empty");
        }
        this.assignedRegion = assignedRegion;
    }
    
    public List<TaxDeclaration> getAuditsConducted() {
        return new ArrayList<>(auditsConducted); // Return a copy to protect encapsulation
    }
    
    public boolean auditDeclaration(TaxDeclaration declaration) {
        // Perform audit verification logic
        boolean auditPassed = declaration.validateDeclaration();
        
        // Add to audit history
        auditsConducted.add(declaration);
        
        // If audit fails, apply additional enforcement
        if (!auditPassed && !declaration.isPaid()) {
            declaration.enforceCompliance();
        }
        
        return auditPassed;
    }
    
    public String generateAuditSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n=== AUDIT SUMMARY ===\n");
        summary.append("Officer: ").append(fullName).append(" (").append(officerId).append(")\n");
        summary.append("Region: ").append(assignedRegion).append("\n");
        summary.append("Audits Conducted: ").append(auditsConducted.size()).append("\n\n");
        
        int passedAudits = 0;
        for (TaxDeclaration declaration : auditsConducted) {
            boolean passed = declaration.validateDeclaration();
            if (passed) passedAudits++;
            
            summary.append("- Declaration ").append(declaration.getDeclarationId())
                  .append(" (").append(declaration.getTaxpayerName()).append("): ")
                  .append(passed ? "PASSED" : "FAILED").append("\n");
        }
        
        if (!auditsConducted.isEmpty()) {
            double complianceRate = (double) passedAudits / auditsConducted.size() * 100;
            summary.append("\nOverall Compliance Rate: ").append(String.format("%.1f%%", complianceRate)).append("\n");
        }
        
        summary.append("======================\n");
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "Tax Officer: " + fullName + " (ID: " + officerId + ", Region: " + assignedRegion + ")";
    }
}

// Main class for user interaction
public class TaxEnforcementManagementSystem {
    private static final ArrayList<TaxDeclaration> declarations = new ArrayList<>();
    private static final ArrayList<Taxpayer> taxpayers = new ArrayList<>();
    private static final ArrayList<TaxOfficer> officers = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Initialize with sample data
        initializeSampleData();
        
        boolean exit = false;
        
        System.out.println("Welcome to RRA Tax Enforcement Management System");
        
        while (!exit) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        registerTaxDeclaration();
                        break;
                    case 2:
                        viewRegisteredDeclarations();
                        break;
                    case 3:
                        viewTaxpayerCompliance();
                        break;
                    case 4:
                        printTaxReceipt();
                        break;
                    case 5:
                        viewUnpaidTaxes();
                        break;
                    case 6:
                        conductAudit();
                        break;
                    case 7:
                        exit = true;
                        System.out.println("Thank you for using RRA Tax Enforcement Management System.");
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
    
    private static void initializeSampleData() {
        // Add sample taxpayers
        taxpayers.add(new Taxpayer("123456789", "Kigali Enterprises Ltd", Taxpayer.TaxpayerType.COMPANY));
        taxpayers.add(new Taxpayer("987654321", "Jean-Paul Munyakazi", Taxpayer.TaxpayerType.INDIVIDUAL));
        
        // Add sample tax officers
        officers.add(new TaxOfficer("RRA001", "Claude Mugisha", "Kigali"));
        officers.add(new TaxOfficer("RRA002", "Diane Mukamana", "Eastern Province"));
    }
    
    private static void displayMenu() {
        System.out.println("\n===== RRA TAX ENFORCEMENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Register a new tax declaration");
        System.out.println("2. View registered declarations");
        System.out.println("3. View taxpayer compliance report");
        System.out.println("4. Print tax receipt");
        System.out.println("5. View summary of unpaid taxes");
        System.out.println("6. Conduct audit");
        System.out.println("7. Exit");
        System.out.println("================================================");
    }
    
    private static void registerTaxDeclaration() {
        System.out.println("\n----- TAX DECLARATION REGISTRATION -----");
        System.out.println("Select tax type:");
        System.out.println("1. PAYE (Pay As You Earn)");
        System.out.println("2. VAT (Value Added Tax)");
        System.out.println("3. Withholding Tax");
        
        int taxTypeChoice = getIntInput("Enter your choice: ");
        
        // Get or create taxpayer
        Taxpayer taxpayer = selectOrCreateTaxpayer();
        if (taxpayer == null) return;
        
        // Generate a unique declaration ID
        String declarationId = "DEC-" + System.currentTimeMillis() % 10000;
        
        // Get declaration date
        LocalDate declarationDate = getValidDate("Enter declaration date (dd/MM/yyyy): ");
        
        // Set tax as initially unpaid
        boolean isPaid = false;
        
        try {
            TaxDeclaration declaration = null;
            
            switch (taxTypeChoice) {
                case 1: // PAYE
                    double grossSalary = getValidPositiveDouble("Enter gross salary (RWF): ", "Gross salary must be greater than 0");
                    int numDependents = getValidNonNegativeInt("Enter number of dependents: ", "Number of dependents cannot be negative");
                    
                    // Calculate tax amount based on gross salary and dependents
                    PAYEDeclaration payeDeclaration = new PAYEDeclaration(
                        declarationId, taxpayer.getName(), taxpayer.getTin(), 
                        declarationDate, 0, isPaid, grossSalary, numDependents
                    );
                    double payeTax = payeDeclaration.calculateTax();
                    payeDeclaration.setTaxAmount(payeTax);
                    
                    declaration = payeDeclaration;
                    break;
                    
                case 2: // VAT
                    double taxableSales = getValidNonNegativeDouble("Enter taxable sales (RWF): ", "Taxable sales cannot be negative");
                    double taxablePurchases = getValidNonNegativeDouble("Enter taxable purchases (RWF): ", "Taxable purchases cannot be negative");
                    
                    // Validate that purchases don't significantly exceed sales
                    if (taxablePurchases > taxableSales * 1.2) {
                        System.out.println("Warning: Purchases exceed sales by more than 20%. This may trigger an audit.");
                        boolean confirm = getValidBooleanInput("Do you want to continue? (true/false): ");
                        if (!confirm) return;
                    }
                    
                    // Calculate VAT amount
                    VATDeclaration vatDeclaration = new VATDeclaration(
                        declarationId, taxpayer.getName(), taxpayer.getTin(), 
                        declarationDate, 0, isPaid, taxableSales, taxablePurchases
                    );
                    double vatTax = vatDeclaration.calculateTax();
                    vatDeclaration.setTaxAmount(vatTax);
                    
                    declaration = vatDeclaration;
                    break;
                    
                case 3: // Withholding Tax
                    System.out.println("Select withholding tax category:");
                    System.out.println("1. Rent (15%)");
                    System.out.println("2. Dividends (15%)");
                    System.out.println("3. Interest (15%)");
                    System.out.println("4. Professional Services (15%)");
                    System.out.println("5. Imports (5%)");
                    System.out.println("6. Public Tender (3%)");
                    
                    int categoryChoice = getIntInput("Enter choice: ");
                    WithholdingTaxDeclaration.Category category;
                    
                    switch (categoryChoice) {
                        case 1:
                            category = WithholdingTaxDeclaration.Category.RENT;
                            break;
                        case 2:
                            category = WithholdingTaxDeclaration.Category.DIVIDENDS;
                            break;
                        case 3:
                            category = WithholdingTaxDeclaration.Category.INTEREST;
                            break;
                        case 4:
                            category = WithholdingTaxDeclaration.Category.PROFESSIONAL_SERVICES;
                            break;
                        case 5:
                            category = WithholdingTaxDeclaration.Category.IMPORTS;
                            break;
                        case 6:
                            category = WithholdingTaxDeclaration.Category.PUBLIC_TENDER;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid category choice");
                    }
                    
                    double baseAmount = getValidPositiveDouble("Enter base amount (RWF): ", "Base amount must be greater than 0");
                    
                    // Calculate withholding tax
                    WithholdingTaxDeclaration whtDeclaration = new WithholdingTaxDeclaration(
                        declarationId, taxpayer.getName(), taxpayer.getTin(), 
                        declarationDate, 0, isPaid, category, baseAmount
                    );
                    double whtTax = whtDeclaration.calculateTax();
                    whtDeclaration.setTaxAmount(whtTax);
                    
                    declaration = whtDeclaration;
                    break;
                    
                default:
                    System.out.println("Invalid tax type choice.");
                    return;
            }
            
            // Add declaration to lists
            declarations.add(declaration);
            taxpayer.addDeclaration(declaration);
            
            System.out.println("Tax declaration registered successfully!");
            System.out.printf("Tax amount calculated: RWF %.2f\n", declaration.getTaxAmount());
            
        } catch (IllegalArgumentException e) {
            System.out.println("Declaration failed: " + e.getMessage());
        }
    }
    
    private static void viewRegisteredDeclarations() {
        if (declarations.isEmpty()) {
            System.out.println("\nNo tax declarations registered yet.");
            return;
        }
        
        System.out.println("\n----- REGISTERED TAX DECLARATIONS -----");
        for (int i = 0; i < declarations.size(); i++) {
            TaxDeclaration declaration = declarations.get(i);
            System.out.println("\nDeclaration #" + (i + 1) + ":");
            System.out.println(declaration);
            System.out.println("--------------------------------------");
        }
    }
    
    private static void viewTaxpayerCompliance() {
        if (taxpayers.isEmpty()) {
            System.out.println("\nNo taxpayers registered yet.");
            return;
        }
        
        Taxpayer taxpayer = selectTaxpayer();
        if (taxpayer == null) return;
        
        System.out.println(taxpayer.generateComplianceReport());
    }
    
    private static void printTaxReceipt() {
        if (declarations.isEmpty()) {
            System.out.println("\nNo tax declarations registered yet.");
            return;
        }
        
        System.out.println("\n----- SELECT DECLARATION FOR RECEIPT -----");
        for (int i = 0; i < declarations.size(); i++) {
            TaxDeclaration declaration = declarations.get(i);
            System.out.printf("%d. %s - %s (RWF %.2f) - %s\n", 
                             (i + 1), 
                             declaration.getDeclarationId(), 
                             declaration.getClass().getSimpleName(), 
                             declaration.getTaxAmount(),
                             declaration.isPaid() ? "PAID" : "UNPAID");
        }
        
        int declarationIndex = getIntInput("Enter declaration number: ") - 1;
        
        if (declarationIndex >= 0 && declarationIndex < declarations.size()) {
            TaxDeclaration selectedDeclaration = declarations.get(declarationIndex);
            
            // Option to mark as paid before generating receipt
            if (!selectedDeclaration.isPaid()) {
                boolean markAsPaid = getValidBooleanInput("Declaration is unpaid. Mark as paid now? (true/false): ");
                if (markAsPaid) {
                    selectedDeclaration.setPaid(true);
                    System.out.println("Declaration marked as paid.");
                    
                    // Improve compliance score for taxpayer
                    for (Taxpayer taxpayer : taxpayers) {
                        if (taxpayer.getTin().equals(selectedDeclaration.getTaxpayerTIN())) {
                            taxpayer.increaseComplianceScore(5);
                            break;
                        }
                    }
                }
            }
            
            selectedDeclaration.generateReceipt();
        } else {
            System.out.println("Invalid declaration number.");
        }
    }
    
    private static void viewUnpaidTaxes() {
        if (declarations.isEmpty()) {
            System.out.println("\nNo tax declarations registered yet.");
            return;
        }
        
        List<TaxDeclaration> unpaidDeclarations = new ArrayList<>();
        
        for (TaxDeclaration declaration : declarations) {
            if (!declaration.isPaid()) {
                unpaidDeclarations.add(declaration);
            }
        }
        
        if (unpaidDeclarations.isEmpty()) {
            System.out.println("\nNo unpaid tax declarations found.");
            return;
        }
        
        System.out.println("\n----- UNPAID TAXES SUMMARY -----");
        
        double totalUnpaidTax = 0;
        double totalPenalties = 0;
        
        for (TaxDeclaration declaration : unpaidDeclarations) {
            double penalty = declaration.enforceCompliance();
            System.out.printf("Declaration ID: %s - %s\n", 
                             declaration.getDeclarationId(), 
                             declaration.getClass().getSimpleName());
            System.out.printf("Taxpayer: %s (TIN: %s)\n", 
                             declaration.getTaxpayerName(), 
                             declaration.getTaxpayerTIN());
            System.out.printf("Tax Due: RWF %.2f\n", declaration.getTaxAmount());
            System.out.printf("Penalty: RWF %.2f\n", penalty);
            System.out.printf("Total Due: RWF %.2f\n", declaration.getTaxAmount() + penalty);
            System.out.println("---------------------------");
            
            totalUnpaidTax += declaration.getTaxAmount();
            totalPenalties += penalty;
        }
        
        System.out.printf("\nTotal Unpaid Taxes: RWF %.2f\n", totalUnpaidTax);
        System.out.printf("Total Penalties: RWF %.2f\n", totalPenalties);
        System.out.printf("Grand Total Due: RWF %.2f\n", totalUnpaidTax + totalPenalties);
    }
    
    private static void conductAudit() {
        if (declarations.isEmpty()) {
            System.out.println("\nNo tax declarations to audit.");
            return;
        }
        
        if (officers.isEmpty()) {
            System.out.println("\nNo tax officers available to conduct audit.");
            return;
        }
        
        // Select an officer
        System.out.println("\n----- SELECT TAX OFFICER -----");
        for (int i = 0; i < officers.size(); i++) {
            System.out.println((i + 1) + ". " + officers.get(i));
        }
        
        int officerIndex = getIntInput("Enter officer number: ") - 1;
        
        if (officerIndex < 0 || officerIndex >= officers.size()) {
            System.out.println("Invalid officer selection.");
            return;
        }
        
        TaxOfficer selectedOfficer = officers.get(officerIndex);
        
        // Select a declaration to audit
        System.out.println("\n----- SELECT DECLARATION TO AUDIT -----");
        for (int i = 0; i < declarations.size(); i++) {
            TaxDeclaration declaration = declarations.get(i);
            System.out.printf("%d. %s - %s (RWF %.2f) - %s\n", 
                             (i + 1), 
                             declaration.getDeclarationId(), 
                             declaration.getClass().getSimpleName(), 
                             declaration.getTaxAmount(),
                             declaration.isPaid() ? "PAID" : "UNPAID");
        }
        
        int declarationIndex = getIntInput("Enter declaration number: ") - 1;
        
        if (declarationIndex < 0 || declarationIndex >= declarations.size()) {
            System.out.println("Invalid declaration selection.");
            return;
        }
        
        TaxDeclaration selectedDeclaration = declarations.get(declarationIndex);
        
        // Conduct the audit
        boolean auditPassed = selectedOfficer.auditDeclaration(selectedDeclaration);
        
        System.out.println("\nAudit completed.");
        System.out.println("Result: " + (auditPassed ? "PASSED" : "FAILED"));
        
        if (!auditPassed) {
            System.out.println("The declaration has failed the audit due to validation issues.");
            
            // Update taxpayer compliance score
            for (Taxpayer taxpayer : taxpayers) {
                if (taxpayer.getTin().equals(selectedDeclaration.getTaxpayerTIN())) {
                    taxpayer.decreaseComplianceScore(10);
                    System.out.println("Taxpayer compliance score has been decreased.");
                    break;
                }
            }
        }
        
        // Show audit summary
        System.out.println(selectedOfficer.generateAuditSummary());
    }
    
    // Helper methods for taxpayer selection/creation
    
    private static Taxpayer selectOrCreateTaxpayer() {
        System.out.println("\nDo you want to use an existing taxpayer or create a new one?");
        System.out.println("1. Use existing taxpayer");
        System.out.println("2. Create new taxpayer");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                return selectTaxpayer();
            case 2:
                return createTaxpayer();
            default:
                System.out.println("Invalid choice.");
                return null;
        }
    }
    
    private static Taxpayer selectTaxpayer() {
        if (taxpayers.isEmpty()) {
            System.out.println("No taxpayers registered yet. Please create a new taxpayer.");
            return createTaxpayer();
        }
        
        System.out.println("\n----- SELECT TAXPAYER -----");
        for (int i = 0; i < taxpayers.size(); i++) {
            System.out.println((i + 1) + ". " + taxpayers.get(i));
        }
        
        int taxpayerIndex = getIntInput("Enter taxpayer number: ") - 1;
        
        if (taxpayerIndex >= 0 && taxpayerIndex < taxpayers.size()) {
            return taxpayers.get(taxpayerIndex);
        } else {
            System.out.println("Invalid taxpayer selection.");
            return null;
        }
    }
    
    private static Taxpayer createTaxpayer() {
        try {
            String tin = getValidTIN("Enter TIN (9 digits): ");
            
            // Check for duplicate TIN
            for (Taxpayer existing : taxpayers) {
                if (existing.getTin().equals(tin)) {
                    System.out.println("A taxpayer with this TIN already exists.");
                    return existing;
                }
            }
            
            String name = getStringInput("Enter taxpayer name: ");
            
            System.out.println("Select taxpayer type:");
            System.out.println("1. Individual");
            System.out.println("2. Company");
            
            int typeChoice = getIntInput("Enter your choice: ");
            
            Taxpayer.TaxpayerType type;
            switch (typeChoice) {
                case 1:
                    type = Taxpayer.TaxpayerType.INDIVIDUAL;
                    break;
                case 2:
                    type = Taxpayer.TaxpayerType.COMPANY;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid taxpayer type");
            }
            
            Taxpayer newTaxpayer = new Taxpayer(tin, name, type);
            taxpayers.add(newTaxpayer);
            
            System.out.println("Taxpayer created successfully!");
            return newTaxpayer;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Taxpayer creation failed: " + e.getMessage());
            return null;
        }
    }
    
    // Input validation methods
    
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
    
    private static String getValidTIN(String prompt) {
        while (true) {
            try {
                String tin = getStringInput(prompt);
                if (tin.length() != 9) {
                    throw new IllegalArgumentException("TIN must be exactly 9 digits");
                }
                if (!tin.matches("\\d{9}")) {
                    throw new IllegalArgumentException("TIN must contain only digits");
                }
                return tin;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static LocalDate getValidDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        while (true) {
            try {
                String dateStr = getStringInput(prompt);
                LocalDate date = LocalDate.parse(dateStr, formatter);
                
                // Validate that date is not in the future
                if (date.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Declaration date cannot be in the future");
                }
                
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use dd/MM/yyyy format.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
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
    
    private static double getValidNonNegativeDouble(String prompt, String errorMessage) {
        while (true) {
            try {
                double value = getDoubleInput(prompt);
                if (value < 0) {
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
    
    private static int getValidNonNegativeInt(String prompt, String errorMessage) {
        while (true) {
            try {
                int value = getIntInput(prompt);
                if (value < 0) {
                    throw new IllegalArgumentException(errorMessage);
                }
                return value;
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
    
    // Generic input validation method with custom validator
    private static <T> T getValidInput(String prompt, Function<String, T> validator) {
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