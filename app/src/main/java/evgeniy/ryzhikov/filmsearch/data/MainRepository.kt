package evgeniy.ryzhikov.filmsearch.data

import evgeniy.ryzhikov.filmsearch.data.dao.FilmDao
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import io.reactivex.rxjava3.core.Observable

class MainRepository(private val filmDao: FilmDao) {
    fun putToDB(films: List<Film>) {
        filmDao.insertAll(films)
    }

    fun getAllFromDB(): Observable<List<Film>> = filmDao.getCashedFilms()

}