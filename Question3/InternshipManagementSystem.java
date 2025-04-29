import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

// Interface for report generation (optional bonus)
interface Reportable {
    void generateDetailedReport();
}

// Abstract class: Internship
abstract class Internship implements Reportable {
    // Private fields for encapsulation
    private String internshipId;
    private Student student;
    private String companyName;
    private Supervisor supervisor;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // "PENDING", "ONGOING", "COMPLETED"
    
    // Constructor
    public Internship(String internshipId, Student student, String companyName,
                     Supervisor supervisor, LocalDate startDate, LocalDate endDate, String status) {
        setInternshipId(internshipId);
        setStudent(student);
        setCompanyName(companyName);
        setSupervisor(supervisor);
        setStartDate(startDate);
        setEndDate(endDate);
        setStatus(status);
    }
    
    // Abstract methods that all child classes must implement
    public abstract void assignSupervisor();
    public abstract void trackProgress();
    public abstract void generateReport();
    public abstract boolean validateInternship();
    
    // Calculate internship duration in weeks
    protected long getDurationInWeeks() {
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }
    
    // Calculate internship duration in months
    protected long getDurationInMonths() {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }
    
    // Getters and Setters with validation
    public String getInternshipId() {
        return internshipId;
    }
    
    public void setInternshipId(String internshipId) {
        if (internshipId == null || internshipId.trim().isEmpty()) {
            throw new IllegalArgumentException("Internship ID cannot be empty");
        }
        this.internshipId = internshipId;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        this.student = student;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.companyName = companyName;
    }
    
    public Supervisor getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(Supervisor supervisor) {
        if (supervisor == null) {
            throw new IllegalArgumentException("Supervisor cannot be null");
        }
        this.supervisor = supervisor;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        if (!status.equals("PENDING") && !status.equals("ONGOING") && !status.equals("COMPLETED")) {
            throw new IllegalArgumentException("Status must be 'PENDING', 'ONGOING', or 'COMPLETED'");
        }
        this.status = status;
    }
    
    // Common toString method to display basic internship info
    @Override
    public String toString() {
        return String.format("Internship ID: %s%nStudent: %s%nCompany: %s%nSupervisor: %s%n" +
                "Period: %s to %s%nStatus: %s",
                internshipId, student.getFullName(), companyName, supervisor.getFullName(),
                startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                endDate.format(DateTimeFormatter.ISO_LOCAL_DATE), status);
    }
    
    // Implementation of Reportable interface
    @Override
    public void generateDetailedReport() {
        System.out.println("===== DETAILED INTERNSHIP REPORT =====");
        System.out.println("Internship ID: " + internshipId);
        System.out.println("\nSTUDENT INFORMATION:");
        System.out.println("Name: " + student.getFullName());
        System.out.println("University: " + student.getUniversity());
        System.out.println("Email: " + student.getEmail());
        
        System.out.println("\nINTERNSHIP DETAILS:");
        System.out.println("Company: " + companyName);
        System.out.println("Duration: " + getDurationInWeeks() + " weeks (" + 
                          startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + " to " +
                          endDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + ")");
        
        System.out.println("\nSUPERVISOR INFORMATION:");
        System.out.println("Name: " + supervisor.getFullName());
        System.out.println("Qualification: " + supervisor.getQualification());
        System.out.println("Email: " + supervisor.getEmail());
        
        System.out.println("\nSTATUS INFORMATION:");
        System.out.println("Current Status: " + status);
        
        generateReport(); // Call the specific implementation
        System.out.println("=======================================");
    }
}

// Concrete implementation for ULK Internship
class ULKInternship extends Internship {
    private String progressNotes;
    
    public ULKInternship(String internshipId, Student student, String companyName,
                        Supervisor supervisor, LocalDate startDate, LocalDate endDate, String status) {
        super(internshipId, student, companyName, supervisor, startDate, endDate, status);
        this.progressNotes = "";
        
        if (!validateInternship()) {
            throw new IllegalArgumentException("ULK Internship validation failed");
        }
    }
    
    public String getProgressNotes() {
        return progressNotes;
    }
    
    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes != null ? progressNotes : "";
    }
    
    @Override
    public void assignSupervisor() {
        if (!getSupervisor().getQualification().equals("Masters") && 
            !getSupervisor().getQualification().equals("PhD")) {
            throw new IllegalArgumentException("ULK internships require supervisors with a Master's degree or higher");
        }
        System.out.println("Supervisor " + getSupervisor().getFullName() + 
                          " assigned to ULK internship for " + getStudent().getFullName());
    }
    
    @Override
    public void trackProgress() {
        System.out.println("Tracking progress for ULK internship " + getInternshipId());
        // Simplified for this example
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter progress notes: ");
        String notes = scanner.nextLine();
        this.progressNotes += LocalDate.now() + ": " + notes + "\n";
        System.out.println("Progress updated successfully");
    }
    
    @Override
    public void generateReport() {
        System.out.println("\nULK INTERNSHIP SPECIFIC DETAILS:");
        System.out.println("Duration check: " + (getDurationInWeeks() >= 6 ? "Valid (≥ 6 weeks)" : "Invalid (< 6 weeks)"));
        System.out.println("Supervisor qualification: " + getSupervisor().getQualification() + 
                          " (Required: Masters or PhD)");
        System.out.println("\nPROGRESS NOTES:");
        System.out.println(progressNotes.isEmpty() ? "No progress notes available" : progressNotes);
    }
    
    @Override
    public boolean validateInternship() {
        // Validate student is from ULK
        if (!getStudent().getUniversity().equals("ULK")) {
            System.out.println("Error: Student must be from ULK for a ULK internship");
            return false;
        }
        
        // Validate internship is at least 6 weeks
        if (getDurationInWeeks() < 6) {
            System.out.println("Error: ULK internship must be at least 6 weeks long");
            return false;
        }
        
        // Validate supervisor has Master's or PhD
        if (!getSupervisor().getQualification().equals("Masters") && 
            !getSupervisor().getQualification().equals("PhD")) {
            System.out.println("Error: ULK internship supervisor must have a Master's degree or higher");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return "ULK Internship\n" + super.toString();
    }
}

