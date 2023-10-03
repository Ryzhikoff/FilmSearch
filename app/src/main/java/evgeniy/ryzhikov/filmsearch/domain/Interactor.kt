package evgeniy.ryzhikov.filmsearch.domain


import evgeniy.ryzhikov.filmsearch.data.API
import evgeniy.ryzhikov.filmsearch.data.MainRepository
import evgeniy.ryzhikov.filmsearch.data.PreferenceProvider
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.utils.Converter
import evgeniy.ryzhikov.remote_module.TmdbApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

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
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreference(), API.KEY, "ru-RU", page)
            .subscribeOn(Schedulers.io())
            .map {
                Converter.convertApiListToDtoList(it.tmdbFilms)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                },
                onNext = {
                    progressBarState.onNext(false)
                    repository.putToDB(it)
                }
            )
    }

    fun getSearchResultFromApi(search: String): Observable<List<Film>> =
        retrofitService.getFilmFromSearch(API.KEY, "ru-RU", search, 1)
            .map {
                Converter.convertApiListToDtoList(it.tmdbFilms)
            }


    fun getFilmFromDB(): Observable<List<Film>> = repository.getAllFromDB()


    fun saveDefaultCategoryToPreference(category: String) {
        preference.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreference() = preference.getDefaultCategory()
}