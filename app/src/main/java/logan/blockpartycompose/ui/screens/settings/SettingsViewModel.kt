package logan.blockpartycompose.ui.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    var state by Delegates.notNull<Int>()
    private var _restartApp = MutableLiveData(false)
    val restartApp: LiveData<Boolean> = _restartApp

    init {
        state = repo.getColorScheme()
    }

    fun updateColorScheme(colorScheme: Int) {
        repo.updateColorScheme(colorScheme)
        _restartApp.value = true
    }

}