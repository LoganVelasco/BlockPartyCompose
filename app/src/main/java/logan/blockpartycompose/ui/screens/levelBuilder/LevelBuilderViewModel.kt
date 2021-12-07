package logan.blockpartycompose.ui.screens.levelBuilder

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.utils.GeneratorService
import javax.inject.Inject


@HiltViewModel
class LevelBuilderViewModel @Inject constructor(
    private val repo: DataRepository,
    private val levelGenerator: GeneratorService
) : ViewModel() {

    private var _state = MutableLiveData<LevelBuilderState>()
    val state: LiveData<LevelBuilderState> = _state

    lateinit var level: Level

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

    fun clearAllClicked(){
        setupNewLevel()
    }

    fun menuClicked() {
        val generatedLevel = levelGenerator.generateLevel()

        _state.postValue(
            LevelBuilderState(generatedLevel)
        )
    }

    fun playClicked() {
        level = Level(
            name = "",
            x = 6,
            y = 8,
            initialBlocks = _state.value!!.blocks,
        )
    }

    fun saveClicked() {
        level.blocks = _state.value!!.blocks.toMutableList()
    }

    @Immutable
    data class LevelBuilderState(
        val blocks: List<Char>,
        var selectedBlockColor: BlockColor? = null
    )
}