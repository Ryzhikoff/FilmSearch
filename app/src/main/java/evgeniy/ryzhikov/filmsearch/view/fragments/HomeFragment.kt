package evgeniy.ryzhikov.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import evgeniy.ryzhikov.filmsearch.view.MainActivity
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentHomeBinding
import evgeniy.ryzhikov.filmsearch.domain.Film
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.TopSpacingItemDecoration
import evgeniy.ryzhikov.filmsearch.utils.AnimationHelper
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private var _homeFragmentBinding: FragmentHomeBinding? = null
    private val homeFragmentBinding get() = _homeFragmentBinding!!

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }

    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение кладем в переменную
            field = value
            filmsAdapter.addItems(field)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        initSearch()

        AnimationHelper.performFragmentCircularRevealAnimation(homeFragmentBinding.root, requireActivity(), 1)

        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>> {
            filmsDataBase = it
        })
    }

    private fun initSearch() {
        homeFragmentBinding.searchView.setOnClickListener {
            homeFragmentBinding.searchView.isIconified = false
        }

        homeFragmentBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText!!.isEmpty()) {
                    filmsAdapter.addItems(filmsDataBase)
                    return true
                }
                //Фильтруем список на поискк подходящих сочетаний
                val result = filmsDataBase.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.addItems(result)
                return true
            }
        })
    }

    private fun initRV() {
        //находим наш RV
        homeFragmentBinding.mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс
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
            val decorator = TopSpacingItemDecoration(
                (requireActivity().resources.getDimension(R.dimen.rv_margin).toInt())
            )
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
    }
}