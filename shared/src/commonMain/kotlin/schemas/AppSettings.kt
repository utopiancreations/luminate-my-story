package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class AppSettings : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var appVersion: String = "1.0.0"
}
