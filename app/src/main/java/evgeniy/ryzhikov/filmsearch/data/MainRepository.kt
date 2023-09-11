package evgeniy.ryzhikov.filmsearch.data

import evgeniy.ryzhikov.filmsearch.data.dao.FilmDao
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import kotlinx.coroutines.flow.Flow

class MainRepository(private val filmDao: FilmDao) {
    fun putToDB(films: List<Film>) {
        filmDao.insertAll(films)
    }

    fun getAllFromDB(): Flow<List<Film>> {
        return filmDao.getCashedFilms()
    }
}