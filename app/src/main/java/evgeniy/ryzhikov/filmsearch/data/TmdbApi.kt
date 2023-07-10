package evgeniy.ryzhikov.filmsearch.data

import evgeniy.ryzhikov.filmsearch.data.entity.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/popular")
    fun getFilms(
        @Query("api_key") apiKey : String,
        @Query("language") landuage: String,
        @Query("page") page: Int
    ) : Call<TmdbResultsDto>
}