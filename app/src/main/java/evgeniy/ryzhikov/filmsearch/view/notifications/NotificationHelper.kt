package evgeniy.ryzhikov.filmsearch.view.notifications

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.services.ReminderBroadcast
import evgeniy.ryzhikov.filmsearch.view.MainActivity
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationsConstants.CHANNEL_ID
import evgeniy.ryzhikov.remote_module.entity.ApiConstants
import java.util.Calendar

object NotificationHelper {

    private const val REQUEST_CODE = 111222
    private const val REQUEST_CODE_ALARM = 123
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
            .into(object : CustomTarget<Bitmap>() {
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

    fun notificationSet(context: Context, film: Film) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            context,
            //второй параметр конструктора DatePickerDialog - слушатель выбора даты
            { _, year, month, day ->

                //слушатель выбора времени
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    //создаем новый Calendar, в него кладем полученные данные и переводим в unixtime
                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(year, month, day, hour, minute, 0)
                    val dateTimeInMillis = pickedDateTime.timeInMillis

                    //вызываем метод который вызовет Alarm
                    createWatchLaterEvent(context, dateTimeInMillis, film)
                }

                //вызываем диалог выбора времени, передавая ранее сосзданный слушатель
                TimePickerDialog(context, timeSetListener, currentHour, currentMinute, true).show()

            },

            //остальные параметры констрауктора DatePickerDialog
            currentYear, currentMonth, currentDay
        )
            //показываем DatePickerDialog
            .show()
    }

    private fun createWatchLaterEvent(context: Context, dataTimeInMillis: Long, film: Film) {
        //получаем досуп к AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //intent для апуска BroadcastReceiver в bundle кладем film
        val intent = Intent(film.title, null, context, ReminderBroadcast()::class.java).apply {
            val bundle = Bundle()
            bundle.putParcelable(NotificationsConstants.FILM_KEY, film)
            putExtra(NotificationsConstants.FILM_BUNDLE_KEY, bundle)
        }

        //pendingIntent Для запуска извне приложения
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //устанавиваем Alarm
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dataTimeInMillis,
            pendingIntent
        )
    }

}