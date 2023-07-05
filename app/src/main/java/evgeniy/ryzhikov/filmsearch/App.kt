package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.domain.Interactor

class App : Application() {
    lateinit var repository: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this
        repository = MainRepository()
        interactor = Interactor(repository)
    }

    companion object {
        //здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }
}