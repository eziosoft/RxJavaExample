package com.netguru.presentation

import android.content.Context
import com.netguru.data.MovieRepository
import com.netguru.data.remote.OpenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient =
        OkHttpClient.Builder()
            .cache(
                Cache(
                    File(appContext.cacheDir, "http_cache"),
                    50L * 1024L * 1024L // 50 MiB
                )
            ).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(OpenApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideOpenApiClient(retrofit: Retrofit): OpenApi =
        retrofit.create(OpenApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(openApi: OpenApi): MovieRepository =
        MovieRepository(openApiClient = openApi)
}
