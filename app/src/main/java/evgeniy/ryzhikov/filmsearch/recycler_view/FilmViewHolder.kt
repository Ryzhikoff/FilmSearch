package evgeniy.ryzhikov.filmsearch.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import evgeniy.ryzhikov.filmsearch.databinding.FilmItemBinding

class FilmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = FilmItemBinding.bind(itemView)
    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film) {
        //Устанавливаем заголовок
        binding.title.text = film.title
        //Устанавливаем постер
        binding.poster.setImageResource(film.poster)
        //Устанавливаем описание
        binding.description.text = film.description
    }
}