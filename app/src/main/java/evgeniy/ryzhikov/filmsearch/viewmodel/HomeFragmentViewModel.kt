package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.domain.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    private var interactor = App.instance.interactor

    init {
        interactor.getFilmFromApi(1, object : ApiCallBack {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }

        })
    }

    interface ApiCallBack {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}