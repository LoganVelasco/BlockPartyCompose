package logan.blockpartycompose.ui.screens.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    var state by Delegates.notNull<Int>()

    private var _hintState = MutableLiveData(getCurrentHintState())
    val hintState: LiveData<Boolean> = _hintState

    private var _customColor = MutableLiveData<Color>()
    val customColor: LiveData<Color> = _customColor

    private var _restartApp = MutableLiveData(false)
    val restartApp: LiveData<Boolean> = _restartApp

    init {
        state = repo.getColorScheme()
    }

    fun updateColorScheme(colorScheme: Int) {
        repo.updateColorScheme(colorScheme)
        _restartApp.value = true
    }

    fun setCustomColor(color: Color){
        _customColor.value = color
    }

    fun hintSwitchOnClick(state: Boolean) {
        repo.updateHintPreference(state)
        _hintState.value = state
    }

    fun unlockAllLevels(){
        val easyList = 0..9
        val mediumList = 10..14
        val hardList = 15..19
        easyList.forEach{
            repo.updateLevelProgress(LevelSet.EASY, it, 3)
        }
        mediumList.forEach{
            repo.updateLevelProgress(LevelSet.MEDIUM, it, 3)
        }
        hardList.forEach{
            repo.updateLevelProgress(LevelSet.HARD, it, 3)
        }
    }


    private fun getCurrentHintState(): Boolean {
        return repo.getHintPreference()
    }

}