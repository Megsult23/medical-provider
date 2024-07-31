package com.team1.dispatch.medicalprovider.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.team1.dispatch.medicalprovider.utils.PreferenceKeys.Companion.APP_PREFERENCES
import com.team1.dispatch.medicalprovider.utils.SessionManager
import com.team1.dispatch.medicalprovider.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context,
    ): SessionManager {
        return SessionManager(
            context = context,
            sharedPreferences = context.getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
    }

    @Singleton
    @Provides
//    @Named("provideGlideOptions")
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

}