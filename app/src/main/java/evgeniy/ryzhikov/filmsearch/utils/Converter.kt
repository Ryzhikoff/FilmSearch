package evgeniy.ryzhikov.filmsearch.utils

import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.remote_module.entity.TmdbFilm

object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?) : List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            val releaseYear = it.releaseDate.split("-")[0]
            result.add(
                Film(
                    title = "${it.title} ($releaseYear)",
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