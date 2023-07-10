package evgeniy.ryzhikov.filmsearch

import android.app.Application
import evgeniy.ryzhikov.filmsearch.data.ApiConstants
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TMDB_TIMEOUT = 30L
class App : Application() {
    lateinit var repository: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this

        //создаем кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //тайм ауты
            .callTimeout(TMDB_TIMEOUT,TimeUnit.SECONDS)
            .readTimeout(TMDB_TIMEOUT, TimeUnit.SECONDS)
            //логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val retrofitService = retrofit.create(TmdbApi::class.java)

        repository = MainRepository()
        interactor = Interactor(repository, retrofitService)
    }

    companion object {
        //здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }
}