package logan.blockpartycompose.ui.screens.level

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
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    var compositions = 0

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    private var _moves = MutableLiveData<LevelState>()
    val moves: LiveData<LevelState> = _moves

    private lateinit var level: Level

    fun setupLevel(levelSet: LevelSet, name: Int) {
        level = getLevel(levelSet, name)
        level.resetLevel()
        _state.postValue(
            LevelState(
                name = level.name,
                x = level.x,
                blocks = level.initialBlocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    fun setupLevel(newLevel: Level) {
        level = newLevel
        _state.postValue(
            LevelState(
                blocks = newLevel.blocks,
                x = newLevel.x,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS,
                name = newLevel.name
            )
        )
    }

    fun getLevels(levelSet: LevelSet): List<Level> {
        return repo.getLevels(levelSet)
    }

    private fun getLevel(levelSet: LevelSet, name: Int): Level {
        return getLevels(levelSet).first { it.name == name }
    }

    fun updateLevel(difficulty: LevelSet, name: Int, stars: Int) {
        repo.updateLevelProgress(difficulty, name, stars)
    }

    fun blockClicked(block: Char, index: Int) {
        if (!GameUtils.isTouchingBlue(
                index,
                level.blueIndex,
                level.x
            )
        ) {
            val i = 0
            return
        }
        val i = 0
        when (block) {
            'r' -> {
                level.state = GameState.FAILED
            }
            'g' -> {
                if (!handleGreenMove(index)) {
                    return
                } else {
                    moveBlue(index)
                }
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

        _state.postValue(
            LevelState(
                blocks = level.blocks,
                x = state.value!!.x,
                movesUsed = _state.value!!.movesUsed + 1,
                gameState = level.state,
                name = level.name,
            )
        )

        if (shouldRedAttemptMove()) {
            handleRedTurn()
        }
    }

    private fun handleRedTurn() {
        if (moveRed() && level.state == GameState.IN_PROGRESS) {
            viewModelScope.launch {
                delay(250)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        x = level.x,
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state,
                        level.name,
                    )
                )

                if (level.state == GameState.IN_PROGRESS && moveRed()) {
                    delay(250)
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks.toMutableList(),
                            x = level.x,
                            movesUsed = _state.value!!.movesUsed,
                            gameState = level.state,
                            level.name,
                        )
                    )
                }
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
            'b' -> true
            else -> false
        }
    }

    private fun shouldRedAttemptMove(): Boolean {
        return level.redIndex != -1 && level.state == GameState.IN_PROGRESS
    }

    fun tryAgain() {
        level.resetLevel()
        _state.postValue(
            LevelState(
                blocks = level.blocks,
                x = level.x,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS,
                name = level.name,
            )
        )
    }

    fun getMinMoves(): Int {
        return  level.minMoves
    }
}


@Immutable
data class LevelState(
    val blocks: List<Char>,
    val x: Int,
    var movesUsed: Int,
    var gameState: GameState,
    val name: Int,
)

enum class GameState {
    FAILED,
    SUCCESS,
    IN_PROGRESS
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}