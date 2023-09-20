package evgeniy.ryzhikov.filmsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import io.reactivex.rxjava3.core.Observable

@Dao
interface FilmDao {
    //запрос на всю таблицу
    @Query("SELECT * FROM cashed_films")
    fun getCashedFilms(): Observable<List<Film>>

    //Кладем спископ в БД, в случае конфликта - перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

}