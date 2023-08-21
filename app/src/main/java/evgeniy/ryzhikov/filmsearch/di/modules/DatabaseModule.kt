package evgeniy.ryzhikov.filmsearch.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.data.AppDatabase
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.dao.FilmDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).build().filmDao()

    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao) = MainRepository(filmDao)
}