package evgeniy.ryzhikov.filmsearch.view.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.view.MainActivity
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationsConstants.CHANNEL_ID
import evgeniy.ryzhikov.remote_module.entity.ApiConstants

object NotificationHelper {

    private const val REQUEST_CODE = 111222
    fun createNotification(context: Context, film: Film) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_movie_24)
            setContentTitle(context.resources.getString(R.string.remember_watch))
            setContentText(film.title)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

//        val notificationManager = NotificationManagerCompat.from(context)
        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        Glide.with(context)
            //указываем битмап
            .asBitmap()
            .load(ApiConstants.IMAGE_URL + "w500" + film.poster)
            .into(object  : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                //когда успешно получили битмап
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //создаем нотификацию в стиле big picture
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    //обновляем нотификацию
                    notificationManager.notify(film.id, builder.build())
                }

            })
        //Отправляем изначальную нотификацию в стандартном исполнении
        notificationManager.notify(film.id, builder.build())
    }
}