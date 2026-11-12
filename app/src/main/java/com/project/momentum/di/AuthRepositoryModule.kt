package com.project.momentum.di

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.auth.repository.EmailCheckerImpl
import com.project.momentum.data.auth.repository.RegistrationRepositoryImpl
import com.project.momentum.data.settings.repository.DeleteAccountRepositoryImpl
import com.project.momentum.data.settings.repository.ServerSettingsRepositoryImpl
import com.project.momentum.domain.auth.repository.EmailChecker
import com.project.momentum.domain.auth.repository.RegistrationRepository
import com.project.momentum.domain.auth.repository.UserSession
import com.project.momentum.domain.settings.repository.DeleteAccountRepository
import com.project.momentum.domain.settings.repository.ServerSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun bindRegistrationRepository(implementation: RegistrationRepositoryImpl): RegistrationRepository

    @Binds
    abstract fun bindEmailChecker(implementation: EmailCheckerImpl): EmailChecker

    @Binds
    abstract fun bindUserSession(implementation: SessionManager): UserSession

    @Binds
    abstract fun bindServerSettingsRepository(implementation: ServerSettingsRepositoryImpl): ServerSettingsRepository

    @Binds
    abstract fun bindDeleteAccountRepository(implementation: DeleteAccountRepositoryImpl): DeleteAccountRepository
}
