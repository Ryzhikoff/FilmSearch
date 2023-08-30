package evgeniy.ryzhikov.filmsearch.domain


import androidx.lifecycle.LiveData
import evgeniy.ryzhikov.filmsearch.data.API
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.PreferenceProvider
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.data.entity.TmdbResultsDto
import evgeniy.ryzhikov.filmsearch.utils.Converter
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Interactor(private val repository: MainRepository, private val retrofitService: TmdbApi, private val preference: PreferenceProvider) {

    //В коструктор передаем коллбек из вью модели, что бы реагировать на то, когда фильмы будут получены
    //И номер страницы для Пагинации
    fun getFilmFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallBack) {
        retrofitService.getFilms(getDefaultCategoryFromPreference(), API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto>{
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                //при успехе вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
                repository.putToDB(list)
                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }

        })
    }

    fun getFilmFromDB() : LiveData<List<Film>> {
        return repository.getAllFromDB()
    }
    fun saveDefaultCategoryToPreference(category: String) {
        preference.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreference() = preference.getDefaultCategory()
}