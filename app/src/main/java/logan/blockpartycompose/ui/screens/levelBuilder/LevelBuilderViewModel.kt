package logan.blockpartycompose.ui.screens.levelBuilder

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.BlockType
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import javax.inject.Inject


@HiltViewModel
class LevelBuilderViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelBuilderState>()
    val state: LiveData<LevelBuilderState> = _state

    lateinit var level: Level
    private val history = mutableListOf<List<Char>>()

    private var saved = false

    fun isInProgress(): Boolean {
        if (level.id != -1) {
            return _state.value!!.blocks != level.initialBlocks
        }
        _state.value!!.blocks.toMutableList().forEach {
            if (it != EMPTY_BLOCK && !saved) return true
        }
        return false
    }

    fun setupNewLevel(x: Int = 6, y: Int = 8) {
        level = repo.getNewLevel(x, y)
        history.clear()
        history.add(level.blocks.toList())
        _state.postValue(
            LevelBuilderState(level.blocks)
        )
    }

    fun colorSelected(selectedBlockType: BlockType) {
        _state.postValue(
            LevelBuilderState(_state.value!!.blocks, selectedBlockType, isEdit = level.id != -1)
        )
    }

    // Block parameter needed for level grid onClick, but is unused for level builder
    fun blockClicked(@Suppress("UNUSED_PARAMETER") block: Char, index: Int) {
        val color = _state.value!!.selectedBlockType
        if (color != null) {
            val blocks = _state.value!!.blocks.toMutableList()
            blocks[index] = color.type
            saved = false
            history.add(blocks.toList())
            _state.postValue(
                LevelBuilderState(blocks, color, isEdit = level.id != -1)
            )
        }
    }

    fun clearAllClicked() {
        level.blocks = repo.getEmptyLayout().toMutableList()
        history.add(level.blocks.toList())
        _state.postValue(
            LevelBuilderState(level.blocks)
        )
    }

    fun undoClicked() {
        if (history.size < 2) return
        history.removeAt(history.size-1)
        level.blocks = history.last().toMutableList()
        _state.postValue(
            LevelBuilderState(
                history.last(),
                _state.value?.selectedBlockType,
                isEdit = level.id != -1
            )
        )
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
            LevelBuilderState(
                blocks,
                selectedBlockType = null,
                saved = true,
                isEdit = level.id != -1
            )
        )
    }

    fun saveClicked(
        context: Context,
        name: String = level.name
    ) { // bad logic shouldn't need to pass blocks
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
        history.clear()
        history.add(level.blocks.toList())
        _state.postValue(
            LevelBuilderState(blocks = level.blocks, isEdit = true)
        )
    }

    fun setupExistingLevel(id: Int, context: Context) {
        repo.getCustomLevels(context).find { level -> level.id == id }
            ?.let { setupExistingLevel(it) }
    }

    @Immutable
    data class LevelBuilderState(
        val blocks: List<Char>,
        var selectedBlockType: BlockType? = null,
        val showDialog: Boolean = false,
        val saved: Boolean = false,
        val isEdit: Boolean = false
    )
}