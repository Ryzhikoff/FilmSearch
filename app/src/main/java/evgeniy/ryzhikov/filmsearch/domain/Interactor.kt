package evgeniy.ryzhikov.filmsearch.domain

import evgeniy.ryzhikov.filmsearch.data.API
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.data.entity.TmdbResultsDto
import evgeniy.ryzhikov.filmsearch.utils.Converter
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Interactor(private val repository: MainRepository, private val retrofitService: TmdbApi) {
    //В коструктор передаем коллбек из вью модели, что бы реагировать на то, когда фильмы будут получены
    //И номер страницы для Пагинации
    fun getFilmFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallBack) {
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto>{
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                //при успехе вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }

        })
    }

}