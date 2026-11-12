package com.project.momentum.data.account.repository

import com.project.momentum.data.account.remote.CheckUserInfoIsFreeRequestDTO
import com.project.momentum.data.account.remote.EditAccountDTO
import com.project.momentum.data.account.remote.GetAccountInfoAPI
import com.project.momentum.domain.account.model.Account
import com.project.momentum.domain.account.model.AccountAvailability
import com.project.momentum.domain.account.model.AccountChanges
import com.project.momentum.domain.account.repository.AccountRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountInfoAPI: GetAccountInfoAPI
) : AccountRepository {
    override suspend fun getMyInfo(): Account? = try {
        accountInfoAPI.getAccountInformation().let {
            Account(it.userId, it.email, it.name, it.phone, it.profilePhotoURL, it.hasPremium)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }

    override suspend fun checkUserInfoDB(user: AccountChanges): AccountAvailability {
        val response = accountInfoAPI.checkUserInfoIsFree(
            CheckUserInfoIsFreeRequestDTO(user.username, user.email, user.phone)
        )
        return AccountAvailability(
            isUsernameFree = response.isUsernameFree ?: true,
            isEmailFree = response.isEmailFree ?: true,
            isPhoneFree = response.isPhoneFree ?: true
        )
    }

    override suspend fun updateUserInfo(userInfo: AccountChanges): AccountChanges {
        val response = accountInfoAPI.sendNewUserInfo(
            EditAccountDTO(userInfo.username, userInfo.email, userInfo.phone, userInfo.profilePhotoURL)
        )
        return AccountChanges(response.username, response.email, response.phone, response.profilePhotoURL)
    }
}
