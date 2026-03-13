package com.project.momentum.features.account.usecases

import com.project.momentum.features.account.repo.AccountRepository
import com.project.momentum.network.s3.PostDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMediaUseCase @Inject constructor(
    private val repo: AccountRepository
) {
    suspend fun getAllPosts(): List<PostDTO> = repo.getAllPosts()
}