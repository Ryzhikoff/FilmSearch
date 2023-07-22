package evgeniy.ryzhikov.filmsearch.data

import evgeniy.ryzhikov.filmsearch.BuildConfig
import evgeniy.ryzhikov.filmsearch.TMDB_TIMEOUT
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object Di {
    val mainModule = module {
        //создаем репозиторий
        single {MainRepository()}
        //создаем объект для получения данных из сети
        single<TmdbApi> {
            val okHttpClient = OkHttpClient.Builder()
            //настраиваем таймауты медленного интернета
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //добавляем логгер
                .addInterceptor(HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                })
                .build()

            //создаем ретрофит
            val retrofit = Retrofit.Builder()
                //базовый УРЛ из констант
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            //создаем сервис с методами для запросов
            retrofit.create(TmdbApi::class.java)
        }

        //создаем интерактор
        single { Interactor(get(), get()) }

    }
}