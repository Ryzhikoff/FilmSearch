package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.domain.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import javax.inject.Inject

class FavoritesFragmentViewModel() : ViewModel() {

    val favoriteListLiveData = MutableLiveData<List<Film>>()
    @Inject
    lateinit var interactor : Interactor

    init {
        App.instance.dagger.inject(this)
        getFavoriteList()
    }

    fun getFavoriteList() {
        favoriteListLiveData.postValue(interactor.getFavoriteFilmsFromDB())
    }
}