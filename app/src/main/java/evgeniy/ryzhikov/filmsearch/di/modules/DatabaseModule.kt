package evgeniy.ryzhikov.filmsearch.di.modules

import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}