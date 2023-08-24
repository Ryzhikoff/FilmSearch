package evgeniy.ryzhikov.filmsearch.data

import androidx.lifecycle.LiveData
import evgeniy.ryzhikov.filmsearch.data.dao.FilmDao
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {
    fun putToDB(films: List<Film>) {
        //запросы в БД в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): LiveData<List<Film>> {
        return filmDao.getCashedFilms()
    }
}