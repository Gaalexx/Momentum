package com.project.momentum.domain.auth.usecase

import com.project.momentum.domain.auth.repository.RegistrationRepository
import javax.inject.Inject

/** Persists the refresh credential, restores the session, then registers push delivery. */
class CompleteSignInUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(refreshToken: String) {
        repository.saveToken(refreshToken)
        repository.authorize()
        repository.syncPushToken()
    }
}
