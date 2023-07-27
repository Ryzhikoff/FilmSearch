package evgeniy.ryzhikov.filmsearch.di.modules

import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi) = Interactor(repository = repository, retrofitService = tmdbApi)
}