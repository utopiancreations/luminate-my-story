package com.luminatemystory.shared

/**
 * LLM Handler expect Class
 *
 * This expect class defines the contract for connecting to a local LLM.
 */
expect class LLMHandler {
    suspend fun executePrompt(prompt: String): String
}
