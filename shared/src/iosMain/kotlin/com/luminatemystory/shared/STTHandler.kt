package com.luminatemystory.shared

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.Speech.*
import platform.AVFAudio.*

/**
 * STT Handler actual Class for iOS
 *
 * This actual class provides the iOS-specific implementation for speech-to-text services.
 * Uses Apple's SFSpeechRecognizer for on-device speech recognition.
 */
actual class STTHandler {
    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private var audioEngine: AVAudioEngine? = null
    private var currentCallback: ((String) -> Unit)? = null

    init {
        // Initialize the speech recognizer for US English
        speechRecognizer = SFSpeechRecognizer.localeIdentifier("en-US")
    }

    actual fun startListening(onResult: (String) -> Unit) {
        currentCallback = onResult

        // Request authorization first
        SFSpeechRecognizer.requestAuthorization { authStatus ->
            when (authStatus) {
                SFSpeechRecognizerAuthorizationStatusAuthorized -> {
                    this.startRecognition()
                }
                SFSpeechRecognizerAuthorizationStatusDenied,
                SFSpeechRecognizerAuthorizationStatusRestricted,
                SFSpeechRecognizerAuthorizationStatusNotDetermined -> {
                    onResult("Speech recognition not authorized")
                }
                else -> {
                    onResult("Speech recognition authorization unknown")
                }
            }
        }
    }

    private fun startRecognition() {
        // Cancel any ongoing recognition
        recognitionTask?.cancel()
        recognitionTask = null

        // Create audio session
        val audioSession = AVAudioSession.sharedInstance()
        try {
            audioSession.setCategory(AVAudioSessionCategoryRecord, error = null)
            audioSession.setMode(AVAudioSessionModeMeasurement, error = null)
            audioSession.setActive(true, withOptions = AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation, error = null)
        } catch (e: Exception) {
            currentCallback?.invoke("Audio session error: ${e.message}")
            return
        }

        // Create recognition request
        recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
        recognitionRequest?.shouldReportPartialResults = true

        // Create audio engine
        audioEngine = AVAudioEngine()
        val inputNode = audioEngine?.inputNode
        if (inputNode == null) {
            currentCallback?.invoke("Audio engine has no input node")
            return
        }

        // Start recognition task
        recognitionTask = speechRecognizer?.recognitionTaskWithRequest(
            recognitionRequest!!,
            resultHandler = { result, error ->
                if (error != null) {
                    this.stopListening()
                    this.currentCallback?.invoke("Recognition error: ${error.localizedDescription}")
                    return@recognitionTaskWithRequest
                }

                if (result != null) {
                    val transcription = result.bestTranscription.formattedString
                    if (result.isFinal) {
                        this.stopListening()
                        this.currentCallback?.invoke(transcription)
                    }
                }
            }
        )

        // Configure audio tap
        val recordingFormat = inputNode.outputFormatForBus(0u)
        inputNode.installTapOnBus(
            0u,
            bufferSize = 1024u,
            format = recordingFormat
        ) { buffer, _ ->
            this.recognitionRequest?.appendAudioPCMBuffer(buffer)
        }

        // Start audio engine
        audioEngine?.prepare()
        try {
            audioEngine?.startAndReturnError(null)
        } catch (e: Exception) {
            currentCallback?.invoke("Audio engine error: ${e.message}")
        }
    }

    actual fun stopListening() {
        audioEngine?.stop()
        audioEngine?.inputNode?.removeTapOnBus(0u)
        recognitionRequest?.endAudio()
        recognitionTask?.cancel()

        audioEngine = null
        recognitionRequest = null
        recognitionTask = null
    }
}