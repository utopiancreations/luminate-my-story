package com.luminatemystory.shared

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * LLM Handler actual Class for Android
 *
 * This actual class provides the Android-specific implementation for connecting to a local LLM.
 * Uses MediaPipe's LLM Inference API to run the Gemma 2B Uncensored model on-device.
 */
actual class LLMHandler(private val context: Context) {
    private var llmInference: LlmInference? = null
    private val modelAssetName = "gemma-2b-uncensored-v1.Q8_0.gguf"

    init {
        try {
            // Get the model path (copy from assets if needed)
            val modelPath = getModelPath()

            // Initialize MediaPipe LLM Inference with our model
            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath(modelPath)
                .setMaxTokens(1024) // Increased for longer story content
                .setTemperature(0.7f)
                .setTopK(40)
                .build()

            llmInference = LlmInference.createFromOptions(context, options)
        } catch (e: Exception) {
            // Log error - model may not be downloaded yet
            android.util.Log.e("LLMHandler", "Failed to initialize LLM: ${e.message}")
            // Don't throw here - allow app to start and show error when executePrompt is called
        }
    }

    /**
     * Gets the file path to the model, copying from assets if necessary.
     * MediaPipe requires a file path, so we copy the model from assets to cache directory.
     */
    private fun getModelPath(): String {
        val cacheDir = context.cacheDir
        val modelFile = File(cacheDir, modelAssetName)

        // If model already exists in cache and is complete, use it
        if (modelFile.exists() && modelFile.length() > 0) {
            return modelFile.absolutePath
        }

        // Copy model from assets to cache
        try {
            context.assets.open(modelAssetName).use { input ->
                FileOutputStream(modelFile).use { output ->
                    input.copyTo(output)
                }
            }
            return modelFile.absolutePath
        } catch (e: Exception) {
            throw IllegalStateException(
                "Model file not found in assets. Please download $modelAssetName " +
                "and place it in androidApp/src/main/assets/. " +
                "See androidApp/src/main/assets/README.md for instructions. " +
                "Error: ${e.message}", e
            )
        }
    }

    actual suspend fun executePrompt(prompt: String): String {
        return withContext(Dispatchers.IO) {
            if (llmInference == null) {
                return@withContext "Error: LLM not initialized. Please ensure the model file " +
                    "$modelAssetName is placed in androidApp/src/main/assets/. " +
                    "See README.md in that directory for download instructions."
            }

            try {
                // Generate response using MediaPipe LLM Inference
                val response = llmInference?.generateResponse(prompt)
                response ?: "Error: No response generated"
            } catch (e: Exception) {
                "Error executing prompt: ${e.message}"
            }
        }
    }

    /**
     * Releases the LLM resources.
     */
    fun close() {
        llmInference?.close()
        llmInference = null
    }
}
