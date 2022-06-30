package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.utils.GameUtils
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    fun getLevels(levelSet: LevelSet): List<Level> {
        return repo.getLevels(levelSet)
    }

    fun getProgress(levelSet: LevelSet): List<Int> {
        return repo.getLevelsProgress(levelSet)
    }
}

