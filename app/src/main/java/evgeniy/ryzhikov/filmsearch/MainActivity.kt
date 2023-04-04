package evgeniy.ryzhikov.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMenuButtons()
    }

    private fun initMenuButtons() {
        val layout = findViewById<LinearLayout>(R.id.main_menu_buttons)
        layout.children.forEach {
            it.setOnClickListener {
                val button = it as Button
                createToast(button.text.toString())
            }
        }
    }

    private fun createToast (message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}