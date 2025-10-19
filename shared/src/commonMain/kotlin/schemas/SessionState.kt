package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class SessionState : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var activeStoryId: RealmUUID? = null
    var activeChapterId: RealmUUID? = null
    var activeSceneId: RealmUUID? = null
    var currentInputMode: String = "Text" // "Voice", "Text"
    var partialUserInput: String = ""
    var lastLumiResponse: String = ""
    var isAwaitingUserInput: Boolean = false
}