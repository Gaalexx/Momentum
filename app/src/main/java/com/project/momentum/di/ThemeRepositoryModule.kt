package com.project.momentum.di

import com.project.momentum.data.settings.local.ThemeRepositoryImpl
import com.project.momentum.domain.settings.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeRepositoryModule {
    @Binds
    abstract fun bindThemeRepository(implementation: ThemeRepositoryImpl): ThemeRepository
}
