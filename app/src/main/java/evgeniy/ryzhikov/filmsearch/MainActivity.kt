package evgeniy.ryzhikov.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
    }

    private fun initNavigation() {

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    createToast(getString(R.string.main_menu_button_setting))
                    true
                }
                else -> false
            }
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bnMainMenu)
        bottomNavigation.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.favorite -> createToast(getString(R.string.main_menu_button_wounded))
                R.id.watch_later -> createToast(getString(R.string.main_menu_button_watch_later))
                R.id.selections -> createToast(getString(R.string.main_menu_button_selection))
            }
        }
    }
    private fun createToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}