// Concrete implementation for UR Internship
class URInternship extends Internship {
    private Supervisor secondarySupervisor;
    private String feedbackLog;
    
    public URInternship(String internshipId, Student student, String companyName,
                       Supervisor supervisor, LocalDate startDate, LocalDate endDate, String status) {
        super(internshipId, student, companyName, supervisor, startDate, endDate, status);
        this.feedbackLog = "";
        
        if (!validateInternship()) {
            throw new IllegalArgumentException("UR Internship validation failed");
        }
    }
    
    public Supervisor getSecondarySupervisor() {
        return secondarySupervisor;
    }
    
    public void setSecondarySupervisor(Supervisor secondarySupervisor) {
        this.secondarySupervisor = secondarySupervisor;
    }
    
    public String getFeedbackLog() {
        return feedbackLog;
    }
    
    public void setFeedbackLog(String feedbackLog) {
        this.feedbackLog = feedbackLog != null ? feedbackLog : "";
    }
    
    @Override
    public void assignSupervisor() {
        System.out.println("Primary Supervisor " + getSupervisor().getFullName() + 
                          " assigned to UR internship for " + getStudent().getFullName());
        
        if (secondarySupervisor != null) {
            System.out.println("Secondary Supervisor " + secondarySupervisor.getFullName() + 
                              " also assigned to this internship");
        }
    }
    
    @Override
    public void trackProgress() {
        System.out.println("Tracking progress for UR internship " + getInternshipId());
        // Simplified for this example
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter feedback for " + getStudent().getFullName() + ": ");
        String feedback = scanner.nextLine();
        this.feedbackLog += LocalDate.now() + ": " + feedback + "\n";
        System.out.println("Feedback logged successfully");
    }
    
    @Override
    public void generateReport() {
        System.out.println("\nUR INTERNSHIP SPECIFIC DETAILS:");
        System.out.println("Duration check: " + (getDurationInMonths() >= 2 && getDurationInMonths() <= 6 ? 
                                              "Valid (2-6 months)" : "Invalid (outside 2-6 months range)"));
        System.out.println("Primary Supervisor: " + getSupervisor().getFullName());
        
        if (secondarySupervisor != null) {
            System.out.println("Secondary Supervisor: " + secondarySupervisor.getFullName());
        } else {
            System.out.println("No secondary supervisor assigned");
        }
        
        System.out.println("\nFEEDBACK HISTORY:");
        System.out.println(feedbackLog.isEmpty() ? "No feedback available" : feedbackLog);
    }
    
    @Override
    public boolean validateInternship() {
        // Validate student is from UR
        if (!getStudent().getUniversity().equals("UR")) {
            System.out.println("Error: Student must be from UR for a UR internship");
            return false;
        }
        
        // Validate internship duration is between 2 and 6 months
        long durationMonths = getDurationInMonths();
        if (durationMonths < 2 || durationMonths > 6) {
            System.out.println("Error: UR internship must be between 2 and 6 months");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UR Internship\n").append(super.toString());
        
        if (secondarySupervisor != null) {
            sb.append("\nSecondary Supervisor: ").append(secondarySupervisor.getFullName());
        }
        
        return sb.toString();
    }
}

// Concrete implementation for AUCA Internship
class AUCAInternship extends Internship {
    private int communityServiceHours;
    private ArrayList<String> weeklyReports;
    
    public AUCAInternship(String internshipId, Student student, String companyName,
                         Supervisor supervisor, LocalDate startDate, LocalDate endDate, String status) {
        super(internshipId, student, companyName, supervisor, startDate, endDate, status);
        this.communityServiceHours = 0;
        this.weeklyReports = new ArrayList<>();
        
        if (!validateInternship()) {
            throw new IllegalArgumentException("AUCA Internship validation failed");
        }
    }
    
    public int getCommunityServiceHours() {
        return communityServiceHours;
    }
    
    public void setCommunityServiceHours(int communityServiceHours) {
        if (communityServiceHours < 0) {
            throw new IllegalArgumentException("Community service hours cannot be negative");
        }
        this.communityServiceHours = communityServiceHours;
    }
    
    public ArrayList<String> getWeeklyReports() {
        return new ArrayList<>(weeklyReports); // Return a copy to prevent direct modification
    }
    
    @Override
    public void assignSupervisor() {
        System.out.println("Supervisor " + getSupervisor().getFullName() + 
                          " assigned to AUCA internship for " + getStudent().getFullName());
    }
    
