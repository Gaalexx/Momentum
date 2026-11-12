package com.project.momentum.domain.posts.repository

import com.project.momentum.domain.posts.model.PostData
import com.project.momentum.domain.posts.model.ReactionType
import com.project.momentum.domain.posts.model.Transcription

interface PostsRepository {
    suspend fun sendPostForTranscription(postId: String): Boolean
    suspend fun checkPostTranscriptionState(postId: String): Transcription
    suspend fun getAllPosts(): List<PostData>
    suspend fun deletePost(postId: String): Boolean
    suspend fun sendReaction(postId: String, reaction: ReactionType): Boolean
    suspend fun deleteReaction(postId: String, reaction: ReactionType): Boolean
    suspend fun getHiddenPosts(): List<String>
    suspend fun hidePost(postId: String): Boolean
    suspend fun showPost(postId: String): Boolean
}
