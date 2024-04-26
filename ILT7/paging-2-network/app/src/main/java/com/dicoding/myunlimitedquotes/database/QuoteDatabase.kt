package com.dicoding.myunlimitedquotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.myunlimitedquotes.network.QuoteResponseItem

// TODO jangan lupa untuk menambahkannya juga di QuoteDatabase karena kita menambahkan entity dan DAO baru.
@Database(
    entities = [QuoteResponseItem::class],
    version = 1,
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {
    // TODO Tambahkan QuoteDao ke dalam QuoteDatabase
    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}