    @Override
    public void trackProgress() {
        System.out.println("Tracking progress for AUCA internship " + getInternshipId());
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter weekly report for week #" + (weeklyReports.size() + 1) + ": ");
        String report = scanner.nextLine();
        weeklyReports.add("Week " + (weeklyReports.size() + 1) + ": " + report);
        
        System.out.print("Enter community service hours completed this week: ");
        int hours = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (hours < 0) {
            System.out.println("Hours cannot be negative. No hours added.");
        } else {
            communityServiceHours += hours;
            System.out.println("Added " + hours + " community service hours. Total: " + communityServiceHours);
        }
    }
    
    @Override
    public void generateReport() {
        System.out.println("\nAUCA INTERNSHIP SPECIFIC DETAILS:");
        System.out.println("Community Service Hours: " + communityServiceHours);
        System.out.println("Number of Weekly Reports: " + weeklyReports.size());
        
        System.out.println("\nWEEKLY REPORTS:");
        if (weeklyReports.isEmpty()) {
            System.out.println("No weekly reports available");
        } else {
            for (String report : weeklyReports) {
                System.out.println(report);
            }
        }
    }
    
    @Override
    public boolean validateInternship() {
        // Validate student is from AUCA
        if (!getStudent().getUniversity().equals("AUCA")) {
            System.out.println("Error: Student must be from AUCA for an AUCA internship");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return "AUCA Internship\n" + super.toString() + 
               "\nCommunity Service Hours: " + communityServiceHours;
    }
}

// Concrete implementation for UK Internship
class UKInternship extends Internship {
    private Supervisor universitySupervisor;
    private String englishProficiencyCert;
    private String evaluationNotes;
    
    public UKInternship(String internshipId, Student student, String companyName,
                       Supervisor companySupervisor, Supervisor universitySupervisor,
                       LocalDate startDate, LocalDate endDate, String status, String englishProficiencyCert) {
        super(internshipId, student, companyName, companySupervisor, startDate, endDate, status);
        this.universitySupervisor = universitySupervisor;
        this.englishProficiencyCert = englishProficiencyCert;
        this.evaluationNotes = "";
        
        if (!validateInternship()) {
            throw new IllegalArgumentException("UK Internship validation failed");
        }
    }
    
    public Supervisor getUniversitySupervisor() {
        return universitySupervisor;
    }
    
    public void setUniversitySupervisor(Supervisor universitySupervisor) {
        if (universitySupervisor == null) {
            throw new IllegalArgumentException("University supervisor cannot be null");
        }
        this.universitySupervisor = universitySupervisor;
    }
    
    public String getEnglishProficiencyCert() {
        return englishProficiencyCert;
    }
    
    public void setEnglishProficiencyCert(String englishProficiencyCert) {
        if (englishProficiencyCert == null || englishProficiencyCert.trim().isEmpty()) {
            throw new IllegalArgumentException("English proficiency certification cannot be empty");
        }
        this.englishProficiencyCert = englishProficiencyCert;
    }
    
    public String getEvaluationNotes() {
        return evaluationNotes;
    }
    
    public void setEvaluationNotes(String evaluationNotes) {
        this.evaluationNotes = evaluationNotes != null ? evaluationNotes : "";
    }
    
    @Override
    public void assignSupervisor() {
        System.out.println("Company Supervisor " + getSupervisor().getFullName() + 
                          " and University Supervisor " + universitySupervisor.getFullName() +
                          " assigned to UK internship for " + getStudent().getFullName());
    }
    
    @Override
    public void trackProgress() {
        System.out.println("Tracking progress for UK internship " + getInternshipId());
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter evaluation notes from company supervisor: ");
        String companyNotes = scanner.nextLine();
        
        System.out.print("Enter evaluation notes from university supervisor: ");
        String universityNotes = scanner.nextLine();
        
        this.evaluationNotes += LocalDate.now() + ":\n" +
                               "Company: " + companyNotes + "\n" +
                               "University: " + universityNotes + "\n";
        
        System.out.println("Evaluation notes added successfully");
    }
    
    @Override
    public void generateReport() {
        System.out.println("\nUK INTERNSHIP SPECIFIC DETAILS:");
        System.out.println("English Proficiency: " + englishProficiencyCert);
        System.out.println("Company Supervisor: " + getSupervisor().getFullName());
        System.out.println("University Supervisor: " + universitySupervisor.getFullName());
        
        System.out.println("\nEVALUATION NOTES:");
        System.out.println(evaluationNotes.isEmpty() ? "No evaluation notes available" : evaluationNotes);
    }
    
    @Override
    public boolean validateInternship() {
        // Validate student is from UK
        if (!getStudent().getUniversity().equals("UK")) {
            System.out.println("Error: Student must be from UK for a UK internship");
            return false;
        }
        
        // Validate university supervisor is not null
        if (universitySupervisor == null) {
            System.out.println("Error: UK internship requires a university supervisor");
            return false;
        }
        
        // Validate English proficiency certification
        if (englishProficiencyCert == null || englishProficiencyCert.trim().isEmpty()) {
            System.out.println("Error: UK internship requires English proficiency certification");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return "UK Internship\n" + super.toString() +
               "\nUniversity Supervisor: " + universitySupervisor.getFullName() +
               "\nEnglish Proficiency: " + englishProficiencyCert;
    }
}

// Concrete implementation for Remote Internship
class RemoteInternship extends Internship {
    private String communicationLog;
    private String remoteAccessUrl;
    
