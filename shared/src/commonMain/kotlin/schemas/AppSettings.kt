package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class AppSettings : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var appVersion: String = "1.1.0"
    var enableEmotionalCheckins: Boolean = true
    var defaultInputMode: String = "Voice" // "Voice", "Text"
    var ttsVoice: String = "Default" // Identifier for the selected Text-to-Speech voice
    var defaultExportFormat: String = "MD" // "TXT", "MD", "PDF"
    var cloudSyncProvider: String? = null // "iCloud", "GoogleDrive", null
    var cloudSyncEnabled: Boolean = false
}
