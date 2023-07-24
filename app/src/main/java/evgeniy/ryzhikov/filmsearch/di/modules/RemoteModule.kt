package evgeniy.ryzhikov.filmsearch.di.modules

import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.filmsearch.BuildConfig
import evgeniy.ryzhikov.filmsearch.TMDB_TIMEOUT
import evgeniy.ryzhikov.filmsearch.data.ApiConstants
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder()
        //Настраиваем таймаут для медленного интернета
        .callTimeout(TMDB_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TMDB_TIMEOUT, TimeUnit.SECONDS)
        //добавляем логгер
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        //базовый URL
        .baseUrl(ApiConstants.BASE_URL)
        //добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)
}