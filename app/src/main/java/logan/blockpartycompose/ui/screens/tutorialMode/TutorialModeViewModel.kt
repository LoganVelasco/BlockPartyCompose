package logan.blockpartycompose.ui.screens.tutorialMode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.level.LevelState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject

@HiltViewModel
class TutorialModeViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<Int>()
    val state: LiveData<Int> = _state

    fun getTutorialProgress() {
        _state.value = repo.getTutorialProgress()
    }

    fun updateTutorialProgress(progress: Int) {
        repo.updateTutorialProgress(progress)
    }

    fun getLevel(): Level {
        if(_state.value!! > 6)
            return repo.getLevel(LevelSet.EASY, 1)
        else if(_state.value!! > 6)
            return repo.getLevel(LevelSet.EASY, 2)
        return repo.getLevel(LevelSet.EASY, 3)
    }

}