    public RemoteInternship(String internshipId, Student student, String companyName,
                           Supervisor supervisor, LocalDate startDate, LocalDate endDate, 
                           String status, String remoteAccessUrl) {
        super(internshipId, student, companyName, supervisor, startDate, endDate, status);
        this.communicationLog = "";
        setRemoteAccessUrl(remoteAccessUrl);
        
        if (!validateInternship()) {
            throw new IllegalArgumentException("Remote Internship validation failed");
        }
    }
    
    public String getCommunicationLog() {
        return communicationLog;
    }
    
    public void setCommunicationLog(String communicationLog) {
        this.communicationLog = communicationLog != null ? communicationLog : "";
    }
    
    public String getRemoteAccessUrl() {
        return remoteAccessUrl;
    }
    
    public void setRemoteAccessUrl(String remoteAccessUrl) {
        if (remoteAccessUrl == null || remoteAccessUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Remote access URL cannot be empty");
        }
        this.remoteAccessUrl = remoteAccessUrl;
    }
    
    public void logCommunication(String communication) {
        this.communicationLog += LocalDate.now() + ": " + communication + "\n";
    }
    
    @Override
    public void assignSupervisor() {
        System.out.println("Remote Supervisor " + getSupervisor().getFullName() + 
                          " assigned to Remote internship for " + getStudent().getFullName());
    }
    
    @Override
    public void trackProgress() {
        System.out.println("Tracking progress for Remote internship " + getInternshipId());
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter communication log entry: ");
        String entry = scanner.nextLine();
        logCommunication(entry);
        
        System.out.println("Communication logged successfully");
    }
    
    @Override
    public void generateReport() {
        System.out.println("\nREMOTE INTERNSHIP SPECIFIC DETAILS:");
        System.out.println("Remote Access URL: " + remoteAccessUrl);
        System.out.println("University: " + getStudent().getUniversity() + " (Remote internships valid for all universities)");
        
        System.out.println("\nCOMMUNICATION LOG:");
        System.out.println(communicationLog.isEmpty() ? "No communication logs available" : communicationLog);
    }
    
    @Override
    public boolean validateInternship() {
        // Validate minimum duration (using same as ULK for simplicity)
        if (getDurationInWeeks() < 6) {
            System.out.println("Error: Remote internship must be at least 6 weeks long");
            return false;
        }
        
        // Validate remote access URL
        if (remoteAccessUrl == null || remoteAccessUrl.trim().isEmpty()) {
            System.out.println("Error: Remote internship requires a remote access URL");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return "Remote Internship\n" + super.toString() +
               "\nRemote Access URL: " + remoteAccessUrl;
    }
}

// Class: Student (Encapsulation)
class Student {
    private String studentId;
    private String fullName;
    private String university;
    private String email;
    
    public Student(String studentId, String fullName, String university, String email) {
        setStudentId(studentId);
        setFullName(fullName);
        setUniversity(university);
        setEmail(email);
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        this.studentId = studentId;
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
    
    public String getUniversity() {
        return university;
    }
    
    public void setUniversity(String university) {
        if (university == null || university.trim().isEmpty()) {
            throw new IllegalArgumentException("University cannot be empty");
        }
        
        // Validate university is one of the allowed values
        if (!university.equals("ULK") && !university.equals("UR") && 
            !university.equals("AUCA") && !university.equals("UK")) {
            throw new IllegalArgumentException("University must be 'ULK', 'UR', 'AUCA', or 'UK'");
        }
        
        this.university = university;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        // Simple email validation
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        
        this.email = email;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, University: %s, Email: %s",
                            studentId, fullName, university, email);
    }
}

// Class: Supervisor (Encapsulation)
class Supervisor {
    private String supervisorId;
    private String fullName;
    private String qualification;
    private String email;
    
    public Supervisor(String supervisorId, String fullName, String qualification, String email) {
        setSupervisorId(supervisorId);
        setFullName(fullName);
        setQualification(qualification);
        setEmail(email);
    }
    
    public String getSupervisorId() {
        return supervisorId;
    }
    
    public void setSupervisorId(String supervisorId) {
        if (supervisorId == null || supervisorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Supervisor ID cannot be empty");
        }
        this.supervisorId = supervisorId;
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
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        if (qualification == null || qualification.trim().isEmpty()) {
            throw new IllegalArgumentException("Qualification cannot be empty");
        }
        
        // Validate qualification is one of the allowed values
        if (!qualification.equals("Bachelors") && !qualification.equals("Masters") && 
            !qualification.equals("PhD")) {
            throw new IllegalArgumentException("Qualification must be 'Bachelors', 'Masters', or 'PhD'");
        }
        
        this.qualification = qualification;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        // Simple email validation
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        
        this.email = email;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Qualification: %s, Email: %s",
                            supervisorId, fullName, qualification, email);
    }
}

// Class: Company (Encapsulation)
class Company {
    private String companyId;
    private String name;
    private String industryType;
    private String location;
    
    public Company(String companyId, String name, String industryType, String location) {
        setCompanyId(companyId);
        setName(name);
        setIndustryType(industryType);
        setLocation(location);
    }
    
    public String getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(String companyId) {
        if (companyId == null || companyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Company ID cannot be empty");
        }
        this.companyId = companyId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.name = name;
    }
    
    public String getIndustryType() {
        return industryType;
    }
    
