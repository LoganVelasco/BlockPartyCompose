package logan.blockpartycompose.ui.screens.levelBuilder

import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject


@HiltViewModel
class LevelBuilderViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableStateFlow<LevelBuilderState?>(null)
    val state: StateFlow<LevelBuilderState?> = _state

    lateinit var level: Level

    var saved = false


    fun isInProgress(): Boolean {
        if (_state.value!!.isEdit) {
            return _state.value!!.blocks != level.initialBlocks
        }
        _state.value!!.blocks.toMutableList().forEach {
            if (it != '.' && !saved) return true
        }
        return  false
    }

    fun setupNewLevel(x: Int = 6, y: Int = 8) {
        level = repo.getNewLevel(x, y)
        _state.value =
            LevelBuilderState(level.blocks)

    }

    fun colorSelected(selectedBlockColor: BlockColor) {
        _state.value =
            LevelBuilderState(_state.value!!.blocks, selectedBlockColor, isEdit = _state.value!!.isEdit)

    }

    fun blockClicked(block: Char, index: Int) {
        val color = _state.value!!.selectedBlockColor
        if (color != null) {
            val blocks = _state.value!!.blocks.toMutableList()
            blocks[index] = color.color
            saved = false
            _state.value =
                LevelBuilderState(blocks, color, isEdit =  _state.value!!.isEdit)

        }
    }

    fun clearAllClicked() {
        setupNewLevel()
    }

    fun menuClicked() {

    }

    fun playClicked() {
        level = Level(
            id = 0,
            name = "",
            levelSet = LevelSet.CUSTOM,
            x = 6,
            y = 8,
            initialBlocks = _state.value!!.blocks,
            minMoves = 0
        )
    }

    fun triggerSaveDialog() {
        val blocks = _state.value!!.blocks.toMutableList()
        _state.value =
            LevelBuilderState(blocks, selectedBlockColor = null, saved = true, isEdit = _state.value!!.isEdit)

    }

    fun saveClicked(context: Context, name: String) { // bad logic shouldn't need to pass blocks
        val newLevel = Level(
            level.id,
            name,
            level.levelSet,
            level.x,
            level.y,
            _state.value!!.blocks.toMutableList(),
            level.minMoves,
        )

        repo.addCustomLevel(newLevel, context)
        saved = true
    }

    fun showPopUpDialog() { // bad logic shouldn't need to pass blocks
        val blocks = _state.value!!.blocks.toMutableList()
        _state.value =
            LevelBuilderState(blocks, null, true, isEdit = _state.value!!.isEdit)

    }


    fun hidePopUpDialog() {
        val blocks = _state.value!!.blocks.toMutableList()
        _state.value =
            LevelBuilderState(blocks, isEdit = _state.value!!.isEdit)

    }

    fun setupExistingLevel(existingLevel: Level) {
        level = existingLevel
        _state.value =
            LevelBuilderState(blocks = level.blocks, isEdit = true)

    }

    @Immutable
    data class LevelBuilderState(
        val blocks: List<Char>,
        var selectedBlockColor: BlockColor? = null,
        val showDialog: Boolean = false,
        val saved: Boolean = false,
        val isEdit: Boolean = false
    )
}