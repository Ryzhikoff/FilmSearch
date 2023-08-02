package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.di.AppComponent
import evgeniy.ryzhikov.filmsearch.di.DaggerAppComponent
import evgeniy.ryzhikov.filmsearch.di.modules.DatabaseModule
import evgeniy.ryzhikov.filmsearch.di.modules.DomainModule
import evgeniy.ryzhikov.filmsearch.di.modules.RemoteModule

const val TMDB_TIMEOUT = 30L
class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this
        //создаем компанент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()


    }

    companion object {
        //здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }
}