package logan.blockpartycompose.ui.screens.level

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    fun setupLevel(levelSet: LevelSet, name: String) {
        val level = getLevel(levelSet, name)
        _state.postValue(
            LevelState(
                level = level,
                blocks = level.blocks,
                movesUsed = 0
            )
        )
    }

    fun getLevels(levelSet: LevelSet): List<Level> {
        return repo.getLevels(levelSet)
    }

    fun getLevel(levelSet: LevelSet, name: String): Level {
        return getLevels(levelSet).first { it.name == name }
    }

    fun blockClicked(block: Char) {
        when (block) {
            'r' -> {

            }
            'g' -> {

            }
            'y' -> {

            }
            '.' -> {

            }
        }



        _state.postValue(
            LevelState(
            level = _state.value!!.level,
            blocks = _state.value!!.blocks,
            movesUsed = _state.value!!.movesUsed + 1
        ))
    }

}

@Immutable
data class LevelState(
    val level: Level,
    val blocks: List<Char>,
    var movesUsed: Int,
)