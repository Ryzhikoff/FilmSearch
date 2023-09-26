package evgeniy.ryzhikov.filmsearch.domain


import evgeniy.ryzhikov.filmsearch.data.API
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.PreferenceProvider
import evgeniy.ryzhikov.filmsearch.data.TmdbApi
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.data.entity.TmdbResultsDto
import evgeniy.ryzhikov.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Interactor(
    private val repository: MainRepository,
    private val retrofitService: TmdbApi,
    private val preference: PreferenceProvider
) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    //В коструктор передаем коллбек из вью модели, что бы реагировать на то, когда фильмы будут получены
    //И номер страницы для Пагинации
    fun getFilmFromApi(page: Int) {
        progressBarState.onNext(true)
        retrofitService.getFilms(getDefaultCategoryFromPreference(), API.KEY, "ru-RU", page)
            .enqueue(object : Callback<TmdbResultsDto> {
                override fun onResponse(
                    call: Call<TmdbResultsDto>,
                    response: Response<TmdbResultsDto>
                ) {
                    //при успехе вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                    val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
                    Completable.fromSingle<List<Film>> {
                        repository.putToDB(list)
                    }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    progressBarState.onNext(false)
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    progressBarState.onNext(false)
                }

            })
    }

    fun getFilmFromDB(): Observable<List<Film>> = repository.getAllFromDB()


    fun saveDefaultCategoryToPreference(category: String) {
        preference.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreference() = preference.getDefaultCategory()
}