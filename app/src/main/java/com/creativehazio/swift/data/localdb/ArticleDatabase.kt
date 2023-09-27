package com.creativehazio.swift.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.creativehazio.swift.domain.model.Article
import com.creativehazio.swift.domain.model.OfflineArticle

@Database(
    entities = [Article::class, OfflineArticle::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao() : ArticleDAO

    companion object {
        @Volatile
        private var instance : ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).addMigrations(migration1to2)
                .build()

        private val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add SQL statements to modify the schema
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS offline_articles " +
                            "(url TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "htmlContent TEXT NOT NULL)")
            }
        }
    }

}