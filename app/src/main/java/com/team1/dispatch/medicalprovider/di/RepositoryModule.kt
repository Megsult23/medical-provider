package com.team1.dispatch.medicalprovider.di

import com.team1.dispatch.medicalprovider.utils.SessionManager
import com.team1.dispatch.medicalprovider.network.ApiInterface
import com.team1.dispatch.medicalprovider.repositories.repo_impl.AuthRepositoryImpl
import com.team1.dispatch.medicalprovider.repositories.repo_interface.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesAuthRepository(
        apiInterface: ApiInterface,
        sessionManager: SessionManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            apiInterface = apiInterface,
            sessionManager = sessionManager
        )
    }
}