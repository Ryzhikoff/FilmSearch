package evgeniy.ryzhikov.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import evgeniy.ryzhikov.filmsearch.App
import evgeniy.ryzhikov.filmsearch.domain.Interactor
import javax.inject.Inject

class SettingsFragmentViewModel: ViewModel() {

    //инжектим интерактор
    @Inject
    lateinit var interactor: Interactor
    val categoryPropertyLifeData: MutableLiveData<String> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        //полуаем категория при инициализации
        getCategoryProperty()
    }

    private fun getCategoryProperty() {
        categoryPropertyLifeData.value = interactor.getDefaultCategoryFromPreference()
    }

    fun putCategoryProperty(category: String) {
        //сохраняем в натсройки
        interactor.saveDefaultCategoryToPreference(category)
        //и передаем в модель
        getCategoryProperty()
    }
}