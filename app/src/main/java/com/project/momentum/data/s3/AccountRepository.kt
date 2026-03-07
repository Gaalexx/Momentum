package com.project.momentum.data.s3

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountRepository @Inject constructor(
    private val clientAPI: GetAccountMediaClient
) {
    suspend fun getAllPosts(): List<PostDTO> = clientAPI.getAllPosts()
}