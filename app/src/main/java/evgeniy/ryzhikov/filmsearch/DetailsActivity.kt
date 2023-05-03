package evgeniy.ryzhikov.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import evgeniy.ryzhikov.filmsearch.databinding.ActivityDetailsBinding
import evgeniy.ryzhikov.filmsearch.recycler_view.Film

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAndSetFilm()
    }

    private fun getAndSetFilm() {
        //Получаем наш фильм из переданного бандла
        val film = intent.extras?.get("film") as Film
        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title
        //Устанавливаем картинку
        binding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
    }
}