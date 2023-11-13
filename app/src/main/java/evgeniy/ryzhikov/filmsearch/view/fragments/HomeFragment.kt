package evgeniy.ryzhikov.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentHomeBinding
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import evgeniy.ryzhikov.filmsearch.view.rv_adapters.TopSpacingItemDecoration
import evgeniy.ryzhikov.filmsearch.utils.AnimationHelper
import evgeniy.ryzhikov.filmsearch.utils.AutoDisposable
import evgeniy.ryzhikov.filmsearch.utils.addTo
import evgeniy.ryzhikov.filmsearch.view.MainActivity
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private var _homeFragmentBinding: FragmentHomeBinding? = null
    private val homeFragmentBinding get() = _homeFragmentBinding!!

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }

    private val autoDisposable = AutoDisposable()

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
        autoDisposable.bindTo(lifecycle)
        initRV()
        initSearch()

        AnimationHelper.performFragmentCircularRevealAnimation(homeFragmentBinding.root, requireActivity(), 1)



        viewModel.filmsListData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                filmsAdapter.addItems(list)
                filmsDataBase = list
            }
            .addTo(autoDisposable)

        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                homeFragmentBinding.progressBar.isVisible = it
            }
            .addTo(autoDisposable)

        initPullToRefresh()
    }

    private fun initSearch() {
        homeFragmentBinding.searchView.setOnClickListener {
            homeFragmentBinding.searchView.isIconified = false
        }
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            homeFragmentBinding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                //вызывается на ввод символов
                override fun onQueryTextChange(newText: String): Boolean {
                    filmsAdapter.clear()
                    subscriber.onNext(newText)
                    return false
                }

                //вызывается при кнопке Поиск
                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            }
            )
        })
            .subscribeOn(Schedulers.io())
            .map { query ->
                query.lowercase(Locale.getDefault()).trim()
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .filter { query ->
                //если поле пустое возвращаем список по умолчанию
                viewModel.getFilms()
                query.isNotBlank()
            }
            .flatMap { query ->
                viewModel.getSearchResult(query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(requireContext(), resources.getString(R.string.error_search_query), Toast.LENGTH_SHORT).show()
                },
                onNext = {films ->
                    filmsAdapter.addItems(films)
                }
            )
            .addTo(autoDisposable)
    }
//    private fun initSearch() {
//        homeFragmentBinding.searchView.setOnClickListener {
//            homeFragmentBinding.searchView.isIconified = false
//        }
//
//        homeFragmentBinding.searchView.setOnQueryTextListener(object :
//            SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                //Если ввод пуст то вставляем в адаптер всю БД
//                if (newText!!.isEmpty()) {
//                    filmsAdapter.addItems(filmsDataBase)
//                    return true
//                }
//                //Фильтруем список на поискк подходящих сочетаний
//                val result = filmsDataBase.filter {
//                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
//                    it.title.lowercase(Locale.getDefault())
//                        .contains(newText.lowercase(Locale.getDefault()))
//                }
//                //Добавляем в адаптер
//                filmsAdapter.addItems(result)
//                return true
//            }
//        })
//    }

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

    private fun initPullToRefresh() {
        //Вешаем слушатель, чтобы вызвался pull to refresh
        homeFragmentBinding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.clear()
            //Делаем новый запрос фильмов на сервер
            viewModel.getFilms()
            //Убираем крутящееся колечко
            homeFragmentBinding.pullToRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _homeFragmentBinding = null
    }
}