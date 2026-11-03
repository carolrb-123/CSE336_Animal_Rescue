# Client Basic Requirements
## Animal Rescue Management System

The following user stories describe the basic requirements from the perspective of the people who would use the system.

### 1. Animal Management

**US-01 — Register an Animal**
As a rescue staff member, I want to add a new animal record with its name, species, age, health status, and description, so that the organization can keep track of every rescued animal.

**US-02 — Update Animal Information**
As a rescue staff member, I want to update an animal's information and health status, so that the records remain accurate and up to date.

**US-03 — View Animal Records**
As a rescue staff member, I want to view the list of registered animals and their details, so that I can easily monitor the animals under the organization's care.

**US-04 — Manage Adoption Availability**
As a rescue staff member, I want to change an animal's adoption availability, so that the organization knows which animals are ready for adoption.

**US-05 — Delete an Animal**
As a rescue staff member, I want to delete an animal record, so that incorrect or obsolete records do not clutter the system.

### 2. Adopter Management

**US-06 — Register an Adopter**
As a rescue staff member, I want to register people interested in adopting and store their contact information, so that the organization maintains an organized record of potential adopters.

**US-07 — Manage Adopter Records**
As a rescue staff member, I want to view, update, and delete adopter records, so that adopter information stays organized and accurate.

### 3. Donation Management

**US-08 — Record Donations**
As a donation manager, I want to record donations and their details, so that the organization can keep track of the support it receives.

**US-09 — Support Different Donation Types**
As a donation manager, I want to record online, cheque, and in-kind donations, so that the system can handle different forms of contributions.

**US-10 — Add Optional Donation Details**
As a donation manager, I want to mark a donation as recurring or as "in memory of" a named person, so that donations can reflect donor preferences.

### 4. Volunteer Management

**US-11 — Register Volunteers with Skills, Availability, and Certificates**
As a volunteer coordinator, I want to register volunteers with their contact details, skills, availability, and certificates, so that the organization has a complete record of the people willing to help.

**US-12 — Match Volunteers by Skills or Availability**
As a volunteer coordinator, I want to match volunteers by skills or by availability, so that I can quickly find suitable people for a rescue activity.

**US-13 — Track Completed Volunteer Hours**
As a volunteer coordinator, I want to track the hours each volunteer has completed, so that the organization can recognize contributions and report on volunteer effort.

**US-14 — Protect Volunteers with Registrations**
As a volunteer coordinator, I want the system to prevent deleting a volunteer who still has event registrations, so that registration records never point to a missing volunteer.

### 5. Rescue Event Management

**US-15 — Create and Manage Events**
As an event coordinator, I want to create and update rescue events, including date, location, required skills, and hours, so that the organization can plan its activities.

**US-16 — Register Volunteers for Events**
As an event coordinator, I want to register volunteers for specific events, so that I know who is participating in each activity.

**US-17 — Prevent Duplicate Registrations**
As an event coordinator, I want the system to reject a second registration of the same volunteer for the same event, so that participant lists and volunteer hours are not counted twice.

**US-18 — Protect Events with Registrations**
As an event coordinator, I want the system to prevent deleting an event that still has volunteer registrations, so that registration records are never left without their event.

### 6. Communication Management

**US-19 — Send Notifications Through Different Channels**
As a rescue staff member, I want to send messages by email, SMS, or social media post, so that I can reach each recipient through an appropriate channel.

**US-20 — Review Sent Messages**
As a rescue staff member, I want to see a history of sent messages, so that I know what has already been communicated.

**US-21 — Respond to Important Activities**
As a rescue coordinator, I want observer components that respond to animal-status changes, donations, and event updates, so that important activities can trigger notifications. (Observer classes exist; end-to-end triggering from all controllers must be verified before it is claimed.)

### 7. User Management

**US-22 — Manage System Users**
As a system administrator, I want to add, view, update, and delete user records, so that the organization can maintain its system users in an organized way.

### 8. Control Panel

**US-23 — Access All Modules from One Place**
As a staff member, I want a main control panel that opens each management module, so that I can move between animals, adopters, donations, volunteers, events, communication, and users without searching for them.
