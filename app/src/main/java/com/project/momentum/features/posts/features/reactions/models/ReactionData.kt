package com.project.momentum.features.posts.features.reactions.models

data class ReactionData (
    val emoji: ReactionType,
    val count: Int,
    val users: List<String> // список id пользователей, которые поставили реакцию
)