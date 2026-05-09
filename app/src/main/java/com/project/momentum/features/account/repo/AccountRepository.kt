package com.project.momentum.features.account.repo

import com.project.momentum.features.account.api.GetAccountInfoAPI
import com.project.momentum.features.account.models.AccountInformationDTO
import com.project.momentum.features.account.models.CheckUserInfoIsFreeRequestDTO
import com.project.momentum.features.account.models.EditAccountDTO
import com.project.momentum.features.editingAccount.viewmodel.EditAccountErrorFields
import com.project.momentum.features.editingAccount.viewmodel.EditAccountFields
import com.project.momentum.features.editingAccount.viewmodel.ErrorType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountInfoAPI: GetAccountInfoAPI
) {
    suspend fun getMyInfo(): AccountInformationDTO? {
        return try {
            accountInfoAPI.getAccountInformation()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkUserInfoDB(user: EditAccountFields): EditAccountErrorFields {
        val response = accountInfoAPI.checkUserInfoIsFree(
            CheckUserInfoIsFreeRequestDTO(
                username = user.username,
                email = user.email,
                phone = user.phone
            )
        )
        return EditAccountErrorFields(
            usernameError =
                if (response.isUsernameFree ?: true) null
                else ErrorType.ALREADY_EXIST,
            emailError =
                if (response.isEmailFree ?: true) null
                else ErrorType.ALREADY_EXIST,
            phoneError =
                if (response.isPhoneFree ?: true) null
                else ErrorType.ALREADY_EXIST
        )
    }

    suspend fun updateUserInfo(userInfo: EditAccountFields): EditAccountDTO {
        val response = accountInfoAPI.sendNewUserInfo(
            EditAccountDTO (
                username = userInfo.username,
                email = userInfo.email,
                phone = userInfo.phone,
                profilePhotoURL = null
            )
        )
        return response
    }
}
