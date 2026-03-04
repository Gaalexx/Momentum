package com.project.momentum.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Backend

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class S3
