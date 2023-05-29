package evgeniy.ryzhikov.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.android.material.snackbar.Snackbar
import evgeniy.ryzhikov.filmsearch.databinding.ActivityMainBinding
import evgeniy.ryzhikov.filmsearch.fragments.DetailsFragment
import evgeniy.ryzhikov.filmsearch.fragments.FavoritesFragment
import evgeniy.ryzhikov.filmsearch.fragments.HomeFragment
import evgeniy.ryzhikov.filmsearch.recycler_view.Film

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        startFragments()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorite -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentPlaceholder, FavoritesFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.watch_later -> makeSnakebar(getString(R.string.main_menu_button_watch_later))
                R.id.selections -> makeSnakebar(getString(R.string.main_menu_button_selection))
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun makeSnakebar(message: String): Boolean {
        Snackbar.make(binding.bottomNavigation, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.bottomNavigation)
            .show()
        return true
    }

    private fun startFragments() {
        //Запускаем фрагмент при старте
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentPlaceholder, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    fun launchDetailsFragment(film: Film) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount == 1) {
                    exitDoubleTap()
                } else {
                    super.setEnabled(false)
                    onBackPressedDispatcher.onBackPressed()
                }
                super.setEnabled(true)
            }
        }

    private fun exitDoubleTap() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish()
        } else {
            makeSnakebar(getString(R.string.alert_double_tap_exit))
        }
        backPressed = System.currentTimeMillis()
    }

    companion object {
        const val TIME_INTERVAL = 2000
    }
}