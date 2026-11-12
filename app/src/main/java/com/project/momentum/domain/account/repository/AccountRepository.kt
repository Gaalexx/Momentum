package com.project.momentum.domain.account.repository

import com.project.momentum.domain.account.model.Account
import com.project.momentum.domain.account.model.AccountChanges
import com.project.momentum.domain.account.model.AccountAvailability

interface AccountRepository {
    suspend fun getMyInfo(): Account?
    suspend fun checkUserInfoDB(user: AccountChanges): AccountAvailability
    suspend fun updateUserInfo(userInfo: AccountChanges): AccountChanges
}
