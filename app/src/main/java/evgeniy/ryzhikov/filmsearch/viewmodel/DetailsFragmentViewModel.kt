package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.domain.Film
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import javax.inject.Inject

class DetailsFragmentViewModel() : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun addToFavorite(film: Film) {
        interactor.changeFavoriteStatus(film, true)
    }

    fun removeFromFavorite(film: Film) {
        interactor.changeFavoriteStatus(film, false)
    }

    fun isFavorite(film: Film) = interactor.isFavorite(film)
}