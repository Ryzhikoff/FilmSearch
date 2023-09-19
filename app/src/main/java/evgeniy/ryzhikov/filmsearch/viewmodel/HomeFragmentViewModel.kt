package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor : Interactor
    val filmsListData : Flow<List<Film>>
    val showProgressBar: Channel<Boolean>

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarSate
        filmsListData = interactor.getFilmFromDB()
        getFilms()

    }

    fun getFilms() {
        interactor.getFilmFromApi(1)
    }

    interface ApiCallBack {
        fun onSuccess()
        fun onFailure()
    }


}

