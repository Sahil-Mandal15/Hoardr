package com.sahilarious.hoardr.di

import android.content.Context
import androidx.room.Room
import com.sahilarious.hoardr.data.db.HoardrDao
import com.sahilarious.hoardr.data.db.HoardrDatabase
import com.sahilarious.hoardr.data.repository.LinkRepositoryImpl
import com.sahilarious.hoardr.domain.repository.LinkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideHoardrDatabase(@ApplicationContext context: Context): HoardrDatabase {
        return Room.databaseBuilder(
            context,
            HoardrDatabase::class.java,
            "hoardr_db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun provideHoardrDao(hoardrDatabase: HoardrDatabase) = hoardrDatabase.hoardrDao()

    @Provides
    fun provideLinkRepository(
        hoardrDao: HoardrDao,
        context: Context,
        @ScraperClient scraperClient: OkHttpClient
    ): LinkRepository = LinkRepositoryImpl(hoardrDao, scraperClient)
}
