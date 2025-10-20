package com.luminatemystory.shared

/**
 * LLM Handler actual Class for iOS
 *
 * This actual class provides the iOS-specific implementation for connecting to a local LLM.
 */
actual class LLMHandler {
    actual suspend fun executePrompt(prompt: String): String {
        return "Placeholder response from iOS LLM"
    }
}