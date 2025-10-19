# Luminate My Story: Shared Core Technical Documentation

## Project Overview

Luminate My Story is a Kotlin Multiplatform Mobile (KMM) application designed to transform personal interviews into compelling narrative stories using AI-powered content generation. The application features a shared core logic that runs on both Android and iOS platforms, with a unified data model and business logic.

## Architecture

### Kotlin Multiplatform Structure

```
LuminateMyStory/
├── shared/                 # Shared Kotlin code
│   └── src/
│       ├── commonMain/     # Platform-agnostic code
│       ├── androidMain/    # Android-specific implementations
│       └── iosMain/        # iOS-specific implementations
├── androidApp/             # Android application
└── iosApp/                 # iOS application
```

### Shared Core Modules

The shared core is organized into several key modules:

#### 1. AIOrchestrator.kt
Manages AI interactions and coordinates content generation based on interview data. This module will interface with various AI services to transform raw interview content into structured narrative elements.

#### 2. PersistenceLayer.kt
Provides a unified interface for all data persistence operations using Realm Kotlin Multiplatform. Handles CRUD operations for all data models and maintains data consistency across platforms.

#### 3. SessionManager.kt
Manages user sessions, authentication, and maintains application state throughout the user's interaction with the app.

#### 4. StoryManager.kt
Provides the public API for story-related operations, serving as the main interface between the UI layers and the underlying data/AI systems.

## Data Models (Schemas)

The application uses Realm Kotlin Multiplatform for data persistence with the following schema structure:

### Core Entities

#### Story
The root entity representing a complete narrative story.
- Contains metadata about the story
- Has One-to-Many relationship with Chapters
- Links to source InterviewData

#### Chapter
Represents logical divisions within a story.
- Contains chapter-specific metadata
- Has One-to-Many relationship with Scenes
- Belongs to a single Story

#### Scene
The smallest unit of narrative content.
- Contains actual story text and dialogue
- Belongs to a single Chapter
- May reference specific QAPairs from interviews

#### InterviewData
Raw data collected from user interviews.
- Stores audio transcriptions
- Contains metadata about interview sessions
- Links to generated QAPairs

#### QAPair
Structured question-answer pairs extracted from interviews.
- Contains question text and user responses
- Used as source material for story generation
- Links back to source InterviewData

#### SessionState
Manages current user session information.
- Tracks user progress
- Stores temporary preferences
- Maintains application state

#### AppSettings
Singleton configuration object for application-wide settings.

## Dependencies

### Realm Kotlin Multiplatform
- **Version**: 1.11.0
- **Purpose**: Cross-platform database solution
- **Usage**: Primary data persistence layer

### Kotlin Multiplatform
- **Version**: 1.9.20
- **Purpose**: Shared code between Android and iOS

### Android Dependencies
- Jetpack Compose for UI
- Material Design 3
- Activity Compose

## Development Phases

### Phase 1: Data Model Implementation
- Implement all data schemas as Realm objects
- Define proper relationships (One-to-Many, One-to-One)
- Set up the singleton AppSettings object
- Ensure data model integrity and proper migrations

### Phase 2: Persistence & Story Management
- Implement PersistenceLayer CRUD operations
- Create StoryManager public API
- Implement data validation and error handling
- Add comprehensive testing for data operations

### Phase 3: AI Logic Integration
- Port AI prompts from Python POC to Kotlin
- Implement AIOrchestrator functionality
- Integrate with external AI services
- Add content generation workflows

### Phase 4: Session Management
- Implement user authentication
- Add session state persistence
- Implement progress tracking
- Add user preference management

## Getting Started

### Prerequisites
- Android Studio or IntelliJ IDEA with Kotlin Multiplatform plugin
- Xcode (for iOS development)
- JDK 17 or higher

### Setup
1. Clone the repository
2. Open the project in your IDE
3. Sync Gradle dependencies
4. Build the shared module
5. Run the Android or iOS application

### Building
```bash
# Build shared module
./gradlew :shared:build

# Build Android app
./gradlew :androidApp:build

# Build iOS framework
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

## Reference Implementation

The `reference_poc_python/` directory contains the original Python proof-of-concept implementation with the following files:
- `llm_handler.py` - AI service integration logic
- `prompts.py` - System prompts for AI content generation
- `project_manager.py` - Project management utilities
- `ghostwriter.py` - Main application logic

These files serve as reference for implementing the Kotlin multiplatform version and should not be included in the final build.

## Contributing

When contributing to this project:
1. Ensure all shared code is platform-agnostic
2. Follow Kotlin coding conventions
3. Add comprehensive tests for new functionality
4. Update documentation for API changes
5. Test on both Android and iOS platforms

## License

[To be determined]

---

*This documentation will be updated as the project evolves and new features are implemented.*