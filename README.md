Luminate My Story: Shared Core Technical Documentation

Version: 1.1
Date: October 19, 2025
Authors: Co-Product Managers

1. Core Mission & Guiding Principles

1.1. Mission

To empower everyone to capture and craft their authentic life stories with a compassionate, intelligent, and private creative partner.

1.2. AI Name

The AI assistant will be referred to as Lumi.

1.3. Guiding Principles

The User is the Author: The app removes technical friction so the user can focus on their narrative. We provide structure, not creative direction.

Privacy by Default: All user data is stored locally on their device. Cloud storage or sharing is an explicit, opt-in choice controlled entirely by the user.

Compassion in Interaction: Every interaction, especially when handling sensitive memories, must be designed with empathy and validation.

Seamless & Adaptive: The user experience must adapt to the user's context, whether they are speaking on a walk or typing discreetly.

2. System Architecture & Database

2.1. Architecture: Hybrid "Shared Core"

The application will use a hybrid architecture to ensure native performance and development efficiency.

Native UI Layer: Thin, platform-specific UI (SwiftUI for Apple, Jetpack Compose for Android/Desktop).

Unified Business Logic (Shared Core): A single, cross-platform library (Kotlin Multiplatform or Rust) containing all application logic.

AI Core: Managed by the Shared Core, with performance-critical tasks delegated to native libraries (Core ML, NNAPI, etc.).

2.2. Local Database Technology

For local persistence, we will use Realm.

Rationale:

Cross-Platform: Realm has excellent support for Kotlin Multiplatform, Swift, and Java/Kotlin, making it a perfect fit for our Shared Core architecture.

High-Performance: It's a mobile-first database designed for speed and efficiency, which is crucial for a responsive user experience.

Object-Oriented: Realm allows us to work with native objects directly, eliminating the need for ORM boilerplate and making the code cleaner.

Reactive: It supports live objects and reactive queries, which will make updating the UI in response to data changes seamless.

3. Data Schemas

These schemas define the structure of the objects that will be stored in the Realm database.

3.1. AppSettings Schema (Singleton Object)

A single object to store user-configurable settings.

{
  "id": "singleton_settings",
  "enableEmotionalCheckins": "boolean",
  "defaultInputMode": "string", // "Voice", "Text"
  "ttsVoice": "string", // Identifier for the selected Text-to-Speech voice
  "defaultExportFormat": "string", // "TXT", "MD", "PDF"
  "cloudSyncProvider": "string | null", // "iCloud", "GoogleDrive", null
  "cloudSyncEnabled": "boolean"
}


3.2. Story Schema

The top-level object for a user's narrative.

{
  "id": "string (UUID)",
  "title": "string",
  "chapters": "List<Chapter>", // One-to-Many relationship
  "visibility": "string", // "private" (default), "shared_anonymously"
  "createdAt": "timestamp",
  "lastModified": "timestamp"
}


3.3. Chapter Schema

A container for a collection of scenes.

{
  "id": "string (UUID)",
  "title": "string",
  "scenes": "List<Scene>", // One-to-Many relationship
  "createdAt": "timestamp",
  "lastModified": "timestamp"
}


3.4. Scene Schema

A single, distinct scene within a Chapter. This is the primary unit for interviews and drafting.

{
    "id": "string (UUID)",
    "title": "string | null",
    "order": "integer",
    "interviewData": "InterviewData | null",
    "draftText": "string",
    "createdAt": "timestamp",
    "lastModified": "timestamp"
}


3.5. InterviewData Schema

A container for the question and answer pairs of a scene's interview.

{
  "id": "string (UUID)",
  "qaPairs": "List<QAPair>", // One-to-Many relationship
  "completedAt": "timestamp"
}


3.6. QAPair Schema

A single question and answer pair.

{
  "id": "string (UUID)",
  "question": "string",
  "answer": "string",
  "timestamp": "timestamp"
}


3.7. SessionState Schema

An in-memory (and auto-saved) object to manage the live state of an active session.

{
  "activeStoryId": "string | null",
  "activeChapterId": "string | null",
  "activeSceneId": "string | null",
  "currentInputMode": "string", // "Voice", "Text"
  "partialUserInput": "string", // Stores text as it's being typed or transcribed
  "lastLumiResponse": "string",
  "isAwaitingUserInput": "boolean"
}


4. Shared Core Module API Specification

This section defines the public API that the native UI layers will use to interact with the Shared Core.

4.1. StoryManager API

getStoriesList(): List<Story>: Returns a list of all stories, sorted by lastModified descending.

createStory(title: String): Story: Creates a new Story object, persists it, and returns the created object.

getStory(id: String): Story?: Retrieves a specific story by its ID.

updateStoryVisibility(storyId: String, visibility: String): void: Sets the story's visibility to 'private' or 'shared_anonymously'.

addChapter(storyId: String, title: String): Chapter: Adds a new chapter to a story.

addScene(chapterId: String, title: String?): Scene: Adds a new, empty scene to a chapter.

updateSceneDraft(sceneId: String, draft: String): void: Updates the draftText for a specific scene.

deleteStory(id: String): void

deleteChapter(id: String): void

deleteScene(id: String): void

4.2. SessionManager API

startSession(storyId: String, chapterId: String, sceneId: String?): SessionState: Initializes a creative session for a specific scene.

handleUserInput(input: String): SessionState: Processes user text/transcription. Triggers the AIOrchestrator and returns the new SessionState.

toggleInputMode(): SessionState: Switches between 'Voice' and 'Text' and returns the updated state.

pauseSession(): void: Safely ends the current session, ensuring all data is saved.

getCurrentState(): SessionState: Retrieves the last known session state on app launch.

4.3. AIOrchestrator API (Internal, used by SessionManager)

processNewTopic(topic: String): String: Generates an initial, empathetic question based on a new topic from the user.

generateInterviewQuestion(context: String): String: Generates the next interview question based on the conversation history.

generateDraft(interviewData: InterviewData): String: Takes completed interview data and generates a draft for a scene.

anonymizeText(text: String): String: Processes text to replace PII with generic placeholders.

analyzeForContext(text: String, localStories: List<Story>): String?: Performs local RAG to find connections within the user's existing stories.

4.4. PersistenceLayer API

exportStory(storyId: String, format: ExportFormat): File: Compiles a full story into the specified format (TXT, MD, PDF).

exportChapter(chapterId: String, format: ExportFormat): File: Compiles a full chapter into the specified format.

getAppSettings(): AppSettings

updateAppSettings(settings: AppSettings): void

initCloudSync(provider: CloudProvider): void: Initiates the connection and synchronization with the user's chosen cloud service.