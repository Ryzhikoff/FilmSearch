package evgeniy.ryzhikov.filmsearch.data

import android.content.ContentValues
import android.database.Cursor
import evgeniy.ryzhikov.filmsearch.data.db.DatabaseHelper
import evgeniy.ryzhikov.filmsearch.domain.Film

class MainRepository(databaseHelper: DatabaseHelper) {
    private val sqlDb = databaseHelper.readableDatabase
    private lateinit var cursor: Cursor

    fun putToDb(film: Film) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
            //новый фильм по умолчанию - НЕ в избранном
            put(DatabaseHelper.COLUMN_FAVORITE, 0)
        }

        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }

    fun getAllFromDb() : List<Film> {
        //курсор - запрос ПОЛУЧИТЬ ВСЕ
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)

        return cursorToListFilms(cursor)
    }

    /**
     * Add or remove the "Favorite" flag.
     *
     * @param film object film
     * @param value true - Add flag "Favorite", false - remove flag "Favorite"
     */
    fun changeFavoriteStatus(film: Film, value: Boolean) {
        val cv = ContentValues()
        cv.put(DatabaseHelper.COLUMN_FAVORITE, booleanToInt(value))
        sqlDb.update(DatabaseHelper.TABLE_NAME, cv, DatabaseHelper.COLUMN_TITLE + "=" + "?", arrayOf(film.title))
    }

    fun isFavorite(film: Film) : Boolean {
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_TITLE} = ?", arrayOf(film.title))
        if (cursor.moveToFirst()) {
            return cursor.getInt(5) == 1
        }
        return false
    }

    fun getFavoritesFilms() : List<Film> {
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_FAVORITE} = 1", null)
        return cursorToListFilms(cursor)
    }

    private fun cursorToListFilms (cursor: Cursor) : MutableList<Film> {
        val result = mutableListOf<Film>()
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                val favorite = cursor.getInt(5)
                result.add(Film(title, poster, description, rating, intToBoolean(favorite)))
            } while (cursor.moveToNext())
        }
        return result
    }

    private fun intToBoolean(int: Int): Boolean {
        return int == 1
    }

    private fun booleanToInt (boolean: Boolean) : Int {
        return if (boolean) 1 else 0
    }
}