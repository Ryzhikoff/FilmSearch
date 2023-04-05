package evgeniy.ryzhikov.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import evgeniy.ryzhikov.filmsearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    private fun initNavigation() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> makeSnakebar(getString(R.string.main_menu_button_setting))
                else -> false
            }
        }
        //setOnItemReselectedListener
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorite -> makeSnakebar(getString(R.string.main_menu_button_wounded))
                R.id.watch_later -> makeSnakebar(getString(R.string.main_menu_button_watch_later))
                R.id.selections -> makeSnakebar(getString(R.string.main_menu_button_selection))
                else -> return@setOnItemSelectedListener false
            }
        }
    }
    private fun makeSnakebar(message : String) : Boolean {
        Snackbar.make(binding.topAppBar,message, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.bottomNavigation)
            .show()
        return true
    }
}