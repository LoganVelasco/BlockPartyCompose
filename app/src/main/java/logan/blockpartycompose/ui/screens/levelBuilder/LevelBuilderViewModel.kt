package logan.blockpartycompose.ui.screens.levelBuilder

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject


@HiltViewModel
class LevelBuilderViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelBuilderState>()
    val state: LiveData<LevelBuilderState> = _state

    lateinit var level: Level


    fun isInProgress(): Boolean {
        _state.value!!.blocks.toMutableList().forEach {
            if (it != '.') return true
        }
        return false
    }

    fun setupNewLevel(x: Int = 6, y: Int = 8) {
        level = repo.getNewLevel(x, y)
        _state.postValue(
            LevelBuilderState(level.blocks)
        )
    }

    fun colorSelected(selectedBlockColor: BlockColor) {
        _state.postValue(
            LevelBuilderState(_state.value!!.blocks, selectedBlockColor)
        )
    }

    fun blockClicked(block: Char, index: Int) {
        val color = _state.value!!.selectedBlockColor
        if (color != null) {
            val blocks = _state.value!!.blocks.toMutableList()
            blocks[index] = color.color
            _state.postValue(
                LevelBuilderState(blocks, color)
            )
        }
    }

    fun clearAllClicked() {
        setupNewLevel()
    }

    fun menuClicked() {

    }

    fun playClicked() {
        level = Level(
            name = 0,
            levelSet = LevelSet.CUSTOM,
            x = 6,
            y = 8,
            initialBlocks = _state.value!!.blocks,
            minMoves = 0
        )
    }

    fun saveClicked() {
        level.blocks = _state.value!!.blocks.toMutableList()
    }

    fun showPopUpDialog() { // bad logic shouldn't need to pass this
        val blocks = _state.value!!.blocks.toMutableList()
        _state.postValue(
            LevelBuilderState(blocks, null, true)
        )
    }

    fun hidePopUpDialog() {
        val blocks = _state.value!!.blocks.toMutableList()
        _state.postValue(
            LevelBuilderState(blocks)
        )
    }

    @Immutable
    data class LevelBuilderState(
        val blocks: List<Char>,
        var selectedBlockColor: BlockColor? = null,
        val showDialog: Boolean? = null
    )
}