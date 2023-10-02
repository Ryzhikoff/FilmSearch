package evgeniy.ryzhikov.filmsearch.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import evgeniy.ryzhikov.filmsearch.databinding.FilmItemBinding
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.utils.FilmCircularProgressDrawable
import evgeniy.ryzhikov.remote_module.entity.ApiConstants

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = FilmItemBinding.bind(itemView)
    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film) {
        //Устанавливаем заголовок
        binding.title.text = film.title

        //Устанавливаем постер
        val circularProgressDrawable = FilmCircularProgressDrawable(binding.root.context, FilmCircularProgressDrawable.Location.MAIN_RECYCLER_VIEW)
        circularProgressDrawable.start()
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(ApiConstants.IMAGE_URL + "w342" + film.poster)
            // Анимация при загрузки изображения
            .placeholder(circularProgressDrawable)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.poster)
        //Устанавливаем описание
        binding.description.text = film.description
        binding.rating.setProgress((film.rating * 10).toInt())
    }
}