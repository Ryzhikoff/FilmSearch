package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.di.AppComponent
import evgeniy.ryzhikov.filmsearch.di.DaggerAppComponent
import evgeniy.ryzhikov.filmsearch.di.modules.DatabaseModule
import evgeniy.ryzhikov.filmsearch.di.modules.DomainModule
import evgeniy.ryzhikov.remote_module.DaggerRemoteComponent


class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this
        //создаем компонент
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
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