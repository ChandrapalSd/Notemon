package com.github.chandrapalsd.notemon.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.chandrapalsd.notemon.models.Note
import com.github.chandrapalsd.notemon.utils.Converters
import com.github.chandrapalsd.notemon.utils.DATABASE_NAME

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    //Singleton
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE != null){
                    INSTANCE
                }
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE!!
            }

        }

    }

}