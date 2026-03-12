package com.project.momentum.features.account.repo

import com.project.momentum.features.account.api.GetAccountMediaAPI
import com.project.momentum.network.s3.PostDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val clientAPI: GetAccountMediaAPI
) {
    suspend fun getAllPosts(): List<PostDTO> = clientAPI.getAllPosts()
}