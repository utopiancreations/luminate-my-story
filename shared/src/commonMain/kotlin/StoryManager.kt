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
     * Retrieves a list of all stories, sorted by lastModified descending.
     * @return A list of all Story objects, most recently modified first.
     */
    fun getStoriesList(): List<Story> {
        return persistenceLayer.getStoriesList()
            .sortedByDescending { it.lastModified }
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
     * @param title The title of the new scene.
     * @return The newly created Scene object.
     */
    suspend fun createScene(chapter: Chapter, title: String?): Scene {
        return persistenceLayer.createScene(chapter, title)
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
     * Updates the draft text for a specific scene.
     * @param sceneId The ID of the scene to update.
     * @param draft The new draft text.
     */
    suspend fun updateSceneDraft(sceneId: RealmUUID, draft: String) {
        val scene = persistenceLayer.getScene(sceneId)
        scene?.let {
            it.draftText = draft
            persistenceLayer.updateScene(it)
        }
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

    // ========================================
    // Enhanced Story Management Methods
    // ========================================

    /**
     * Adds a new chapter to a specific story by ID.
     * @param storyId The ID of the story to add the chapter to.
     * @param title The title of the new chapter.
     * @return The newly created Chapter object, or null if story not found.
     */
    suspend fun addChapter(storyId: RealmUUID, title: String): Chapter? {
        val story = persistenceLayer.getStory(storyId) ?: return null
        return persistenceLayer.createChapter(story, title)
    }

    /**
     * Adds a new scene to a specific chapter by ID.
     * @param chapterId The ID of the chapter to add the scene to.
     * @param title The title of the new scene (can be null).
     * @return The newly created Scene object, or null if chapter not found.
     */
    suspend fun addScene(chapterId: RealmUUID, title: String?): Scene? {
        val chapter = persistenceLayer.getChapter(chapterId) ?: return null
        return persistenceLayer.createScene(chapter, title)
    }

    /**
     * Deletes a story and all of its nested chapters and scenes by ID.
     * Performs cascade delete to ensure data integrity.
     * @param id The ID of the story to delete.
     */
    suspend fun deleteStory(id: RealmUUID) {
        val story = persistenceLayer.getStory(id) ?: return

        // Delete all scenes in all chapters
        story.chapters.forEach { chapter ->
            chapter.scenes.forEach { scene ->
                persistenceLayer.deleteScene(scene)
            }
            persistenceLayer.deleteChapter(chapter)
        }

        // Delete the story itself
        persistenceLayer.deleteStory(story)
    }

    /**
     * Deletes a chapter and all of its nested scenes by ID.
     * Performs cascade delete to ensure data integrity.
     * @param id The ID of the chapter to delete.
     */
    suspend fun deleteChapter(id: RealmUUID) {
        val chapter = persistenceLayer.getChapter(id) ?: return

        // Delete all scenes in the chapter
        chapter.scenes.forEach { scene ->
            persistenceLayer.deleteScene(scene)
        }

        // Delete the chapter itself
        persistenceLayer.deleteChapter(chapter)
    }

    /**
     * Deletes a single scene by ID.
     * @param id The ID of the scene to delete.
     */
    suspend fun deleteScene(id: RealmUUID) {
        val scene = persistenceLayer.getScene(id) ?: return
        persistenceLayer.deleteScene(scene)
    }
}
