package com.luminatemystory.shared

import com.luminatemystory.shared.schemas.*
import io.realm.kotlin.types.RealmUUID

/**
 * Story Manager
 *
 * This module provides the public API for story-related operations,
 * including creating, updating, and managing stories, chapters, and scenes.
 *
 * @property persistenceLayer The underlying persistence layer for data operations.
 */
class StoryManager(private val persistenceLayer: PersistenceLayer) {

    /**
     * Creates a new story with the given title.
     * @param title The title of the story.
     * @return The newly created Story object.
     */
    suspend fun createStory(title: String): Story {
        return persistenceLayer.createStory(title)
    }

    /**
     * Retrieves a story by its unique ID.
     * @param id The RealmUUID of the story.
     * @return The Story object if found, otherwise null.
     */
    fun getStory(id: RealmUUID): Story? {
        return persistenceLayer.getStory(id)
    }

    /**
     * Retrieves a list of all stories.
     * @return A list of all Story objects.
     */
    fun getStoriesList(): List<Story> {
        return persistenceLayer.getStoriesList()
    }

    /**
     * Updates an existing story.
     * @param story The story object to update.
     */
    suspend fun updateStory(story: Story) {
        persistenceLayer.updateStory(story)
    }

    /**
     * Deletes a story.
     * @param story The story object to delete.
     */
    suspend fun deleteStory(story: Story) {
        persistenceLayer.deleteStory(story)
    }

    /**
     * Creates a new chapter for a given story.
     * @param story The story to add the chapter to.
     * @param title The title of the new chapter.
     * @return The newly created Chapter object.
     */
    suspend fun createChapter(story: Story, title: String): Chapter {
        return persistenceLayer.createChapter(story, title)
    }

    /**
     * Retrieves a chapter by its unique ID.
     * @param id The RealmUUID of the chapter.
     * @return The Chapter object if found, otherwise null.
     */
    fun getChapter(id: RealmUUID): Chapter? {
        return persistenceLayer.getChapter(id)
    }

    /**
     * Updates an existing chapter.
     * @param chapter The chapter object to update.
     */
    suspend fun updateChapter(chapter: Chapter) {
        persistenceLayer.updateChapter(chapter)
    }

    /**
     * Deletes a chapter.
     * @param chapter The chapter object to delete.
     */
    suspend fun deleteChapter(chapter: Chapter) {
        persistenceLayer.deleteChapter(chapter)
    }

    /**
     * Creates a new scene for a given chapter.
     * @param chapter The chapter to add the scene to.
     * @param content The content of the new scene.
     * @return The newly created Scene object.
     */
    suspend fun createScene(chapter: Chapter, content: String): Scene {
        return persistenceLayer.createScene(chapter, content)
    }

    /**
     * Retrieves a scene by its unique ID.
     * @param id The RealmUUID of the scene.
     * @return The Scene object if found, otherwise null.
     */
    fun getScene(id: RealmUUID): Scene? {
        return persistenceLayer.getScene(id)
    }

    /**
     * Updates an existing scene.
     * @param scene The scene object to update.
     */
    suspend fun updateScene(scene: Scene) {
        persistenceLayer.updateScene(scene)
    }

    /**
     * Deletes a scene.
     * @param scene The scene object to delete.
     */
    suspend fun deleteScene(scene: Scene) {
        persistenceLayer.deleteScene(scene)
    }

    /**
     * Creates a new InterviewData object.
     * @return The newly created InterviewData object.
     */
    suspend fun createInterviewData(): InterviewData {
        return persistenceLayer.createInterviewData()
    }

    /**
     * Retrieves an InterviewData object by its unique ID.
     * @param id The RealmUUID of the InterviewData object.
     * @return The InterviewData object if found, otherwise null.
     */
    fun getInterviewData(id: RealmUUID): InterviewData? {
        return persistenceLayer.getInterviewData(id)
    }

    /**
     * Deletes an InterviewData object.
     * @param interviewData The InterviewData object to delete.
     */
    suspend fun deleteInterviewData(interviewData: InterviewData) {
        persistenceLayer.deleteInterviewData(interviewData)
    }

    /**
     * Creates a new QAPair for a given InterviewData object.
     * @param interviewData The InterviewData object to add the QAPair to.
     * @param question The question for the new QAPair.
     * @param answer The answer for the new QAPair.
     * @return The newly created QAPair object.
     */
    suspend fun createQAPair(interviewData: InterviewData, question: String, answer: String): QAPair {
        return persistenceLayer.createQAPair(interviewData, question, answer)
    }

    /**
     * Retrieves a QAPair by its unique ID.
     * @param id The RealmUUID of the QAPair.
     * @return The QAPair object if found, otherwise null.
     */
    fun getQAPair(id: RealmUUID): QAPair? {
        return persistenceLayer.getQAPair(id)
    }

    /**
     * Updates an existing QAPair.
     * @param qaPair The QAPair object to update.
     */
    suspend fun updateQAPair(qaPair: QAPair) {
        persistenceLayer.updateQAPair(qaPair)
    }

    /**
     * Deletes a QAPair.
     * @param qaPair The QAPair object to delete.
     */
    suspend fun deleteQAPair(qaPair: QAPair) {
        persistenceLayer.deleteQAPair(qaPair)
    }

    /**
     * Retrieves the AppSettings object.
     * @return The AppSettings object if found, otherwise null.
     */
    fun getAppSettings(): AppSettings? {
        return persistenceLayer.getAppSettings()
    }

    /**
     * Updates the AppSettings object.
     * @param settings The AppSettings object to update.
     */
    suspend fun updateAppSettings(settings: AppSettings) {
        persistenceLayer.updateAppSettings(settings)
    }

    /**
     * Retrieves the SessionState object.
     * @return The SessionState object if found, otherwise null.
     */
    fun getSessionState(): SessionState? {
        return persistenceLayer.getSessionState()
    }

    /**
     * Updates the SessionState object.
     * @param state The SessionState object to update.
     */
    suspend fun updateSessionState(state: SessionState) {
        persistenceLayer.updateSessionState(state)
    }
}
