package com.luminatemystory.shared

/**
 * TTS Handler expect Class
 *
 * This expect class defines the contract for text-to-speech services.
 */
expect class TTSHandler {
    fun speak(text: String)
}
