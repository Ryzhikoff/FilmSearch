package evgeniy.ryzhikov.filmsearch.di

import dagger.Component
import evgeniy.ryzhikov.filmsearch.di.modules.DatabaseModule
import evgeniy.ryzhikov.filmsearch.di.modules.DomainModule
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import evgeniy.ryzhikov.filmsearch.viewmodel.SettingsFragmentViewModel
import evgeniy.ryzhikov.remote_module.RemoteProvider
import javax.inject.Singleton

@Singleton
@Component (
    //Внедряем ВСЕ модули
    dependencies = [RemoteProvider::class],
    modules = [
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    //метод для того что бы внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)

    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}