package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.di.AppComponent
import evgeniy.ryzhikov.filmsearch.di.DaggerAppComponent

const val TMDB_TIMEOUT = 30L
class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this
        //создаем компанент
        dagger = DaggerAppComponent.create()


    }

    companion object {
        //здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }
}