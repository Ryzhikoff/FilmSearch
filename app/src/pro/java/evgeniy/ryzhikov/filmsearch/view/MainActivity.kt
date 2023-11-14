import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.databinding.ActivityMainBinding
import evgeniy.ryzhikov.filmsearch.view.fragments.DetailsFragment
import evgeniy.ryzhikov.filmsearch.view.fragments.FavoritesFragment
import evgeniy.ryzhikov.filmsearch.view.fragments.HomeFragment
import evgeniy.ryzhikov.filmsearch.view.fragments.SelectionsFragment
import evgeniy.ryzhikov.filmsearch.view.fragments.WatchLaterFragment
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.services.LowBatteryReceiver
import evgeniy.ryzhikov.filmsearch.view.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private var receiver: LowBatteryReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        startFragments()
        startLowBatteryBroadcastReceiver()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment(fragment ?: HomeFragment(), tag)
                    true
                }

                R.id.favorite -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: FavoritesFragment(), tag)
                    true
                }

                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: WatchLaterFragment(), tag)
                    true
                }

                R.id.selections -> {
                    val tag = "selections"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SelectionsFragment(), tag)
                    true
                }

                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SettingsFragment(), tag)
                    true
                }

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
            .commit()

    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitDoubleTap()
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

    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment, tag)
            .commit()
    }

    private fun startLowBatteryBroadcastReceiver() {
        receiver = LowBatteryReceiver()
        val filters = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        filters.addAction(Intent.ACTION_BATTERY_LOW)
        registerReceiver(receiver, filters)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }
}