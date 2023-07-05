package evgeniy.ryzhikov.filmsearch.domain

import evgeniy.ryzhikov.filmsearch.data.MainRepository

class Interactor(val repository: MainRepository) {
    fun getFilmsDB(): List<Film> = repository.filmsDataBase
}