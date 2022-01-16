package logan.blockpartycompose.ui.screens.levelBuilder

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.utils.GeneratorService
import javax.inject.Inject
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2


@HiltViewModel
class LevelBuilderViewModel @Inject constructor(
    private val repo: DataRepository,
    private val levelGenerator: GeneratorService
) : ViewModel() {

    private var _state = MutableLiveData<LevelBuilderState>()
    val state: LiveData<LevelBuilderState> = _state

    val levelBuilderOnClicks = LevelBuilderOnClicks(
        backOnClicked = {
            clearAllClicked()
        },
        blockOnClicked = this::blockClicked,
        colorOnClicked = this::colorSelected,
        playOnClicked = this::playClicked,
        menuOnClicked = this::menuClicked,
        saveOnClicked = this::saveClicked,
        clearAllOnClicked = this::clearAllClicked
    )

    lateinit var level: Level

    fun setupNewLevel(x: Int = 6, y: Int = 8) {
        level = repo.getNewLevel(x, y)
        _state.postValue(
            LevelBuilderState(
                x = level.x,
                blocks = level.blocks
            )
        )
    }

    fun colorSelected(selectedBlockColor: BlockColor) {
        _state.postValue(
            LevelBuilderState(_state.value!!.blocks, selectedBlockColor, x = level.x)
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

    fun blockClicked(block: Char, index: Int) {
        val color = _state.value!!.selectedBlockColor
        if (color != null) {
            val blocks = _state.value!!.blocks.toMutableList()
            blocks[index] = color.color
            _state.postValue(
                LevelBuilderState(blocks, color, level.x)
            )
        }
    }

    fun clearAllClicked() {
        setupNewLevel()
    }

    fun menuClicked() {
        viewModelScope.launch {
            val generatedLevel = levelGenerator.generateLevel()
            level.blocks = generatedLevel
            _state.postValue(
                LevelBuilderState(blocks = generatedLevel, x = level.x)
            )
        }

    }

    fun saveClicked() {
        level.blocks = _state.value!!.blocks.toMutableList()
    }

    @Immutable
    data class LevelBuilderState(
        val blocks: List<Char>,
        var selectedBlockColor: BlockColor? = null,
        val x: Int
    )

    @Immutable
    class LevelBuilderOnClicks(
        val backOnClicked: () -> Unit,
        val blockOnClicked: KFunction2<Char, Int, Unit>,
        val colorOnClicked: KFunction1<BlockColor, Unit>,
        val menuOnClicked: () -> Unit,
        val playOnClicked: () -> Unit,
        val saveOnClicked: () -> Unit,
        val clearAllOnClicked: () -> Unit,
    )
}