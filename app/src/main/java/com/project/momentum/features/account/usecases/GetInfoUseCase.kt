package com.project.momentum.features.account.usecases

import com.project.momentum.features.account.models.AccountInformationDTO
import com.project.momentum.features.account.repo.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetInfoUseCase @Inject constructor(
    private val repo: AccountRepository
) {
    suspend fun getMyInfo(): AccountInformationDTO? = repo.getMyInfo()
}