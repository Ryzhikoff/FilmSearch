package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.data.ApiConstants
import evgeniy.ryzhikov.filmsearch.data.Di
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TMDB_TIMEOUT = 30L
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            //прикрепляем контекст
            androidContext(this@App)
            //подключаем логгер
            androidLogger()
            //инициализируем модули
            modules(listOf(Di.mainModule))
        }
    }

}