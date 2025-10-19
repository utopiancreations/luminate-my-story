package com.luminatemystory.shared.schemas

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class InterviewData : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var qaPairs: RealmList<QAPair> = realmListOf()
    var createdAt: RealmInstant = RealmInstant.now()
    var lastModified: RealmInstant = RealmInstant.now()
}
