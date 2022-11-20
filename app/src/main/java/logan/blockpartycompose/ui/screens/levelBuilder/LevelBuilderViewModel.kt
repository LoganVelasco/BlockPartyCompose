package logan.blockpartycompose.ui.screens.levelBuilder

import android.content.Context
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

    var saved = false

    fun isInProgress(): Boolean {
        if (level.id != -1) {
            return _state.value!!.blocks != level.initialBlocks
        }
        _state.value!!.blocks.toMutableList().forEach {
            if (it != '.' && !saved) return true
        }
        return  false
    }

    fun setupNewLevel(x: Int = 6, y: Int = 8) {
        level = repo.getNewLevel(x, y)
        _state.postValue(
            LevelBuilderState(level.blocks)
        )
    }

    fun colorSelected(selectedBlockColor: BlockColor) {
        _state.postValue(
            LevelBuilderState(_state.value!!.blocks, selectedBlockColor, isEdit = level.id != -1)
        )
    }

    fun blockClicked(block: Char, index: Int) {
        val color = _state.value!!.selectedBlockColor
        if (color != null) {
            val blocks = _state.value!!.blocks.toMutableList()
            blocks[index] = color.color
            saved = false
            _state.postValue(
                LevelBuilderState(blocks, color, isEdit =  level.id != -1)
            )
        }
    }

    fun clearAllClicked() {
        level.blocks = repo.getEmptyLayout().toMutableList()
        _state.postValue(
            LevelBuilderState(level.blocks)
        )
    }

    fun menuClicked() {

    }

    fun playClicked() {
        level = Level(
            id = level.id,
            name = level.name,
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
            LevelBuilderState(blocks, selectedBlockColor = null, saved = true, isEdit = level.id != -1)
        )
    }

    fun saveClicked(context: Context, name: String = level.name) { // bad logic shouldn't need to pass blocks
        if (level.id == -1)
            level.id = repo.generateId(context)

        level.name = name
        val newLevel = Level(
            id = level.id,
            name = name,
            levelSet = level.levelSet,
            x = level.x,
            y = level.y,
            initialBlocks = level.blocks.toMutableList(),
            minMoves = level.minMoves,
        )
        level = newLevel
        repo.addCustomLevel(newLevel, context)
        saved = true
    }

    fun saveNewLevel() {
        level.id = -1
        triggerSaveDialog()
    }

    fun showPopUpDialog() { // bad logic shouldn't need to pass blocks
        val blocks = _state.value!!.blocks.toMutableList()
        _state.postValue(
            LevelBuilderState(blocks, null, true, isEdit = level.id != -1)
        )
    }


    fun hidePopUpDialog() {
        val blocks = _state.value!!.blocks.toMutableList()
        _state.postValue(
            LevelBuilderState(blocks, isEdit = level.id != -1)
        )
    }

    fun setupExistingLevel(existingLevel: Level) {
        level = existingLevel
        _state.postValue(
            LevelBuilderState(blocks = level.blocks, isEdit = true)
        )
    }

    fun setupExistingLevel(id: Int, context:Context) {
        repo.getCustomLevels(context).find { level -> level.id == id }
            ?.let { setupExistingLevel(it) }
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