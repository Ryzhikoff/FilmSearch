package evgeniy.ryzhikov.filmsearch.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.db.DatabaseHelper
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}