package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.SessionState
import com.luminatemystory.shared.schemas.UserContext
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Session Manager
 *
 * This module manages user sessions, handles authentication,
 * and maintains session state throughout the application lifecycle.
 *
 * @property storyManager The manager for story-related operations.
 * @property aiOrchestrator The orchestrator for AI interactions.
 * @property contextService The service for building intelligent conversation context.
 */
class SessionManager(
    private val storyManager: StoryManager,
    private val aiOrchestrator: AIOrchestrator,
    private val contextService: ContextService,
    private val sttHandler: STTHandler,
    private val ttsHandler: TTSHandler
) {

    private var sessionState: SessionState? = null
    private var userContext: UserContext = UserContext()

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
     * This method now uses ContextService to build intelligent conversation context,
     * preventing context window exhaustion and enabling more relevant questions.
     *
     * @param input The user's input/answer.
     * @param speakResponse If true, uses TTS to speak the response (for voice mode).
     * @return The updated SessionState.
     */
    suspend fun handleUserInput(input: String, speakResponse: Boolean = false): SessionState {
        sessionState?.let { state ->
            // Get the active scene ID
            val sceneId = state.activeSceneId
            if (sceneId == null) {
                state.lastLumiResponse = "Error: No active scene. Please start a session first."
                return state
            }

            // 1. Build intelligent context using ContextService
            val conversationContext = contextService.buildContextWithCurrentInput(sceneId, input)

            // 2. Call the AI Orchestrator to get Lumi's next question
            val lumiResponse = aiOrchestrator.generateInterviewQuestion(conversationContext, userContext)

            // 3. Update the SessionState with the new information
            state.lastLumiResponse = lumiResponse
            state.isAwaitingUserInput = true

            // 4. Speak the response if in voice mode
            if (speakResponse) {
                ttsHandler.speak(lumiResponse)
            }

            // 5. Return the updated state
            return state
        }
        // This should not happen if a session is active
        return SessionState()
    }

    /**
     * Starts an interview session for a scene.
     * Gets the first question from Lumi to begin the conversation.
     *
     * @param speakResponse If true, speaks the first question.
     * @return The session state with Lumi's first question.
     */
    suspend fun startInterviewSession(speakResponse: Boolean = false): SessionState {
        sessionState?.let { state ->
            val sceneId = state.activeSceneId
            if (sceneId == null) {
                state.lastLumiResponse = "Error: No active scene. Please start a session first."
                return state
            }

            // Build initial context (no current input yet)
            val conversationContext = contextService.buildContextForInterview(sceneId)

            // Get Lumi's first question
            val lumiResponse = aiOrchestrator.generateInterviewQuestion(conversationContext, userContext)

            // Update state
            state.lastLumiResponse = lumiResponse
            state.isAwaitingUserInput = true

            // Speak if requested
            if (speakResponse) {
                ttsHandler.speak(lumiResponse)
            }

            return state
        }
        return SessionState()
    }

    /**
     * Updates the user context for the current session.
     *
     * @param context The updated UserContext.
     */
    fun updateUserContext(context: UserContext) {
        userContext = context
    }

    /**
     * Retrieves the current user context.
     *
     * @return The current UserContext.
     */
    fun getUserContext(): UserContext {
        return userContext
    }

    /**
     * Starts a voice conversation session.
     * Implements the complete loop: voice input → LLM → voice output
     *
     * @param onTranscriptionReceived Optional callback when transcription is received.
     * @param onResponseGenerated Optional callback when LLM response is generated.
     */
    fun startVoiceSession(
        onTranscriptionReceived: ((String) -> Unit)? = null,
        onResponseGenerated: ((String) -> Unit)? = null
    ) {
        sttHandler.startListening { transcribedText ->
            // Notify callback of transcription
            onTranscriptionReceived?.invoke(transcribedText)

            // Process the transcribed text through the LLM and speak the response
            // Note: This uses a coroutine scope that should be provided by the platform layer
            kotlinx.coroutines.GlobalScope.launch {
                try {
                    val state = handleUserInput(transcribedText, speakResponse = true)
                    onResponseGenerated?.invoke(state.lastLumiResponse)
                } catch (e: Exception) {
                    // Handle errors (e.g., log or speak error message)
                    ttsHandler.speak("I'm sorry, I encountered an error processing your request.")
                }
            }
        }
    }

    /**
     * Stops the voice session.
     */
    fun stopVoiceSession() {
        sttHandler.stopListening()
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