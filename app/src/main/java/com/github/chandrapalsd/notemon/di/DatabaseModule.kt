package com.github.chandrapalsd.notemon.di

import android.content.Context
import androidx.room.Room
import com.github.chandrapalsd.notemon.database.NoteDao
import com.github.chandrapalsd.notemon.database.NoteDatabase
import com.github.chandrapalsd.notemon.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext appContext: Context): NoteDatabase{
        return Room.databaseBuilder(
            appContext,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDb: NoteDatabase): NoteDao {
        return noteDb.getNoteDao()
    }

}