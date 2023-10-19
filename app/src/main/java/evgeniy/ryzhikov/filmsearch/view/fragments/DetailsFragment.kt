package evgeniy.ryzhikov.filmsearch.view.fragments

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentDetailsBinding
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.utils.FilmCircularProgressDrawable
import evgeniy.ryzhikov.filmsearch.utils.FilmCircularProgressDrawable.Location
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationHelper
import evgeniy.ryzhikov.filmsearch.viewmodel.DetailsFragmentViewModel
import evgeniy.ryzhikov.remote_module.entity.ApiConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    lateinit var film: Film
    private val viewModel : DetailsFragmentViewModel by viewModels()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailsBinding.bind(view)

        getAndSetFilm()
        initFavoritesButton()
        initShareButton()

        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadPoster()
        }

        binding.fabAddToNotification.setOnClickListener{
            NotificationHelper.createNotification(requireContext(), film)
        }

    }

    private fun getAndSetFilm() {
        //Получаем наш фильм из переданного бандла
        film = arguments?.get("film") as Film
        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title

        val circularProgressDrawable = FilmCircularProgressDrawable(requireContext(), Location.DETAIL_FRAGMENT)
        circularProgressDrawable.start()

        //Устанавливаем картинку
        Glide.with(this)
            .load(ApiConstants.IMAGE_URL + ApiConstants.SIZE_IMAGE_DETAILS_FILM + film.poster)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(binding.detailsPoster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
    }

    private fun initFavoritesButton() {
        binding.fabFavorites.setImageResource(
            if (film.isInFavorites) {
                R.drawable.ic_baseline_favorite_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }
        )

        binding.fabFavorites.setOnClickListener {
            if (!film.isInFavorites) {
                binding.fabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                film.isInFavorites = true
            } else {
                binding.fabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                film.isInFavorites = false
            }
        }
    }

    private fun initShareButton() {
        binding.fabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, getString(R.string.details_share_to)))
        }
    }

    private fun performAsyncLoadPoster() {
        if (!checkPermission()) {
            requestPermission()
            return
        }
        //Создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI
        MainScope().launch {
            //включаем прогресс бар
            binding.progressBar.isVisible = true
            //Создаем через async, так как нам нужен результат от работы, то есть Bitmap
            val job = scope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGE_URL + "original" + film.poster)
            }
            //сохраням как только загрузится
            saveToGallery(job.await())
            //Снекбар с кнопкой перейти в галерею
            Snackbar.make(
                binding.root,
                R.string.downloaded_to_gallery,
                Snackbar.LENGTH_SHORT
            )
                .setAction(R.string.open) {
                    val intent = Intent()
                    intent.apply {
                        action = Intent.ACTION_VIEW
                        type = "image/*"
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
                .show()
            //Отключаем прогресс-бар
            binding.progressBar.isVisible = false
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    private fun saveToGallery(bitmap: Bitmap) {
        //Версия системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmSearchApp")
            }
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из приложения вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            //Открываем канал для записи на диск
            val outputStream = contentResolver.openOutputStream(uri!!)
            //Передаем нашу картинку, можем сделать компрессию
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.close()
        } else {
            //для старых ОС
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

}