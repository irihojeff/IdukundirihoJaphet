# Advanced Java OOP Solutions Repository

This repository contains three comprehensive Java solutions that demonstrate advanced Object-Oriented Programming principles. Each solution implements a different management system with a focus on inheritance, polymorphism, encapsulation, and abstraction.

## Table of Contents

1. [Overview](#overview)
2. [Solution 1: Vehicle Tax Management System](#solution-1-vehicle-tax-management-system)
3. [Solution 2: Tax Enforcement Management System](#solution-2-tax-enforcement-management-system)
4. [Solution 3: Internship Management System](#solution-3-internship-management-system)
5. [Common Design Patterns](#common-design-patterns)
6. [How to Run](#how-to-run)
7. [Requirements](#requirements)

## Overview

These solutions were developed as answers to advanced Java programming assignments. Each solution demonstrates:

- Proper class hierarchy with abstract base classes
- Implementation of interfaces
- Extensive validation and error handling
- Menu-driven user interfaces with Scanner
- Application of all four OOP principles

## Solution 1: Vehicle Tax Management System

### Description
A system for a government authority to register vehicles, calculate annual tax based on vehicle types, and generate tax reports.

### Key Components
- **Abstract Vehicle Class**: Base class with common attributes and abstract methods
- **Concrete Classes**:
  - Car: With special tax rules for electric vehicles
  - Truck: With load capacity-based taxation
  - Motorcycle: With engine capacity considerations
  - Bus: With passenger capacity scaling
  - SUV: With four-wheel drive tax adjustments
- **Features**:
  - Registration of different vehicle types
  - Tax calculation based on multiple factors
  - Age-based tax adjustments
  - Detailed tax reporting

### OOP Principles Applied
- **Abstraction**: Abstract Vehicle class with abstract methods
- **Inheritance**: Concrete vehicle classes extending the base class
- **Encapsulation**: Private fields with accessor methods and validation
- **Polymorphism**: Different implementations for tax calculation and reporting

## Solution 2: Tax Enforcement Management System

### Description
A Rwanda Revenue Authority (RRA) system for managing tax declarations, calculating tax amounts, enforcing compliance, and conducting audits.

### Key Components
- **Abstract TaxDeclaration Class**: Base class implementing multiple interfaces
- **Concrete Classes**:
  - PAYEDeclaration: For salary-based taxation
  - VATDeclaration: For value-added tax
  - WithholdingTaxDeclaration: For various withholding categories
- **Support Classes**:
  - Taxpayer: For tracking compliance and declarations
  - TaxOfficer: For conducting audits
- **Features**:
  - Progressive tax calculation
  - Compliance enforcement with penalties
  - Audit functionality
  - Detailed receipt generation

### OOP Principles Applied
- **Abstraction**: Abstract tax class and interfaces
- **Inheritance**: Specific tax types extending base class
- **Encapsulation**: Protected data with validation
- **Polymorphism**: Different tax calculations and enforcement strategies
- **Interface Implementation**: Multiple interfaces for different behaviors

## Solution 3: Internship Management System

### Description
A system for managing and monitoring internship placements for students from different universities (ULK, UR, AUCA, and UK).

### Key Components
- **Abstract Internship Class**: Base class with common functionality
- **Concrete Classes**:
  - ULKInternship: With specific validation rules
  - URInternship: With dual supervisor support
  - AUCAInternship: With community service tracking
  - UKInternship: With dual supervision and language requirements
  - RemoteInternship: With communication tracking
- **Support Classes**:
  - Student: With university validation
  - Supervisor: With qualification tracking
  - Company: With industry categorization
- **Features**:
  - University-specific internship rules
  - Progress tracking
  - Status monitoring
  - Report generation

### OOP Principles Applied
- **Abstraction**: Abstract base class with abstract methods
- **Inheritance**: University-specific internship types
- **Encapsulation**: Private fields with validation
- **Polymorphism**: Different implementations for progress tracking and reporting
- **Interface Implementation**: Reportable interface

## Common Design Patterns

All three solutions share these design patterns:

1. **Template Method Pattern**: Using abstract base classes that define the skeleton of operations
2. **Strategy Pattern**: Different calculation strategies for different entity types
3. **Composite Pattern**: Managing collections of related objects
4. **Command Pattern**: In the menu-driven user interface structure

## How to Run

1. Ensure you have Java JDK 8 or higher installed
2. Compile the main class for each solution:
   ```
   javac VehicleTaxManagementSystem.java
   javac TaxEnforcementManagementSystem.java
   javac InternshipManagementSystem.java
   ```
3. Run the compiled class:
   ```
   java VehicleTaxManagementSystem
   java TaxEnforcementManagementSystem
   java InternshipManagementSystem
   ```

## Requirements

- Java JDK 8 or higher
- Console/Terminal for running the applications
- Basic understanding of Java OOP concepts to understand the code

---

© 2025 Advanced Java Programming Solutions
