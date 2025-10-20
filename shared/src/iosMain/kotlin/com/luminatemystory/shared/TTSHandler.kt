package com.luminatemystory.shared

/**
 * TTS Handler actual Class for iOS
 *
 * This actual class provides the iOS-specific implementation for text-to-speech services.
 */
actual class TTSHandler {
    actual fun speak(text: String) {
        println("iOS TTS speaking: $text")
    }
}