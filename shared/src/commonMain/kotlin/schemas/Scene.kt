package com.luminatemystory.shared.schemas

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Scene : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var title: String? = null
    var order: Int = 0
    var interviewData: InterviewData? = null
    var draftText: String = ""
    var createdAt: RealmInstant = RealmInstant.now()
    var lastModified: RealmInstant = RealmInstant.now()
}
