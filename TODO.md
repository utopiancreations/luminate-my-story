# Initial Development Tasks for Luminate Shared Core

This task list is based on the project blueprint in `README.md`.

## Phase 1: Data Model Implementation

- [ ] Implement all data schemas (`Story`, `Chapter`, `Scene`, etc.) as Realm objects in the `/shared/src/commonMain/kotlin/schemas/` directory, following the specification in Section 3 of the README.
- [ ] Ensure correct relationships (One-to-Many, One-to-One) are defined in the Realm models.
- [ ] Set up the singleton `AppSettings` object.

## Phase 2: Persistence & Story Management

- [ ] Implement the `PersistenceLayer.kt` API to handle basic CRUD (Create, Read, Update, Delete) operations for all Realm schemas.
- [ ] Implement the public API for `StoryManager.kt`, using the `PersistenceLayer` for data operations. Test all functions thoroughly.

## Phase 3: AI Logic Porting & Prompts

- [ ] In `AIOrchestrator.kt`, create a new object or file to hold the system prompts.
- [ ] Port the prompt strings from `/reference_poc_python/prompts.py` into this new file, translating them into Kotlin constants.