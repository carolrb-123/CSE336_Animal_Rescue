# Animal Management — CRUD Lookup Table

**Branch:** `main`  
**Commit:** `8c351f0077d49d4523567ec7867c6953bba5bdfb`

| User operation in Animal screen | Controller action | Model | Repository call | C | R | U | D |
|---|---|---|---|:---:|:---:|:---:|:---:|
| Add animal | `AnimalController.addAnimal()` | `Animal` | `save(animal)` | ✓ | | | |
| Display all animals | `refreshTable()` | `Animal` | `findAll()` | | ✓ | | |
| Load/search selected animal by ID | `loadSelectedAnimal()` / update/delete validation | `Animal` | `findById(id)` | | ✓ | | |
| Edit animal | `updateAnimal()` | `Animal` | `save(updatedAnimal)` | | | ✓ | |
| Change adoption availability | `updateAnimal()` | `Animal` | `save(updatedAnimal)` | | | ✓ | |
| Delete animal | `deleteAnimal()` | `Animal` | `delete(id)` | | | | ✓ |

**Source-specific note:** The current `Animal` model has `id`, `name`, `species`, `ageYears`, `ageMonths`, `healthStatus`, `availableForAdoption`, and `description`. Species and health status are selected from combo boxes, but the model stores them as strings. `LookupTable<T>` is a generic repository implementation, not a dedicated editable lookup-data screen.
