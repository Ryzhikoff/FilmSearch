package evgeniy.ryzhikov.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import evgeniy.ryzhikov.filmsearch.view.MainActivity
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentFavoritesBinding
import evgeniy.ryzhikov.filmsearch.domain.Film
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.TopSpacingItemDecoration
import evgeniy.ryzhikov.filmsearch.utils.AnimationHelper

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Получаем список при транзакции фрагмента
        val favoritesList: List<Film> = emptyList()

        binding.favoritesRV
            .apply {
                filmsAdapter =
                    FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                        override fun click(film: Film) {
                            (requireActivity() as MainActivity).launchDetailsFragment(film)
                        }
                    })
                //Присваиваем адаптер
                adapter = filmsAdapter
                //Присвои layoutmanager
                layoutManager = LinearLayoutManager(requireContext())
                //Применяем декоратор для отступов
                val decorator = TopSpacingItemDecoration((requireActivity().resources.getDimension(R.dimen.rv_margin).toInt()))
                addItemDecoration(decorator)
            }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(favoritesList)

        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}