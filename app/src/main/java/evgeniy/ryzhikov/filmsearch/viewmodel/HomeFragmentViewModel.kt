package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor : Interactor
    val filmsListData : Observable<List<Film>>
    val showProgressBar: BehaviorSubject<Boolean>

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        filmsListData = interactor.getFilmFromDB()
        getFilms()

    }

    fun getFilms() {
        interactor.getFilmFromApi(1)
    }

    fun getSearchResult(search: String) = interactor.getSearchResultFromApi(search)

//    interface ApiCallBack {
//        fun onSuccess()
//        fun onFailure()
//    }


}

