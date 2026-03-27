package com.project.momentum.features.account.repo

import com.project.momentum.features.account.api.GetAccountInfoAPI
import com.project.momentum.features.account.api.GetAccountMediaAPI
import com.project.momentum.features.account.models.AccountInformationDTO
import com.project.momentum.features.account.models.CheckUserInfoIsFreeRequestDTO
import com.project.momentum.features.account.models.EditAccountDTO
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.editingAccount.EditAccountErrorFields
import com.project.momentum.features.editingAccount.EditAccountFields
import com.project.momentum.features.editingAccount.ErrorType
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
            posts.add(
                PostData(
                    id = post.id,
                    userId = post.userId,
                    userName = post.userName,
                    title = post.title,
                    presignedURL = post.presignedURL,
                    avatarPresignedURL = post.avatarPresignedURL,
                    createdAt = post.createdAt
                )
            )
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
            EditAccountDTO(
                username = userInfo.username,
                email = userInfo.email,
                phone = userInfo.phone,
                profilePhotoURL = userInfo.profilePhotoURL
            )
        )
        return response
    }
}
