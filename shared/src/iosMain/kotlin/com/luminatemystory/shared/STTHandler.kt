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
@OptIn(ExperimentalForeignApi::class)
actual class STTHandler {
    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private var audioEngine: AVAudioEngine? = null
    private var currentCallback: ((String) -> Unit)? = null

    init {
        // Initialize the speech recognizer for US English
        val locale = NSLocale("en-US")
        speechRecognizer = SFSpeechRecognizer(locale = locale)
    }

    actual fun startListening(onResult: (String) -> Unit) {
        currentCallback = onResult

        // Request authorization first
        SFSpeechRecognizer.requestAuthorization { authStatus ->
            when (authStatus) {
                SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized -> {
                    this.startRecognition()
                }
                SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusDenied,
                SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusRestricted,
                SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusNotDetermined -> {
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
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            audioSession.setCategory(AVAudioSessionCategoryRecord, error = errorPtr.ptr)
            audioSession.setMode(AVAudioSessionModeMeasurement, error = errorPtr.ptr)
            audioSession.setActive(true, withOptions = AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation, error = errorPtr.ptr)

            if (errorPtr.value != null) {
                currentCallback?.invoke("Audio session error: ${errorPtr.value?.localizedDescription}")
                return
            }
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
                    if (result.final) {
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
            buffer?.let {
                this.recognitionRequest?.appendAudioPCMBuffer(it)
            }
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