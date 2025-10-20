package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.QAPair
import io.realm.kotlin.types.RealmUUID

/**
 * Context Service
 *
 * This service manages the conversation context for AI interactions,
 * implementing intelligent summarization to prevent context window exhaustion.
 *
 * @property storyManager The manager for accessing story and scene data.
 */
class ContextService(private val storyManager: StoryManager) {

    /**
     * Builds an intelligent context string for an interview session.
     *
     * Strategy:
     * - Include first 2 Q&A pairs in full (establishes initial context)
     * - Include most recent 3 Q&A pairs in full (maintains current conversation flow)
     * - Summarize the middle section if there's a gap
     *
     * @param sceneId The ID of the scene being discussed.
     * @return A formatted context string containing scene title and curated conversation history.
     */
    fun buildContextForInterview(sceneId: RealmUUID): String {
        // Retrieve the scene
        val scene = storyManager.getScene(sceneId)
            ?: return "Error: Scene not found"

        // Get scene title
        val sceneTitle = scene.title ?: "Untitled Scene"

        // Get interview data and Q&A pairs
        val interviewData = scene.interviewData
        val qaPairs = interviewData?.qaPairs?.toList() ?: emptyList()

        // Build the conversation history
        val conversationHistory = buildConversationHistory(qaPairs)

        // Combine into context string
        return """
            Scene: $sceneTitle

            Conversation History:
            $conversationHistory
        """.trimIndent()
    }

    /**
     * Builds the conversation history section with intelligent summarization.
     *
     * @param qaPairs The list of Q&A pairs from the interview.
     * @return A formatted string with curated conversation history.
     */
    private fun buildConversationHistory(qaPairs: List<QAPair>): String {
        if (qaPairs.isEmpty()) {
            return "This is the start of the conversation."
        }

        val totalPairs = qaPairs.size

        return when {
            // If we have 5 or fewer pairs, include all of them
            totalPairs <= 5 -> {
                qaPairs.joinToString("\n\n") { formatQAPair(it) }
            }

            // If we have more than 5, use the summarization strategy
            else -> {
                val firstTwo = qaPairs.take(2)
                val lastThree = qaPairs.takeLast(3)
                val middleCount = totalPairs - 5

                val firstTwoFormatted = firstTwo.joinToString("\n\n") { formatQAPair(it) }
                val lastThreeFormatted = lastThree.joinToString("\n\n") { formatQAPair(it) }

                """
                    $firstTwoFormatted

                    [...$middleCount additional questions and answers were discussed...]

                    $lastThreeFormatted
                """.trimIndent()
            }
        }
    }

    /**
     * Formats a single Q&A pair for display.
     *
     * @param pair The Q&A pair to format.
     * @return A formatted string representation of the Q&A pair.
     */
    private fun formatQAPair(pair: QAPair): String {
        return """
            Q: ${pair.question}
            A: ${pair.answer}
        """.trimIndent()
    }

    /**
     * Builds a full context including the current user input.
     * This is useful when the user has just provided an answer.
     *
     * @param sceneId The ID of the scene being discussed.
     * @param currentInput The user's current input/answer.
     * @return A formatted context string with the current input included.
     */
    fun buildContextWithCurrentInput(sceneId: RealmUUID, currentInput: String): String {
        val baseContext = buildContextForInterview(sceneId)

        return """
            $baseContext

            Current User Response:
            $currentInput
        """.trimIndent()
    }
}
