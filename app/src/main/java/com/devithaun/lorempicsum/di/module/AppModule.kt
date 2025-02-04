package com.devithaun.lorempicsum.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.devithaun.data.network.PhotoApi
import com.devithaun.data.repository.PhotoRepositoryImpl
import com.devithaun.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): PhotoApi {
        return Retrofit.Builder()
            .baseUrl(PhotoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.filesDir.resolve("user_settings.preferences_pb") }
        )
    }

    @Provides
    @Singleton
    fun providePhotoRepository(api: PhotoApi, dataStore: DataStore<Preferences>): PhotoRepository {
        return PhotoRepositoryImpl(api, dataStore)
    }
}