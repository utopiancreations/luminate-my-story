package com.luminatemystory.shared.schemas

data class UserContext(
    val userName: String = "the author",
    val userDescription: String = "an author", // e.g., "a young woman", "a veteran"
    val userThemes: String = "their life", // e.g., "motherhood and loss"
    val mentionedNames: List<String> = emptyList() // e.g., ["DJ", "Jeromey"]
)
