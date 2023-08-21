package evgeniy.ryzhikov.filmsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import evgeniy.ryzhikov.filmsearch.data.dao.FilmDao
import evgeniy.ryzhikov.filmsearch.data.entity.Film

@Database(entities = [Film::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}