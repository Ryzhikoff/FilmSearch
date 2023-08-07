package evgeniy.ryzhikov.filmsearch.domain

interface ApiCallBack {
    fun onSuccess(films: List<Film>)
    fun onFailure()
}