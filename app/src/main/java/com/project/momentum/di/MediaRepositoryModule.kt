package com.project.momentum.di

import com.project.momentum.data.media.repository.MediaUploadRepositoryImpl
import com.project.momentum.domain.media.repository.MediaUploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaRepositoryModule {
    @Binds
    abstract fun bindMediaUploadRepository(implementation: MediaUploadRepositoryImpl): MediaUploadRepository
}
