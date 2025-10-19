package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class QAPair : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var question: String = ""
    var answer: String = ""
    var timestamp: RealmInstant = RealmInstant.now()
}