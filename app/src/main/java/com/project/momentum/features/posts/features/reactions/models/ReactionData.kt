package com.project.momentum.features.posts.features.reactions.models

data class ReactionData (
    val emoji: ReactionType,
    val users: List<String> // список id пользователей, которые поставили реакцию
)