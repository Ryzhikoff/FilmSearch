package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor : Interactor
    val filmsListLiveData : LiveData<List<Film>>
    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = interactor.getFilmFromDB()
        getFilms()

    }

    fun getFilms() {
        showProgressBar.postValue(true)
        interactor.getFilmFromApi(1, object : ApiCallBack {
            override fun onSuccess() {
                showProgressBar.postValue(false)
            }

            override fun onFailure() {
                showProgressBar.postValue(false)
            }

        })

    }

    interface ApiCallBack {
        fun onSuccess()
        fun onFailure()
    }


}

