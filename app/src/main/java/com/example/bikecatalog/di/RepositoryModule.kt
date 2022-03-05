package com.example.bikecatalog.di

import com.example.bikecatalog.repos.BikesRepository
import com.example.bikecatalog.repos.BikesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bikeRepository(mainRepositoryImpl: BikesRepositoryImpl): BikesRepository
}