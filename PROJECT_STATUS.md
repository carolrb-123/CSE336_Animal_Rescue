# Animal Rescue Management System

## Project Overview

This project is a Java 17 Swing application for managing an animal-rescue organization. It provides separate management screens for:

- Animals and adopters
- Donors and donations
- Communication with donors, volunteers, and adopters
- Volunteers and volunteer matching
- Events and volunteer registrations

The application demonstrates software design patterns in a realistic domain while following MVC. Data is currently stored in memory behind repository interfaces so that persistence can be replaced later without rewriting controllers or views.

## Phase 1 Team Scope

| Member | Responsibility | Main patterns/features |
|---|---|---|
| A | Technical architecture and integration | MVC, Singleton, shared interfaces, control panel |
| B | Donations | Donation and donor management, Factory, Decorator |
| C | Communication | Notification channels, Strategy, Observer |
| D | Volunteers and events | Volunteer CRUD, event CRUD, registrations, volunteer-matching Strategy |
| E | Animals and lookup tables | Animal/adopter management and generic lookup CRUD |
| F | Documentation and QA | Class diagram, client requirements, tests, README, merge checks |

## Member D Scope

Member D owns the volunteer and event-management area:

### Volunteer management

- Create, update, list, and delete volunteers
- Automatically assign unique volunteer IDs
- Store:
  - Name
  - Email and phone
  - One or more predefined skills
  - One or more predefined availability slots
  - Certificates
- Completed hours are calculated automatically from completed event registrations.
- Prevent deleting a volunteer who still has event registrations

### Defined volunteer skills

Volunteers choose one or more of these controlled options:

1. Basic Handling
2. Advanced Behavior & Fear Handling
3. Medication Administration
4. Grooming
5. Animal Transport & Driving
6. Adoption Matchmaking & Counseling

### Defined availability slots

Volunteers choose one or more of these controlled time slots:

1. Morning (AM) — 8:00 AM - 12:00 PM
2. Midday / Afternoon (PM) — 12:00 PM - 4:00 PM
3. Evening (EVE) — 4:00 PM - 8:00 PM

### Event management

- Create, update, list, and delete rescue events
- Automatically assign unique event IDs
- Store event name, type, date, location, required skills, description, and duration in hours
- Restrict event types to Adoption, Fundraising, or Vaccine
- Select required skills from the same controlled skill list used by volunteers
- Prevent deleting an event that still has volunteer registrations

### Volunteer registrations

- Register volunteers for events
- Update registration status
- Delete registrations
- Prevent duplicate volunteer/event registrations
- Keep event and volunteer selections connected to the repository data

### Volunteer matching Strategy

The Volunteer Management page includes a matching tab. It uses Strategy implementations to find volunteers by:

- A selected skill
- A selected availability slot

A volunteer matches an availability query if they selected that slot, even when they have other availability slots too.

## Current GUI Structure

The homepage has separate routes for:

- **Volunteer Management**
  - Volunteers
  - Volunteer Matching
- **Event Management**
  - Events
  - Volunteer Registrations

The Member D pages use the same green themed form cards, tables, and CRUD button layout as the existing animal and donation pages.

## Implementation Status

### Completed

- Member D domain models:
  - `Volunteer`
  - `RescueEvent`
  - `EventRegistration`
- In-memory repositories for all Member D entities
- Automatic, non-reused volunteer and event ID allocation
- Volunteer and event MVC controllers and views
- Volunteer CRUD
- Event CRUD
- Registration CRUD
- Skill and availability selection controls
- Skill and availability matching Strategies
- Homepage integration
- Deletion safeguards for registered volunteers/events
- Focused tests for matching and ID allocation

### Current branch

The work is on:

```text
feature/volunteer
```

The branch has been pushed to GitHub. The latest pushed commit is the previous UI/ID update. The latest multi-availability changes are currently present locally but are not committed yet.

### Validation

The latest test run passed:

```text
25 tests, 0 failures
```

Validation command:

```bash
mvn test
```

## Running the Application

From the repository root:

```bash
mvn test
```

To run the GUI in VS Code, open [`src/main/java/com/rescue/Main.java`](src/main/java/com/rescue/Main.java) and select **Run** above the `main` method. The application opens the control panel, where the Volunteer Management and Event Management pages can be tested.

## Phase 2 Extension Hooks

The current structure leaves room for later patterns and features:

- Repository interfaces can support a database or Proxy
- Controllers can be wrapped by Commands
- Notification services can act as a Facade
- Animal status can support State
- Shared abstractions can support Template Method and Adapter
- Repository collections can support Iterator
