package com.luminatemystory.shared

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * TTS Handler actual Class for Android
 *
 * This actual class provides the Android-specific implementation for text-to-speech services.
 */
actual class TTSHandler(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context, this)
    }

    actual fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language not supported
            }
        } else {
            // Initialization failed
        }
    }
}

