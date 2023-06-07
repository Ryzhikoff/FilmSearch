package evgeniy.ryzhikov.filmsearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import evgeniy.ryzhikov.filmsearch.MainActivity
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.FragmentHomeBinding
import evgeniy.ryzhikov.filmsearch.recycler_view.Film
import evgeniy.ryzhikov.filmsearch.recycler_view.FilmListRecyclerAdapter
import evgeniy.ryzhikov.filmsearch.recycler_view.TopSpacingItemDecoration
import evgeniy.ryzhikov.filmsearch.utils.AnimationHelper
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var filmsDataBase: List<Film>
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private var _homeFragmentBinding: FragmentHomeBinding? = null
    private val homeFragmentBinding get() = _homeFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFilmBase()
        initRV()
        initSearch()

        AnimationHelper.performFragmentCircularRevealAnimation(homeFragmentBinding.root, requireActivity(), 1)
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

    //для title и description не создавал строковые ресурсы. Эти данные ведь временно..
    private fun createFilmBase() {
        filmsDataBase = listOf(
            Film(
                "Зеленая миля (1999)",
                R.drawable.film_1,
                "В тюрьме для смертников появляется заключенный с божественным даром. Мистическая драма по роману Стивена Кинга"
            ),
            Film(
                "Список Шиндлера (1993)",
                R.drawable.film_2,
                "История немецкого промышленника, спасшего тысячи жизней во время Холокоста. Драма Стивена Спилберга"
            ),
            Film(
                "Побег из Шоушенка (1994)",
                R.drawable.film_3,
                "Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решётки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, обладающий живым умом и доброй душой, находит подход как к заключённым, так и к охранникам, добиваясь их особого к себе расположения."
            ),
            Film(
                "Форрест Гамп (1994)",
                R.drawable.film_4,
                "Полувековая история США глазами чудака из Алабамы. Абсолютная классика Роберта Земекиса с Томом Хэнксом"
            ),
            Film(
                "Тайна Коко (2017)",
                R.drawable.film_5,
                "12-летний Мигель живёт в мексиканской деревушке в семье сапожников и тайно мечтает стать музыкантом. Тайно, потому что в его семье музыка считается проклятием. Когда-то его прапрадед оставил жену, прапрабабку Мигеля, ради мечты, которая теперь не даёт спокойно жить и его праправнуку. С тех пор музыкальная тема в семье стала табу. Мигель обнаруживает, что между ним и его любимым певцом Эрнесто де ла Крусом, ныне покойным, существует некая связь. Паренёк отправляется к своему кумиру в Страну Мёртвых, где встречает души предков. Мигель знакомится там с духом-скелетом по имени Гектор, который становится его проводником. Вдвоём они отправляются на поиски де ла Круса."
            ),
            Film(
                "Властелин колец: Возвращение короля (2003)",
                R.drawable.film_6,
                "Повелитель сил тьмы Саурон направляет свою бесчисленную армию под стены Минас-Тирита, крепости Последней Надежды. Он предвкушает близкую победу, но именно это мешает ему заметить две крохотные фигурки — хоббитов, приближающихся к Роковой Горе, где им предстоит уничтожить Кольцо Всевластья."
            ),
            Film(
                "Интерстеллар (2014)",
                R.drawable.film_7,
                "Когда засуха, пыльные бури и вымирание растений приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется сквозь червоточину (которая предположительно соединяет области пространства-времени через большое расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека и найти планету с подходящими для человечества условиями."
            ),
            Film(
                "Криминальное чтиво (1994)",
                R.drawable.film_8,
                "Несколько связанных историй из жизни бандитов. Шедевр Квентина Тарантино, который изменил мировое кино"
            ),
            Film(
                "Бойцовский клуб (1999)",
                R.drawable.film_9,
                "Страховой работник разрушает рутину своей благополучной жизни. Культовая драма по книге Чака Паланика"
            ),
            Film(
                "Властелин колец: Братство Кольца (2001)",
                R.drawable.film_10,
                "Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет. Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу."
            ),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
    }
}