package evgeniy.ryzhikov.filmsearch.utils

import evgeniy.ryzhikov.filmsearch.data.entity.TmdbFilm
import evgeniy.ryzhikov.filmsearch.domain.Film

object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?) : List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    title = it.title,
                    poster = it.posterPath,
                    description = it.overview,
                    rating =  it.voteAverage,
                    isInFavorites = false
                )
            )
        }
        return result
    }
}