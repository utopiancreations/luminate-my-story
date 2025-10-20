package com.luminatemystory.shared

/**
 * STT Handler expect Class
 *
 * This expect class defines the contract for speech-to-text services.
 */
expect class STTHandler {
    fun startListening(onResult: (String) -> Unit)
    fun stopListening()
}
