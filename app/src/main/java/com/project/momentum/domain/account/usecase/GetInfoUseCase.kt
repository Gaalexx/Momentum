package com.project.momentum.domain.account.usecase

import com.project.momentum.domain.account.model.Account
import com.project.momentum.domain.account.repository.AccountRepository

class GetInfoUseCase(private val repository: AccountRepository) {
    suspend fun getMyInfo(): Account? = repository.getMyInfo()
}
