package evgeniy.ryzhikov.filmsearch.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.PreferenceProvider
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import evgeniy.ryzhikov.remote_module.TmdbApi
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземплр SharedPreference
    fun providePreference(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repository = repository, retrofitService = tmdbApi, preference = preferenceProvider)
}