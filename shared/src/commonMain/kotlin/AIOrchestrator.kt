package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.InterviewData
import com.luminatemystory.shared.schemas.UserContext

/**
 * AI Orchestrator
 *
 * This module will be responsible for managing AI interactions,
 * coordinating between different AI services, and handling the
 * generation of story content based on interview data.
 *
 * @property llmHandler The handler for connecting to a local LLM.
 */
class AIOrchestrator(private val llmHandler: LLMHandler) {

    /**
     * Builds a final prompt string from a template and user context.
     *
     * @param template The prompt template string.
     * @param userContext The user's personalization data.
     * @param additionalParams A map of any other parameters to replace.
     * @return The final, formatted prompt string.
     */
    private fun buildPrompt(template: String, userContext: UserContext, additionalParams: Map<String, String>): String {
        var finalPrompt = template
        finalPrompt = finalPrompt.replace("{user_name}", userContext.userName)
        finalPrompt = finalPrompt.replace("{user_description}", userContext.userDescription)
        finalPrompt = finalPrompt.replace("{user_themes}", userContext.userThemes)
        finalPrompt = finalPrompt.replace("{mentioned_names_list}", userContext.mentionedNames.joinToString(", "))

        additionalParams.forEach { (key, value) ->
            finalPrompt = finalPrompt.replace("{$key}", value)
        }

        return finalPrompt
    }

    /**
     * Processes a new topic from the user and generates an outline prompt.
     * @param topic The user's topic (raw text).
     * @param userContext The user's personalization data.
     * @return A generated prompt.
     */
    suspend fun processNewTopic(topic: String, userContext: UserContext): String {
        val params = mapOf("raw_text_content" to topic)
        val finalPrompt = buildPrompt(LumiPromptTemplates.GET_OUTLINE_PROMPT_TEMPLATE, userContext, params)
        return llmHandler.executePrompt(finalPrompt)
    }

    /**
     * Generates an interview question based on the current conversation context.
     *
     * This method now accepts a rich context string from ContextService that includes
     * the scene title and conversation history, which it parses and formats for the LLM.
     *
     * @param contextString The formatted context from ContextService containing scene and conversation history.
     * @param userContext The user's personalization data.
     * @return The next interview question from Lumi.
     */
    suspend fun generateInterviewQuestion(contextString: String, userContext: UserContext): String {
        // Parse the context string to extract scene title and conversation history
        val (sceneTitle, conversationHistory) = parseContextString(contextString)

        // Prepare parameters for the template
        val params = mapOf(
            "scene_title" to sceneTitle,
            "conversation_history" to conversationHistory
        )

        val finalPrompt = buildPrompt(LumiPromptTemplates.GET_INTERVIEW_PROMPT_TEMPLATE, userContext, params)
        return llmHandler.executePrompt(finalPrompt)
    }

    /**
     * Parses the context string from ContextService into scene title and conversation history.
     *
     * @param contextString The formatted context string.
     * @return A Pair of (sceneTitle, conversationHistory).
     */
    private fun parseContextString(contextString: String): Pair<String, String> {
        val lines = contextString.lines()

        var sceneTitle = "Unknown Scene"
        var conversationHistory = "No conversation history yet."

        // Find the scene title (first line after "Scene:")
        val sceneLine = lines.find { it.startsWith("Scene:") }
        if (sceneLine != null) {
            sceneTitle = sceneLine.substringAfter("Scene:").trim()
        }

        // Find the conversation history section
        val historyStartIndex = lines.indexOfFirst { it.contains("Conversation History:") }
        if (historyStartIndex != -1 && historyStartIndex + 1 < lines.size) {
            conversationHistory = lines.drop(historyStartIndex + 1).joinToString("\n").trim()
        }

        return Pair(sceneTitle, conversationHistory)
    }

    /**
     * Takes completed interview data and generates a draft for a scene.
     * @param interviewData The interview data.
     * @param userContext The user's personalization data.
     * @return A generated draft prompt.
     */
    suspend fun generateDraft(interviewData: InterviewData, userContext: UserContext): String {
        // 1. Format the Q&A block from interviewData
        val qAndABlock = interviewData.qaPairs.joinToString("\n") { "Q: ${it.question}\nA: ${it.answer}" }

        // 2. Prepare additional parameters
        val params = mapOf(
            "outline_point" to "Some Scene Title", // This will come from the Scene object later
            "q_and_a_block" to qAndABlock
        )

        // 3. Build the final prompt
        val finalPrompt = buildPrompt(LumiPromptTemplates.GET_WRITE_PROMPT_TEMPLATE, userContext, params)

        // 4. (Placeholder) Return the generated prompt for now to verify it's working
        // In the future, this will be sent to the llmHandler
        return llmHandler.executePrompt(finalPrompt)
    }
}
