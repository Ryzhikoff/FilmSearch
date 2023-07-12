package evgeniy.ryzhikov.filmsearch.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

class FilmCircularProgressDrawable(context: Context, location: Location) :
    CircularProgressDrawable(context) {

    enum class Location {
        DETAIL_FRAGMENT,
        MAIN_RECYCLER_VIEW
    }

    init {
        when (location) {
            Location.DETAIL_FRAGMENT -> {
                centerRadius = 170f
                strokeWidth = 20f
            }

            Location.MAIN_RECYCLER_VIEW -> {
                centerRadius = 50f
                strokeWidth = 15f
            }
        }
    }

}

