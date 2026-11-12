package com.project.momentum.domain.posts.model

data class ReactionData (
    val emoji: ReactionType,
    val users: List<String> // список id пользователей, которые поставили реакцию
)