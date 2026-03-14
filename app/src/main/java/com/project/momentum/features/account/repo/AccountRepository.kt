package com.project.momentum.features.account.repo

import com.project.momentum.features.account.api.GetAccountInfoAPI
import com.project.momentum.features.account.api.GetAccountMediaAPI
import com.project.momentum.features.account.models.AccountInformationDTO
import com.project.momentum.features.account.models.PostData
import com.project.momentum.network.s3.PostDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val mediaAPI: GetAccountMediaAPI,
    private val accountInfoAPI: GetAccountInfoAPI
) {
    suspend fun getAllPosts(): List<PostData> {
        val listOfDTOs = mediaAPI.getAllPosts()
        val posts: MutableList<PostData> = mutableListOf()
        listOfDTOs.forEach { post ->
            posts.add(PostData(post.id, post.userId, post.title, post.presignedURL, post.createdAt))
        }
        return posts
    }

    suspend fun getMyInfo(): AccountInformationDTO? {
        return try {
            accountInfoAPI.getAccountInformation()
        } catch (e: Exception) {
            null
        }
    }

}