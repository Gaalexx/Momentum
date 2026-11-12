package com.project.momentum.data.posts.repository

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.domain.posts.model.PostData
import com.project.momentum.data.posts.remote.PostsAPI
import com.project.momentum.domain.posts.model.ReactionType
import com.project.momentum.domain.posts.model.Transcription
import com.project.momentum.domain.posts.repository.PostsRepository
import com.project.momentum.data.posts.mapper.toDomain
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepositoryImpl @Inject constructor(
    private val postsAPI: PostsAPI,
    private val sessionManager: SessionManager
) : PostsRepository {

    override suspend fun sendPostForTranscription(postId: String): Boolean =
        postsAPI.sendPostForTranscription(postId)

    override suspend fun checkPostTranscriptionState(postId: String): Transcription =
        postsAPI.checkPostTranscriptionState(postId).toDomain()

    override suspend fun getAllPosts(): List<PostData> {
        val myPostsDTO = postsAPI.getMyPosts()
        val myFriendsPostsDTO = postsAPI.getMyFriendsPosts()
        val currentUser = sessionManager.getUserId() ?: throw Exception("Unauthenticated")

        return (myPostsDTO + myFriendsPostsDTO)
            .map { it.toDomain(currentUser) }
            .sortedByDescending { it.createdAtInstantOrNull() ?: Instant.MIN }
    }

    override suspend fun deletePost(postId: String): Boolean =
        postsAPI.deletePost(postId)

    override suspend fun sendReaction(postId: String, reaction: ReactionType): Boolean =
        postsAPI.sendReaction(postId, reaction)

    override suspend fun deleteReaction(postId: String, reaction: ReactionType): Boolean =
        postsAPI.deleteReaction(postId, reaction)

    override suspend fun getHiddenPosts(): List<String> =
        postsAPI.getHiddenPosts()

    override suspend fun hidePost(postId: String): Boolean =
        postsAPI.hidePost(postId)

    override suspend fun showPost(postId: String): Boolean =
        postsAPI.showPost(postId)
}
