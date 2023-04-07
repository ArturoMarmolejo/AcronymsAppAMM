package com.arturomarmolejo.acronymsappam.di

import com.arturomarmolejo.acronymsappam.rest.AcromineRepository
import com.arturomarmolejo.acronymsappam.rest.AcromineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesAcromineRepositoryImpl(
        acromineRepositoryImpl: AcromineRepositoryImpl
    ): AcromineRepository

}