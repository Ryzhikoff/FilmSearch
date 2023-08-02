package evgeniy.ryzhikov.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentSettingsBinding
import evgeniy.ryzhikov.filmsearch.utils.AnimationHelper
import evgeniy.ryzhikov.filmsearch.viewmodel.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Подключаем анимации и передаем номер позиции у кнопки в нижнем меню
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 5)
        //Слушаем, какой у нас сейчас выбран вариант в настройках
        viewModel.categoryPropertyLifeData.observe(viewLifecycleOwner, Observer<String> {
            when(it) {
                POPULAR_CATEGORY -> binding.radioGroup.check(R.id.radioPopular)
                TOP_RATED_CATEGORY -> binding.radioGroup.check(R.id.radioTopRated)
                UPCOMING_CATEGORY -> binding.radioGroup.check(R.id.radioUpcoming)
                NOW_PLAYING_CATEGORY -> binding.radioGroup.check(R.id.radioNowPlaying)
            }
        })
        //Слушатель для отправки нового состояния в настройки
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioPopular -> viewModel.putCategoryProperty(POPULAR_CATEGORY)
                R.id.radioTopRated -> viewModel.putCategoryProperty(TOP_RATED_CATEGORY)
                R.id.radioUpcoming -> viewModel.putCategoryProperty(UPCOMING_CATEGORY)
                R.id.radioNowPlaying -> viewModel.putCategoryProperty(NOW_PLAYING_CATEGORY)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val POPULAR_CATEGORY = "popular"
        private const val TOP_RATED_CATEGORY = "top_rated"
        private const val UPCOMING_CATEGORY = "upcoming"
        private const val NOW_PLAYING_CATEGORY = "now_playing"
    }
}