package com.luminatemystory.shared

import kotlinx.cinterop.*
import platform.AVFAudio.*
import platform.Foundation.*

/**
 * TTS Handler actual Class for iOS
 *
 * This actual class provides the iOS-specific implementation for text-to-speech services.
 * Uses Apple's AVSpeechSynthesizer for natural-sounding text-to-speech.
 */
@OptIn(ExperimentalForeignApi::class)
actual class TTSHandler {
    private val synthesizer: AVSpeechSynthesizer = AVSpeechSynthesizer()
    private var currentVoice: AVSpeechSynthesisVoice? = null

    init {
        // Set default voice to US English
        currentVoice = AVSpeechSynthesisVoice.voiceWithLanguage("en-US")
    }

    actual fun speak(text: String) {
        if (text.isEmpty()) return

        // Create an utterance with the text to speak
        val utterance = AVSpeechUtterance.speechUtteranceWithString(text)

        // Configure the utterance
        utterance.voice = currentVoice
        utterance.rate = AVSpeechUtteranceDefaultSpeechRate  // Normal speed
        utterance.pitchMultiplier = 1.0f  // Normal pitch
        utterance.volume = 1.0f  // Full volume

        // Stop any ongoing speech
        if (synthesizer.speaking) {
            synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
        }

        // Speak the utterance
        synthesizer.speakUtterance(utterance)
    }

    /**
     * Sets the voice for text-to-speech.
     *
     * @param languageCode The language code (e.g., "en-US", "en-GB")
     */
    fun setVoice(languageCode: String) {
        currentVoice = AVSpeechSynthesisVoice.voiceWithLanguage(languageCode)
    }

    /**
     * Sets a specific voice by identifier.
     *
     * @param voiceIdentifier The voice identifier
     */
    fun setVoiceByIdentifier(voiceIdentifier: String) {
        currentVoice = AVSpeechSynthesisVoice.voiceWithIdentifier(voiceIdentifier)
    }

    /**
     * Gets all available voices.
     *
     * @return List of available voice identifiers
     */
    fun getAvailableVoices(): List<String> {
        return AVSpeechSynthesisVoice.speechVoices().map { voice ->
            (voice as AVSpeechSynthesisVoice).identifier
        }
    }

    /**
     * Stops speaking immediately.
     */
    fun stop() {
        if (synthesizer.speaking) {
            synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
        }
    }

    /**
     * Pauses speaking.
     */
    fun pause() {
        if (synthesizer.speaking) {
            synthesizer.pauseSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
        }
    }

    /**
     * Resumes speaking.
     */
    fun resume() {
        if (synthesizer.paused) {
            synthesizer.continueSpeaking()
        }
    }
}