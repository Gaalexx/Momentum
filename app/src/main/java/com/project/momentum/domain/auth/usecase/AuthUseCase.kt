package com.project.momentum.domain.auth.usecase

import com.project.momentum.domain.auth.repository.RegistrationRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

sealed interface AuthError {
    data object NoInternetConnectionError : AuthError
}

sealed interface AuthResult {
    data class Success(val token: String?) : AuthResult
    data class Error(val error: AuthError) : AuthResult
}

class AuthUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend fun authorize(): AuthResult = try {
        AuthResult.Success(registrationRepository.authorize())
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (error: Exception) {
        AuthResult.Error(AuthError.NoInternetConnectionError)
    }

    suspend fun unauthorize(): Boolean = registrationRepository.clearSession()

    suspend fun syncPushToken() {
        registrationRepository.syncPushToken()
    }
}