    public void setIndustryType(String industryType) {
        if (industryType == null || industryType.trim().isEmpty()) {
            throw new IllegalArgumentException("Industry type cannot be empty");
        }
        
        // Validate industry type is one of the allowed values
        if (!industryType.equals("IT") && !industryType.equals("Finance") && 
            !industryType.equals("Health") && !industryType.equals("Education")) {
            throw new IllegalArgumentException("Industry type must be 'IT', 'Finance', 'Health', or 'Education'");
        }
        
        this.industryType = industryType;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Industry: %s, Location: %s",
                            companyId, name, industryType, location);
    }
}

// Main class for user interaction
public class InternshipManagementSystem {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final ArrayList<Supervisor> supervisors = new ArrayList<>();
    private static final ArrayList<Company> companies = new ArrayList<>();
    private static final ArrayList<Internship> internships = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        boolean exit = false;
        
        System.out.println("Welcome to Internship Management System");
        
        // Add some sample data for testing
        addSampleData();
        
        while (!exit) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        managementMenu("student");
                        break;
                    case 2:
                        managementMenu("supervisor");
                        break;
                    case 3:
                        managementMenu("company");
                        break;
                    case 4:
                        managementMenu("internship");
                        break;
                    case 5:
                        searchInternships();
                        break;
                    case 6:
                        generateReports();
                        break;
                    case 7:
                        exit = true;
                        System.out.println("Thank you for using Internship Management System.");
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
    
    // Method to add sample data for testing
    private static void addSampleData() {
        // Sample students
        students.add(new Student("S001", "John Doe", "ULK", "john.doe@ulk.ac.rw"));
        students.add(new Student("S002", "Jane Smith", "UR", "jane.smith@ur.ac.rw"));
        students.add(new Student("S003", "Alice Johnson", "AUCA", "alice@auca.ac.rw"));
        students.add(new Student("S004", "Bob Brown", "UK", "bob@uk.ac.rw"));
        
        // Sample supervisors
        supervisors.add(new Supervisor("SUP001", "Dr. Michael Chen", "PhD", "mchen@company.com"));
        supervisors.add(new Supervisor("SUP002", "Prof. Sarah Wilson", "Masters", "swilson@ur.ac.rw"));
        supervisors.add(new Supervisor("SUP003", "Mr. David Lee", "Bachelors", "dlee@tech.com"));
        supervisors.add(new Supervisor("SUP004", "Dr. Emily Taylor", "PhD", "etaylor@uk.ac.rw"));
        
        // Sample companies
        companies.add(new Company("C001", "TechInnovate", "IT", "Kigali"));
        companies.add(new Company("C002", "HealthPlus", "Health", "Butare"));
        companies.add(new Company("C003", "EduLearn", "Education", "Musanze"));
        
        // Sample internships will be created as needed
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== INTERNSHIP MANAGEMENT SYSTEM =====");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Supervisors");
        System.out.println("3. Manage Companies");
        System.out.println("4. Manage Internships");
        System.out.println("5. Search Internships");
        System.out.println("6. Generate Reports");
        System.out.println("7. Exit");
        System.out.println("=======================================");
    }
    
    private static void managementMenu(String entityType) {
        System.out.println("\n----- " + entityType.toUpperCase() + " MANAGEMENT -----");
        System.out.println("1. Register new " + entityType);
        System.out.println("2. View all " + entityType + "s");
        System.out.println("3. Back to main menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                registerNewEntity(entityType);
                break;
            case 2:
                viewAllEntities(entityType);
                break;
            case 3:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    
    private static void registerNewEntity(String entityType) {
        try {
            switch (entityType) {
                case "student":
                    registerStudent();
                    break;
                case "supervisor":
                    registerSupervisor();
                    break;
                case "company":
                    registerCompany();
                    break;
                case "internship":
                    registerInternship();
                    break;
                default:
                    System.out.println("Invalid entity type.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
    
    private static void viewAllEntities(String entityType) {
        System.out.println("\n----- " + entityType.toUpperCase() + " LIST -----");
        
        switch (entityType) {
            case "student":
                if (students.isEmpty()) {
                    System.out.println("No students registered yet.");
                } else {
                    for (int i = 0; i < students.size(); i++) {
                        System.out.println((i + 1) + ". " + students.get(i));
                    }
                }
                break;
                
            case "supervisor":
                if (supervisors.isEmpty()) {
                    System.out.println("No supervisors registered yet.");
                } else {
                    for (int i = 0; i < supervisors.size(); i++) {
                        System.out.println((i + 1) + ". " + supervisors.get(i));
                    }
                }
                break;
                
            case "company":
                if (companies.isEmpty()) {
                    System.out.println("No companies registered yet.");
                } else {
                    for (int i = 0; i < companies.size(); i++) {
                        System.out.println((i + 1) + ". " + companies.get(i));
                    }
                }
                break;
                
            case "internship":
                if (internships.isEmpty()) {
                    System.out.println("No internships registered yet.");
                } else {
                    for (int i = 0; i < internships.size(); i++) {
                        System.out.println("\nInternship #" + (i + 1) + ":");
                        System.out.println(internships.get(i));
                        System.out.println("-----------------------------");
                    }
                }
                break;
                
            default:
                System.out.println("Invalid entity type.");
        }
    }
    
    private static void registerStudent() {
        System.out.println("\n----- STUDENT REGISTRATION -----");
        
        String studentId = getUniqueStudentId();
        String fullName = getValidInput("Enter full name: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be empty");
            }
            return input;
        });
        
        System.out.println("Select university:");
        System.out.println("1. ULK");
        System.out.println("2. UR");
        System.out.println("3. AUCA");
        System.out.println("4. UK");
        
        int universityChoice = getIntInput("Enter choice: ");
        String university;
        switch (universityChoice) {
            case 1: university = "ULK"; break;
            case 2: university = "UR"; break;
            case 3: university = "AUCA"; break;
            case 4: university = "UK"; break;
            default:
                throw new IllegalArgumentException("Invalid university choice");
        }
        
        String email = getValidInput("Enter email: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!input.contains("@")) {
                throw new IllegalArgumentException("Email must contain '@'");
            }
            return input;
        });
        
        Student student = new Student(studentId, fullName, university, email);
        students.add(student);
        System.out.println("Student registered successfully!");
    }
    
    private static void registerSupervisor() {
        System.out.println("\n----- SUPERVISOR REGISTRATION -----");
        
        String supervisorId = getUniqueSupervisorId();
        String fullName = getValidInput("Enter full name: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be empty");
            }
            return input;
        });
        
