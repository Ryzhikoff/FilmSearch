package evgeniy.ryzhikov.filmsearch.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import evgeniy.ryzhikov.filmsearch.data.entity.Film
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationHelper
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationsConstants

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val bundle = intent.getBundleExtra(NotificationsConstants.FILM_BUNDLE_KEY)

            val film = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(NotificationsConstants.FILM_KEY, Film::class.java)
            } else {
                bundle?.get(NotificationsConstants.FILM_KEY) as Film
            }

            NotificationHelper.createNotification(context, film!!)
        }
    }
}