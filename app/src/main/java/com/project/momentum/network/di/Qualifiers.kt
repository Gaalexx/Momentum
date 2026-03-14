package com.project.momentum.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Backend

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class S3

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EmailChecker
