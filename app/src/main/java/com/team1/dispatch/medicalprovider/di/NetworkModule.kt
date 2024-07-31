package com.team1.dispatch.medicalprovider.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.team1.dispatch.medicalprovider.network.ApiInterface
import com.team1.dispatch.medicalprovider.network.LiveDataCallAdapterFactory
import com.team1.dispatch.medicalprovider.network.SocketIOManager
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.BASE_URL
import com.team1.dispatch.medicalprovider.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Singleton
    @Provides
    fun provideHttpClientBuilder(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sessionManager: SessionManager,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(interceptor = { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Accept", "application/json")
                requestBuilder.header("Accept-language", sessionManager.getLanguage())
                if (sessionManager.getToken().isNotEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer ${sessionManager.getToken()}")
                chain.proceed(requestBuilder.build())
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson, httpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideApiInterfaceService(retrofitBuilder: Retrofit.Builder): ApiInterface {
        return retrofitBuilder
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideSocketIOManager(
        sessionManager: SessionManager
    ): SocketIOManager {
        return SocketIOManager(sessionManager)
    }
}