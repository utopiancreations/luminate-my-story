package com.luminatemystory.shared

/**
 * STT Handler actual Class for iOS
 *
 * This actual class provides the iOS-specific implementation for speech-to-text services.
 */
actual class STTHandler {
    actual fun startListening(onResult: (String) -> Unit) {
        onResult("Placeholder transcription from iOS STT")
    }

    actual fun stopListening() {
    }
}