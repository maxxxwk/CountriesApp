package com.example.countriesapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherDefault
