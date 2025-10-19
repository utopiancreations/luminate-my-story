package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.Flow

class PersistenceLayer {
    private val configuration = RealmConfiguration.create(
        schema = setOf(
            Story::class,
            Chapter::class,
            Scene::class,
            InterviewData::class,
            QAPair::class,
            AppSettings::class,
            SessionState::class
        )
    )
    private val realm = Realm.open(configuration)

    /**
     * Creates a new story with the given title.
     * @param title The title of the story.
     * @return The newly created Story object.
     */
    suspend fun createStory(title: String): Story {
        return realm.write {
            copyToRealm(Story().apply {
                this.title = title
            })
        }
    }

    /**
     * Retrieves a story by its unique ID.
     * @param id The RealmUUID of the story.
     * @return The Story object if found, otherwise null.
     */
    fun getStory(id: RealmUUID): Story? {
        return realm.query<Story>("id == $0", id).first().find()
    }

    /**
     * Retrieves a list of all stories.
     * @return A list of all Story objects.
     */
    fun getStoriesList(): List<Story> {
        return realm.query<Story>().find()
    }

    /**
     * Updates an existing story.
     * @param story The story object to update.
     */
    suspend fun updateStory(story: Story) {
        realm.write {
            findLatest(story)?.let {
                it.title = story.title
                it.visibility = story.visibility
                it.lastModified = story.lastModified
            }
        }
    }

    /**
     * Deletes a story.
     * @param story The story object to delete.
     */
    suspend fun deleteStory(story: Story) {
        realm.write {
            findLatest(story)?.let { delete(it) }
        }
    }

    /**
     * Creates a new chapter for a given story.
     * @param story The story to add the chapter to.
     * @param title The title of the new chapter.
     * @return The newly created Chapter object.
     */
    suspend fun createChapter(story: Story, title: String): Chapter {
        val chapter = Chapter().apply { this.title = title }
        realm.write {
            findLatest(story)?.chapters?.add(chapter)
        }
        return chapter
    }

    /**
     * Retrieves a chapter by its unique ID.
     * @param id The RealmUUID of the chapter.
     * @return The Chapter object if found, otherwise null.
     */
    fun getChapter(id: RealmUUID): Chapter? {
        return realm.query<Chapter>("id == $0", id).first().find()
    }

    /**
     * Updates an existing chapter.
     * @param chapter The chapter object to update.
     */
    suspend fun updateChapter(chapter: Chapter) {
        realm.write {
            findLatest(chapter)?.let {
                it.title = chapter.title
                it.lastModified = chapter.lastModified
            }
        }
    }

    /**
     * Deletes a chapter.
     * @param chapter The chapter object to delete.
     */
    suspend fun deleteChapter(chapter: Chapter) {
        realm.write {
            findLatest(chapter)?.let { delete(it) }
        }
    }

    /**
     * Creates a new scene for a given chapter.
     * @param chapter The chapter to add the scene to.
     * @param title The title of the new scene.
     * @return The newly created Scene object.
     */
    suspend fun createScene(chapter: Chapter, title: String?): Scene {
        val scene = Scene().apply { this.title = title }
        realm.write {
            findLatest(chapter)?.scenes?.add(scene)
        }
        return scene
    }

    /**
     * Retrieves a scene by its unique ID.
     * @param id The RealmUUID of the scene.
     * @return The Scene object if found, otherwise null.
     */
    fun getScene(id: RealmUUID): Scene? {
        return realm.query<Scene>("id == $0", id).first().find()
    }

    /**
     * Updates an existing scene.
     * @param scene The scene object to update.
     */
    suspend fun updateScene(scene: Scene) {
        realm.write {
            findLatest(scene)?.let {
                it.title = scene.title
                it.order = scene.order
                it.draftText = scene.draftText
                it.lastModified = scene.lastModified
            }
        }
    }

    /**
     * Deletes a scene.
     * @param scene The scene object to delete.
     */
    suspend fun deleteScene(scene: Scene) {
        realm.write {
            findLatest(scene)?.let { delete(it) }
        }
    }

    /**
     * Creates a new InterviewData object.
     * @return The newly created InterviewData object.
     */
    suspend fun createInterviewData(): InterviewData {
        return realm.write {
            copyToRealm(InterviewData())
        }
    }

    /**
     * Retrieves an InterviewData object by its unique ID.
     * @param id The RealmUUID of the InterviewData object.
     * @return The InterviewData object if found, otherwise null.
     */
    fun getInterviewData(id: RealmUUID): InterviewData? {
        return realm.query<InterviewData>("id == $0", id).first().find()
    }

    /**
     * Deletes an InterviewData object.
     * @param interviewData The InterviewData object to delete.
     */
    suspend fun deleteInterviewData(interviewData: InterviewData) {
        realm.write {
            findLatest(interviewData)?.let { delete(it) }
        }
    }

    /**
     * Creates a new QAPair for a given InterviewData object.
     * @param interviewData The InterviewData object to add the QAPair to.
     * @param question The question for the new QAPair.
     * @param answer The answer for the new QAPair.
     * @return The newly created QAPair object.
     */
    suspend fun createQAPair(interviewData: InterviewData, question: String, answer: String): QAPair {
        val qaPair = QAPair().apply {
            this.question = question
            this.answer = answer
        }
        realm.write {
            findLatest(interviewData)?.qaPairs?.add(qaPair)
        }
        return qaPair
    }

    /**
     * Retrieves a QAPair by its unique ID.
     * @param id The RealmUUID of the QAPair.
     * @return The QAPair object if found, otherwise null.
     */
    fun getQAPair(id: RealmUUID): QAPair? {
        return realm.query<QAPair>("id == $0", id).first().find()
    }

    /**
     * Updates an existing QAPair.
     * @param qaPair The QAPair object to update.
     */
    suspend fun updateQAPair(qaPair: QAPair) {
        realm.write {
            findLatest(qaPair)?.let {
                it.question = qaPair.question
                it.answer = qaPair.answer
            }
        }
    }

    /**
     * Deletes a QAPair.
     * @param qaPair The QAPair object to delete.
     */
    suspend fun deleteQAPair(qaPair: QAPair) {
        realm.write {
            findLatest(qaPair)?.let { delete(it) }
        }
    }

    /**
     * Retrieves the AppSettings object.
     * @return The AppSettings object if found, otherwise null.
     */
    fun getAppSettings(): AppSettings? {
        return realm.query<AppSettings>().first().find()
    }

    /**
     * Updates the AppSettings object.
     * @param settings The AppSettings object to update.
     */
    suspend fun updateAppSettings(settings: AppSettings) {
        realm.write {
            findLatest(settings)?.let {
                it.appVersion = settings.appVersion
                it.enableEmotionalCheckins = settings.enableEmotionalCheckins
                it.defaultInputMode = settings.defaultInputMode
                it.ttsVoice = settings.ttsVoice
                it.defaultExportFormat = settings.defaultExportFormat
                it.cloudSyncProvider = settings.cloudSyncProvider
                it.cloudSyncEnabled = settings.cloudSyncEnabled
            }
        }
    }

    /**
     * Retrieves the SessionState object.
     * @return The SessionState object if found, otherwise null.
     */
    fun getSessionState(): SessionState? {
        return realm.query<SessionState>().first().find()
    }

    /**
     * Updates the SessionState object.
     * @param state The SessionState object to update.
     */
    suspend fun updateSessionState(state: SessionState) {
        realm.write {
            findLatest(state)?.let {
                it.currentStoryId = state.currentStoryId
            }
        }
    }
}
