package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.utils.GameUtils
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    private lateinit var level: Level


    fun setupLevel(levelSet: LevelSet, name: String) {
        level = getLevel(levelSet, name)

        _state.postValue(
            LevelState(
                level = level,
                blocks = level.blocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    fun getLevels(levelSet: LevelSet): List<Level> {
        return repo.getLevels(levelSet)
    }

    private fun getLevel(levelSet: LevelSet, name: String): Level {
        return getLevels(levelSet).first { it.name == name }
    }

    fun blockClicked(block: Char, index: Int) {
        if (!GameUtils.isTouchingBlue(index, level.blueIndex, level.x)) return
        when (block) {
            'r' -> {
                level.state = GameState.FAILED
            }
            'g' -> {
                if (!handleGreenMove(index)) return
                else moveBlue(index)
            }
            'y' -> {
                level.state = GameState.SUCCESS
            }
            '.' -> {
                moveBlue(index)
            }
            else -> {
                return
            }
        }

        handleRedTurn()

        _state.postValue(
            LevelState(
                level = _state.value!!.level,
                blocks = level.blocks,
                movesUsed = _state.value!!.movesUsed + 1,
                gameState = level.state
            )
        )
    }

    private fun handleRedTurn() {
        if (level.redIndex != -1 && level.state == GameState.IN_PROGRESS) {
            if (moveRed() && level.state == GameState.IN_PROGRESS) {
                moveRed()
            }
        }
    }

    private fun moveBlue(index: Int) {
        level.blocks[index] = 'b'
        level.blocks[level.blueIndex] = '.'
        level.blueIndex = index
    }

    private fun handleGreenMove(index: Int): Boolean {
        val difference = (index - level.blueIndex)
        val newIndex = index + difference

        if (newIndex < 0 || newIndex > level.blocks.size - 1) return false

        if (GameUtils.isEdge(index, level.x) && GameUtils.isEdge(
                newIndex,
                level.x
            ) && difference.absoluteValue == 1
        ) return false

        return when (level.blocks[newIndex]) {
            '.' -> {
                level.blocks[newIndex] = 'g'
                true
            }
            'g' -> {
                level.blocks[newIndex] = '.'
                true
            }
            else -> {
                false
            }
        }
    }

    private fun moveRed(): Boolean {
        if (level.redIndex % level.x == level.blueIndex % level.x) {
            if (level.redIndex > level.blueIndex) {
                if (!moveRed(Direction.UP)) return false
            } else {
                if (!moveRed(Direction.DOWN)) return false
            }
        } else if (level.redIndex % level.x > level.blueIndex % level.x) {
            if (!moveRed(Direction.LEFT)) {
                if ((level.redIndex / level.x) == (level.blueIndex / level.x)) return false
                if (level.redIndex > level.blueIndex) {
                    if (!moveRed(Direction.UP)) return false
                } else {
                    if (!moveRed(Direction.DOWN)) return false
                }
            }
        } else {
            if (!moveRed(Direction.RIGHT)) {
                if ((level.redIndex / level.x) == (level.blueIndex / level.x)) return false
                if (level.redIndex > level.blueIndex) {
                    if (!moveRed(Direction.UP)) return false
                } else {
                    if (!moveRed(Direction.DOWN)) return false
                }
            }
        }

        return true
    }

    private fun moveRed(direction: Direction): Boolean {
        val newIndex = when (direction) {
            Direction.UP -> {
                level.redIndex - level.x
            }
            Direction.DOWN -> {
                level.redIndex + level.x
            }
            Direction.LEFT -> {
                level.redIndex - 1
            }
            Direction.RIGHT -> {
                level.redIndex + 1
            }
        }

        if (newIndex == level.blueIndex) {
            level.state = GameState.FAILED
            return true
        }

        if (isValidRedMove(newIndex)) {

            level.blocks[level.redIndex] = '.'
            level.blocks[newIndex] = 'r'
            level.redIndex = newIndex

            return true
        }
        return false
    }

    private fun isValidRedMove(newIndex: Int): Boolean {
        return when (_state.value!!.blocks[newIndex]) {
            '.' -> true
            else -> false
        }
    }

    fun tryAgain() {
        level.resetLevel()
        _state.postValue(
            LevelState(
                level = level,
                blocks = level.blocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }
}


@Immutable
data class LevelState(
    val level: Level,
    val blocks: List<Char>,
    var movesUsed: Int,
    var gameState: GameState
)

enum class GameState() {
    FAILED,
    SUCCESS,
    IN_PROGRESS
}

enum class Direction() {
    UP,
    DOWN,
    LEFT,
    RIGHT
}