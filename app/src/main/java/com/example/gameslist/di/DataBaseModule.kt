package com.example.gameslist.di

import android.content.Context
import androidx.room.Room
import com.example.gameslist.data.datasource.local.GamesDao
import com.example.gameslist.data.datasource.local.GamesDatabase
import com.example.gameslist.repository.local.LocalGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideGamesDatabase(@ApplicationContext context: Context): GamesDatabase {
        return Room.databaseBuilder(
            context,
            GamesDatabase::class.java,
            "games.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideGamesDao(gamesDatabase: GamesDatabase): GamesDao =
        gamesDatabase.dao

    @Singleton
    @Provides
    fun provideLocalGameRepo(gamesDao: GamesDao): LocalGameRepository =
        LocalGameRepository(gamesDao)
}