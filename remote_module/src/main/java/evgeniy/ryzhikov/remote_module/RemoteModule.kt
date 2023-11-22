package evgeniy.ryzhikov.remote_module

import dagger.Module
import dagger.Provides
import evgeniy.ryzhikov.remote_module.entity.ApiConstants
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TMDB_TIMEOUT = 300L
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
            //поддержка RxJava
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)
}