        System.out.println("Select qualification:");
        System.out.println("1. Bachelors");
        System.out.println("2. Masters");
        System.out.println("3. PhD");
        
        int qualificationChoice = getIntInput("Enter choice: ");
        String qualification;
        switch (qualificationChoice) {
            case 1: qualification = "Bachelors"; break;
            case 2: qualification = "Masters"; break;
            case 3: qualification = "PhD"; break;
            default:
                throw new IllegalArgumentException("Invalid qualification choice");
        }
        
        String email = getValidInput("Enter email: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!input.contains("@")) {
                throw new IllegalArgumentException("Email must contain '@'");
            }
            return input;
        });
        
        Supervisor supervisor = new Supervisor(supervisorId, fullName, qualification, email);
        supervisors.add(supervisor);
        System.out.println("Supervisor registered successfully!");
    }
    
    private static void registerCompany() {
        System.out.println("\n----- COMPANY REGISTRATION -----");
        
        String companyId = getUniqueCompanyId();
        String name = getValidInput("Enter company name: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Company name cannot be empty");
            }
            return input;
        });
        
        System.out.println("Select industry type:");
        System.out.println("1. IT");
        System.out.println("2. Finance");
        System.out.println("3. Health");
        System.out.println("4. Education");
        
        int industryChoice = getIntInput("Enter choice: ");
        String industryType;
        switch (industryChoice) {
            case 1: industryType = "IT"; break;
            case 2: industryType = "Finance"; break;
            case 3: industryType = "Health"; break;
            case 4: industryType = "Education"; break;
            default:
                throw new IllegalArgumentException("Invalid industry choice");
        }
        
        String location = getValidInput("Enter location: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Location cannot be empty");
            }
            return input;
        });
        
        Company company = new Company(companyId, name, industryType, location);
        companies.add(company);
        System.out.println("Company registered successfully!");
    }
    
    private static void registerInternship() {
        if (students.isEmpty() || supervisors.isEmpty()) {
            System.out.println("Error: You need to register at least one student and one supervisor first.");
            return;
        }
        
        System.out.println("\n----- INTERNSHIP REGISTRATION -----");
        
        // Display available students
        System.out.println("\nAvailable Students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        
        int studentChoice = getIntInput("Select student (enter number): ");
        if (studentChoice < 1 || studentChoice > students.size()) {
            throw new IllegalArgumentException("Invalid student selection");
        }
        Student selectedStudent = students.get(studentChoice - 1);
        
        // Check if student already has an active internship
        if (hasActiveInternship(selectedStudent)) {
            throw new IllegalArgumentException("This student already has an active internship");
        }
        
        // Get internship type based on student's university or choose Remote
        System.out.println("\nSelect internship type:");
        System.out.println("1. " + selectedStudent.getUniversity() + " Internship");
        System.out.println("2. Remote Internship");
        
        int typeChoice = getIntInput("Enter choice: ");
        if (typeChoice != 1 && typeChoice != 2) {
            throw new IllegalArgumentException("Invalid internship type selection");
        }
        
        String internshipId = getUniqueInternshipId();
        
        // Get company
        String companyName;
        if (companies.isEmpty()) {
            companyName = getValidInput("Enter company name: ", input -> {
                if (input == null || input.trim().isEmpty()) {
                    throw new IllegalArgumentException("Company name cannot be empty");
                }
                return input;
            });
        } else {
            System.out.println("\nAvailable Companies:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.println((i + 1) + ". " + companies.get(i).getName());
            }
            
            int companyChoice = getIntInput("Select company (enter number): ");
            if (companyChoice < 1 || companyChoice > companies.size()) {
                throw new IllegalArgumentException("Invalid company selection");
            }
            companyName = companies.get(companyChoice - 1).getName();
        }
        
        // Display available supervisors
        System.out.println("\nAvailable Supervisors:");
        for (int i = 0; i < supervisors.size(); i++) {
            System.out.println((i + 1) + ". " + supervisors.get(i));
        }
        
        int supervisorChoice = getIntInput("Select primary supervisor (enter number): ");
        if (supervisorChoice < 1 || supervisorChoice > supervisors.size()) {
            throw new IllegalArgumentException("Invalid supervisor selection");
        }
        Supervisor selectedSupervisor = supervisors.get(supervisorChoice - 1);
        
        // Get dates
        LocalDate startDate = getValidDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = getValidDate("Enter end date (yyyy-MM-dd): ");
        
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        
        // Create internship based on type
        Internship internship = null;
        
        if (typeChoice == 1) { // University-specific internship
            String university = selectedStudent.getUniversity();
            
            switch (university) {
                case "ULK":
                    if (!selectedSupervisor.getQualification().equals("Masters") && 
                        !selectedSupervisor.getQualification().equals("PhD")) {
                        throw new IllegalArgumentException("ULK internships require supervisors with a Master's degree or higher");
                    }
                    
                    internship = new ULKInternship(
                        internshipId, selectedStudent, companyName, selectedSupervisor,
                        startDate, endDate, "PENDING");
                    break;
                    
                case "UR":
                    internship = new URInternship(
                        internshipId, selectedStudent, companyName, selectedSupervisor,
                        startDate, endDate, "PENDING");
                    
                    // Check if they want a secondary supervisor
                    boolean wantSecondary = getBooleanInput("Do you want to assign a secondary supervisor? (yes/no): ");
                    if (wantSecondary && supervisors.size() > 1) {
                        System.out.println("\nSelect secondary supervisor:");
                        for (int i = 0; i < supervisors.size(); i++) {
                            if (i != supervisorChoice - 1) { // Skip the primary supervisor
                                System.out.println((i + 1) + ". " + supervisors.get(i));
                            }
                        }
                        
                        int secondaryChoice = getIntInput("Enter choice: ");
                        if (secondaryChoice < 1 || secondaryChoice > supervisors.size() || 
                            secondaryChoice == supervisorChoice) {
                            throw new IllegalArgumentException("Invalid secondary supervisor selection");
                        }
                        
                        ((URInternship) internship).setSecondarySupervisor(supervisors.get(secondaryChoice - 1));
                    }
                    break;
                    
                case "AUCA":
                    internship = new AUCAInternship(
                        internshipId, selectedStudent, companyName, selectedSupervisor,
                        startDate, endDate, "PENDING");
                    break;
                    
                case "UK":
                    // For UK internship, we need a university supervisor too
                    System.out.println("\nSelect university supervisor:");
                    for (int i = 0; i < supervisors.size(); i++) {
                        if (i != supervisorChoice - 1) { // Skip the company supervisor
                            System.out.println((i + 1) + ". " + supervisors.get(i));
                        }
                    }
                    
                    int uniSupervisorChoice = getIntInput("Enter choice: ");
                    if (uniSupervisorChoice < 1 || uniSupervisorChoice > supervisors.size() || 
                        uniSupervisorChoice == supervisorChoice) {
                        throw new IllegalArgumentException("Invalid university supervisor selection");
                    }
                    
                    String englishCert = getValidInput("Enter English proficiency certification: ", input -> {
                        if (input == null || input.trim().isEmpty()) {
                            throw new IllegalArgumentException("English certification cannot be empty");
                        }
                        return input;
                    });
                    
                    internship = new UKInternship(
                        internshipId, selectedStudent, companyName, selectedSupervisor,
                        supervisors.get(uniSupervisorChoice - 1), startDate, endDate, 
                        "PENDING", englishCert);
                    break;
            }
        } else { // Remote internship
            String remoteUrl = getValidInput("Enter remote access URL: ", input -> {
                if (input == null || input.trim().isEmpty()) {
                    throw new IllegalArgumentException("Remote access URL cannot be empty");
                }
                return input;
            });
            
            internship = new RemoteInternship(
                internshipId, selectedStudent, companyName, selectedSupervisor,
                startDate, endDate, "PENDING", remoteUrl);
        }
        
        if (internship != null) {
            internships.add(internship);
            System.out.println("Internship registered successfully!");
        } else {
            System.out.println("Failed to create internship");
        }
    }
    
    private static void searchInternships() {
        if (internships.isEmpty()) {
            System.out.println("No internships registered yet.");
            return;
        }
        
        System.out.println("\n----- SEARCH INTERNSHIPS -----");
        System.out.println("1. Search by student name");
        System.out.println("2. Search by university");
        System.out.println("3. Back to main menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                searchInternshipsByStudent();
                break;
            case 2:
                searchInternshipsByUniversity();
                break;
            case 3:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    
    private static void searchInternshipsByStudent() {
        String searchTerm = getValidInput("Enter student name to search: ", input -> {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Search term cannot be empty");
            }
            return input.toLowerCase();
        });
        
        boolean found = false;
        System.out.println("\n----- SEARCH RESULTS -----");
        
        for (Internship internship : internships) {
            if (internship.getStudent().getFullName().toLowerCase().contains(searchTerm)) {
                System.out.println(internship);
                System.out.println("-------------------------");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No internships found for students with name containing '" + searchTerm + "'");
        }
    }
    
    private static void searchInternshipsByUniversity() {
        System.out.println("Select university:");
        System.out.println("1. ULK");
        System.out.println("2. UR");
        System.out.println("3. AUCA");
        System.out.println("4. UK");
        
        int universityChoice = getIntInput("Enter choice: ");
        String university;
        switch (universityChoice) {
            case 1: university = "ULK"; break;
            case 2: university = "UR"; break;
            case 3: university = "AUCA"; break;
            case 4: university = "UK"; break;
            default:
                throw new IllegalArgumentException("Invalid university choice");
        }
        
        boolean found = false;
        System.out.println("\n----- INTERNSHIPS FOR " + university + " -----");
        
        for (Internship internship : internships) {
            if (internship.getStudent().getUniversity().equals(university)) {
                System.out.println(internship);
                System.out.println("-------------------------");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No internships found for " + university + " students");
        }
    }
    
    private static void generateReports() {
        if (internships.isEmpty()) {
            System.out.println("No internships registered yet.");
            return;
        }
        
        System.out.println("\n----- GENERATE REPORTS -----");
        System.out.println("1. Generate reports for all internships");
        System.out.println("2. Generate report for specific internship");
        System.out.println("3. Track progress for an internship");
        System.out.println("4. Back to main menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                generateAllInternshipReports();
                break;
            case 2:
                generateSpecificInternshipReport();
                break;
            case 3:
                trackInternshipProgress();
                break;
            case 4:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    
    private static void generateAllInternshipReports() {
        System.out.println("\n----- ALL INTERNSHIP REPORTS -----");
        for (Internship internship : internships) {
            internship.generateDetailedReport();
            System.out.println();
        }
    }
    
    private static void generateSpecificInternshipReport() {
        // Display list of internships
        System.out.println("\nSelect internship to generate report:");
        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i + 1) + ". " + internship.getInternshipId() + " - " + 
                              internship.getStudent().getFullName() + " at " + internship.getCompanyName());
        }
        
        int choice = getIntInput("Enter choice: ");
        if (choice < 1 || choice > internships.size()) {
            throw new IllegalArgumentException("Invalid internship selection");
        }
        
        Internship selectedInternship = internships.get(choice - 1);
        selectedInternship.generateDetailedReport();
    }
    
    private static void trackInternshipProgress() {
        // Display list of internships
        System.out.println("\nSelect internship to track progress:");
        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i + 1) + ". " + internship.getInternshipId() + " - " + 
                              internship.getStudent().getFullName() + " at " + internship.getCompanyName());
        }
        
        int choice = getIntInput("Enter choice: ");
        if (choice < 1 || choice > internships.size()) {
            throw new IllegalArgumentException("Invalid internship selection");
        }
        
        Internship selectedInternship = internships.get(choice - 1);
        selectedInternship.trackProgress();
    }
    
    // Helper methods for input validation
    
    private static String getUniqueStudentId() {
        while (true) {
            try {
                String studentId = getValidInput("Enter student ID: ", input -> {
                    if (input == null || input.trim().isEmpty()) {
                        throw new IllegalArgumentException("Student ID cannot be empty");
                    }
                    return input;
                });
                
                // Check for duplicate IDs
                for (Student s : students) {
                    if (s.getStudentId().equalsIgnoreCase(studentId)) {
                        throw new IllegalArgumentException("Student ID already exists. Please enter a unique ID.");
                    }
                }
                
                return studentId;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String getUniqueSupervisorId() {
        while (true) {
            try {
                String supervisorId = getValidInput("Enter supervisor ID: ", input -> {
                    if (input == null || input.trim().isEmpty()) {
                        throw new IllegalArgumentException("Supervisor ID cannot be empty");
                    }
                    return input;
                });
                
                // Check for duplicate IDs
                for (Supervisor s : supervisors) {
                    if (s.getSupervisorId().equalsIgnoreCase(supervisorId)) {
                        throw new IllegalArgumentException("Supervisor ID already exists. Please enter a unique ID.");
                    }
                }
                
                return supervisorId;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String getUniqueCompanyId() {
        while (true) {
            try {
                String companyId = getValidInput("Enter company ID: ", input -> {
                    if (input == null || input.trim().isEmpty()) {
                        throw new IllegalArgumentException("Company ID cannot be empty");
                    }
                    return input;
                });
                
                // Check for duplicate IDs
                for (Company c : companies) {
                    if (c.getCompanyId().equalsIgnoreCase(companyId)) {
                        throw new IllegalArgumentException("Company ID already exists. Please enter a unique ID.");
                    }
                }
                
                return companyId;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String getUniqueInternshipId() {
        while (true) {
            try {
                String internshipId = getValidInput("Enter internship ID: ", input -> {
                    if (input == null || input.trim().isEmpty()) {
                        throw new IllegalArgumentException("Internship ID cannot be empty");
                    }
                    return input;
                });
                
                // Check for duplicate IDs
                for (Internship i : internships) {
                    if (i.getInternshipId().equalsIgnoreCase(internshipId)) {
                        throw new IllegalArgumentException("Internship ID already exists. Please enter a unique ID.");
                    }
                }
                
                return internshipId;
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
                System.out.println("Error: Invalid number. Please enter a valid integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static LocalDate getValidDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Date cannot be empty");
                }
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use yyyy-MM-dd format.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
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
    
    private static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("yes") || input.equals("y") || input.equals("1")) {
                return true;
            } else if (input.equals("false") || input.equals("no") || input.equals("n") || input.equals("0")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
    
    private static boolean hasActiveInternship(Student student) {
        for (Internship internship : internships) {
            if (internship.getStudent().getStudentId().equals(student.getStudentId()) && 
                (internship.getStatus().equals("PENDING") || internship.getStatus().equals("ONGOING"))) {
                return true;
            }
        }
        return false;
    }
}