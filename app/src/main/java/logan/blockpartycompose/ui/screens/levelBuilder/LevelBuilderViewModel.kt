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

    private var _state = MutableLiveData<LevelBuilderState>()
    val state: LiveData<LevelBuilderState> = _state

    lateinit var level: Level

    var saved = false


    fun isInProgress(): Boolean {
        _state.value!!.blocks.toMutableList().forEach {
            if (it != '.' && !saved) return true
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
            saved = false
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
        _state.postValue(
            LevelBuilderState(blocks, selectedBlockColor = null, saved = true)
        )
    }

    fun saveClicked(context: Context, name: String) { // bad logic shouldn't need to pass blocks
//        level.blocks = _state.value!!.blocks.toMutableList()

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

    fun setupExistingLevel(existingLevel: Level) {
        level = existingLevel
        _state.postValue(
            LevelBuilderState(blocks = level.blocks, isEdit =  true)
        )
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