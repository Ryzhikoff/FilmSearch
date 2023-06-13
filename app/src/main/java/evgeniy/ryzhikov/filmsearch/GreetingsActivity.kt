package evgeniy.ryzhikov.filmsearch

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import evgeniy.ryzhikov.filmsearch.databinding.ActivityGreetingsBinding

class GreetingsActivity : AppCompatActivity() {
    lateinit var binding: ActivityGreetingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAndStartGreetingsAnim()
    }

    private fun initAndStartGreetingsAnim() {
        val lottieAnimationView: LottieAnimationView = binding.greetingsLottieAnim
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                val intent = Intent(this@GreetingsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

        lottieAnimationView.playAnimation()
    }
}