package com.project.momentum.di

import com.project.momentum.data.account.repository.AccountRepositoryImpl
import com.project.momentum.data.friends.repository.FriendsRepositoryImpl
import com.project.momentum.data.posts.repository.PostsRepositoryImpl
import com.project.momentum.domain.account.repository.AccountRepository
import com.project.momentum.domain.account.usecase.GetInfoUseCase
import com.project.momentum.domain.friends.repository.FriendsRepository
import com.project.momentum.domain.posts.repository.PostsRepository
import com.project.momentum.domain.posts.usecase.TranscribePostUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocialRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAccountRepository(implementation: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindFriendsRepository(implementation: FriendsRepositoryImpl): FriendsRepository

    @Binds
    @Singleton
    abstract fun bindPostsRepository(implementation: PostsRepositoryImpl): PostsRepository

    companion object {
        @Provides
        fun provideGetInfoUseCase(repository: AccountRepository): GetInfoUseCase = GetInfoUseCase(repository)

        @Provides
        fun provideTranscribePostUseCase(repository: PostsRepository): TranscribePostUseCase =
            TranscribePostUseCase(repository)
    }
}
