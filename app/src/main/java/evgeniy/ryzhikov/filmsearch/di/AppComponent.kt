package evgeniy.ryzhikov.filmsearch.di

import dagger.Component
import evgeniy.ryzhikov.filmsearch.di.modules.DatabaseModule
import evgeniy.ryzhikov.filmsearch.di.modules.DomainModule
import evgeniy.ryzhikov.filmsearch.di.modules.RemoteModule
import evgeniy.ryzhikov.filmsearch.viewmodel.HomeFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component (
    //Внедряем ВСЕ модули
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    //метод для того что бы внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}