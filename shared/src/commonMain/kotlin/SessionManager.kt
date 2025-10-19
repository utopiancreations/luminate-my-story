package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.SessionState
import io.realm.kotlin.types.RealmUUID

/**
 * Session Manager
 *
 * This module manages user sessions, handles authentication,
 * and maintains session state throughout the application lifecycle.
 *
 * @property storyManager The manager for story-related operations.
 * @property aiOrchestrator The orchestrator for AI interactions.
 */
class SessionManager(
    private val storyManager: StoryManager,
    private val aiOrchestrator: AIOrchestrator
) {

    private var sessionState: SessionState? = null

    /**
     * Initializes a creative session for a specific scene.
     *
     * @param storyId The ID of the active story.
     * @param chapterId The ID of the active chapter.
     * @param sceneId The ID of the active scene.
     * @return The initialized SessionState.
     */
    fun startSession(storyId: RealmUUID, chapterId: RealmUUID, sceneId: RealmUUID?): SessionState {
        val state = storyManager.getSessionState() ?: SessionState()
        state.activeStoryId = storyId
        state.activeChapterId = chapterId
        state.activeSceneId = sceneId
        sessionState = state
        return state
    }

    /**
     * Processes user text/transcription. Triggers the AIOrchestrator
     * and returns the new SessionState.
     *
     * @param input The user's input.
     * @return The updated SessionState.
     */
    fun handleUserInput(input: String): SessionState {
        sessionState?.let { state ->
            // 1. Call the AI Orchestrator to get Lumi's response
            val lumiResponse = aiOrchestrator.generateInterviewQuestion(input)

            // 2. Update the SessionState with the new information
            state.lastLumiResponse = lumiResponse
            state.isAwaitingUserInput = true

            // 3. Return the updated state
            return state
        }
        // This should not happen if a session is active
        return SessionState()
    }

    /**
     * Safely ends the current session, ensuring all data is saved.
     */
    suspend fun pauseSession() {
        sessionState?.let {
            storyManager.updateSessionState(it)
        }
    }

    /**
     * Retrieves the last known session state on app launch.
     *
     * @return The last known SessionState.
     */
    fun getCurrentState(): SessionState? {
        sessionState = storyManager.getSessionState()
        return sessionState
    }
}