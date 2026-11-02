# Animal Rescue Management System

**Course:** Software Design Patterns (CSE336)

**Phase:** 1 (Mid-Term Submission)

## 📌 Project Overview

The Animal Rescue Management System is a Java-based desktop application designed to streamline the operations of a non-profit animal shelter. It handles the management of animals (beneficiaries), adopters, donations, volunteers, events, and automated communications.

The system is built with a strict adherence to **Object-Oriented Design**, the **MVC Architecture**, and leverages **Gang of Four (GoF) Design Patterns** to ensure a flexible, maintainable, and highly decoupled codebase.

## 🏗️ Architecture & Technology Stack

* **Language:** Java 17

* **Build Tool:** Maven

* **UI Framework:** Java Swing

* **Architecture:** Strict Model-View-Controller (MVC)

* **Persistence (Phase 1):** Shared In-Memory Storage (`DataStore` Singleton) via Generic `LookupTable<T>` repositories.

## 🧩 Design Patterns Implemented (Phase 1 Rubric)

Our team successfully implemented the 5 required design patterns for Phase 1. Below is the mapping of where the TA can find each pattern in the codebase:

| Pattern | Marks | Package / Classes | Purpose in Domain | 
| ----- | ----- | ----- | ----- | 
| **Singleton** | 0.5 | `model.DataStore`, `model.AppConfig` | Provides a single shared memory store for all repositories and global application settings. | 
| **Factory** | 0.5 | `factory.DonationFactory` | Abstracts the instantiation of `OnlineDonation`, `ChequeDonation`, and `InKindDonation` based on user UI selection. | 
| **Decorator** | 1.0 | `model.DonationDecorator` (Base)  `model.InMemoryOfDonation`  `model.RecurringDonation` | Dynamically stacks behaviors onto donations at runtime (e.g., adding memorial text or recurring status). | 
| **Strategy** | 1.0 | `service.strategy.*` | **1. Communications:** Swaps channels (Email/SMS/Social) based on recipient type.  **2. Volunteers:** Swaps matching algorithms (Skill vs. Availability). | 
| **Observer** | 1.0 | `service.Subject`, `service.Observer`  `service.observer.*` | Decouples modules. A new donation or animal status change automatically notifies the Dashboard and Communication modules. | 

## 📂 Core Modules (MVC)

The application is divided into functional modules, all accessible via the central **User Control Panel**:

1. **Infrastructure & Control Panel (User CRUD):** Manages role-based routing and system user accounts.

2. **Beneficiary Management (Animals & Adopters):** Tracks shelter intake, medical status, and adopter profiles utilizing generic CRUD lookups.

3. **Donation Management:** Handles donor data, donation types, and dynamically generated receipts.

4. **Volunteer & Event Management:** Tracks volunteer hours/skills, event registration, and performs dynamic volunteer matching.

5. **Communication Center:** An underlying service module that broadcasts notifications via Email, SMS, or Social Posts based on user triggers.

## 🚀 How to Run

1. Ensure **Java 17** and **Maven** are installed.

2. Clone the repository and navigate to the root directory.

3. Compile the project:

   ```
   mvn clean compile
   
   ```

4. Run the application by executing the `Main.java` class located at:
   `src/main/java/com/rescue/Main.java`
   *(Alternatively, run it via your IDE by right-clicking `Main.java` -> Run)*

## 👥 Team Contributions (Phase 1)

* **Member A (Tech Lead):** Infrastructure, Singletons (`DataStore`/`AppConfig`), User Control Panel (CRUD), Final UI Integration.

* **Member B:** Donation Management (Factory & Decorator Patterns).

* **Member C:** Communication Module (Strategy & Observer Patterns).

* **Member D:** Volunteer & Event Management (Volunteer Matching Strategy).

* **Member E:** Beneficiary Management (Animals & Adopters) & Generic CRUD Lookup Tables.

* **Member F:** Documentation, QA, LMS Client Requirements, and Class Diagram Generation.

*Developed for CSE336 Phase 1.*