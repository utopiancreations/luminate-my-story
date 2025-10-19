package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class SessionState : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var currentStoryId: String? = null
}
