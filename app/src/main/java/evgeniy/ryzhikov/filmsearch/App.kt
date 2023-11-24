package evgeniy.ryzhikov.filmsearch

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import evgeniy.ryzhikov.filmsearch.di.AppComponent
import evgeniy.ryzhikov.filmsearch.di.DaggerAppComponent
import evgeniy.ryzhikov.filmsearch.di.modules.DatabaseModule
import evgeniy.ryzhikov.filmsearch.di.modules.DomainModule
import evgeniy.ryzhikov.filmsearch.view.notifications.NotificationsConstants.CHANNEL_ID
import evgeniy.ryzhikov.remote_module.DaggerRemoteComponent


class App : Application() {
    lateinit var dagger: AppComponent
    var isPromoShown = false

    override fun onCreate() {
        super.onCreate()
        //инициализируем App
        instance = this
        //создаем компонент
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Имя, описание и важность канала
            val name = "Watch Later Channel"
            val description = "FilmSearch notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            //создаем канала передав его ИД, имя, важность
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            //описание отдельно
            channel.description = description
            //получаем доступ к менеджеру нотификаций
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //регистрируем канал
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        //здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }
}