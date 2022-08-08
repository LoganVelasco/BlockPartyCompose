package logan.blockpartycompose.ui.screens.level

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext
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

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    lateinit var level: Level

    fun setupLevel(levelSet: LevelSet, id: Int) {
        level = getLevel(levelSet, id)
        level.resetLevel()
        _state.postValue(
            LevelState(
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
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

//    private fun getLevels(levelSet: LevelSet, context:Context): List<Level> {
//        return repo.getLevels(levelSet, context)
//    }

    private fun getLevel(levelSet: LevelSet, id: Int): Level {
        return repo.levelsSets[levelSet.name]!!.first { it.id == id }
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
            return
        }

        when (block) {
            'r' -> {
                level.state = GameState.FAILED
            }
            'g' -> {
                if (handleGreenMove(index)) {
                    moveBlue(index)
                } else {
                    return
                }
            }
            'y' -> {
                viewModelScope.launch {
                    delay(450)
                    if (level.state != GameState.FAILED)
                        level.state = GameState.SUCCESS

                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = _state.value!!.movesUsed + 1,
                            gameState = level.state,
                        )
                    )
                }
                return
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
                movesUsed = _state.value!!.movesUsed + 1,
                gameState = level.state,
            )
        )

        if (shouldRedAttemptMove()) {
            handleRedTurn()
        }
    }

    private fun handleRedTurn() {
        if (moveRed() && level.state == GameState.IN_PROGRESS) {
            viewModelScope.launch {
                delay(100)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state,
                    )
                )

                if (level.state == GameState.IN_PROGRESS && moveRed()) {
                    delay(175)
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks.toMutableList(),
                            movesUsed = _state.value!!.movesUsed,
                            gameState = level.state
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
        if (isInSameColumn()) {
            if (isRedAboveBlue()) {
                if (!moveRed(Direction.UP)) {
                    return false
                }
            } else {
                if (!moveRed(Direction.DOWN)) {
                    return false
                }
            }
        } else if (isRedRightOfBlue()) {
            if (!moveRed(Direction.LEFT)) {
                if (isInSameRow()) {
                    return false
                }
                if (isRedAboveBlue()) {
                    if (!moveRed(Direction.UP)) {
                        return false
                    }
                } else {
                    if (!moveRed(Direction.DOWN)) {
                        return false
                    }
                }
            }
        } else {
            if (!moveRed(Direction.RIGHT)) {
                if (isInSameRow()) {
                    return false
                }
                if (isRedAboveBlue()) {
                    if (!moveRed(Direction.UP)) {
                        return false
                    }
                } else {
                    if (!moveRed(Direction.DOWN)) {
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun isInSameRow() = (level.redIndex / level.x) == (level.blueIndex / level.x)

    private fun isRedRightOfBlue() = level.redIndex % level.x > level.blueIndex % level.x

    private fun isRedAboveBlue() = level.redIndex > level.blueIndex

    private fun isInSameColumn() = level.redIndex % level.x == level.blueIndex % level.x

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


        if (newIndex == level.blueIndex) { // Red reached Blue
            viewModelScope.launch {
                moveRedToNewIndex(newIndex)
                delay(250)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state
                    )
                )
                delay(250)
                level.state = GameState.FAILED
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state
                    )
                )
            }
            return false
        }


        if (level.redIndex == level.goalIndex) { // Red moves off of Yellow
            moveRedOffYellow(newIndex)
            return true
        }

        if (isValidRedMove(newIndex)) {
            moveRedToNewIndex(newIndex)
            return true
        }
        return false
    }

    private fun moveRedOffYellow(newIndex: Int) {
        level.blocks[level.redIndex] = 'y'
        level.blocks[newIndex] = 'r'
        level.redIndex = newIndex
    }

    private fun moveRedToNewIndex(newIndex: Int) {
        level.blocks[level.redIndex] = '.'
        level.blocks[newIndex] = 'r'
        level.redIndex = newIndex
    }

    private fun isValidRedMove(newIndex: Int): Boolean {
        return when (_state.value!!.blocks[newIndex]) {
            '.' -> true
            'b' -> true
            'y' -> true
            else -> false
        }
    }

    private fun shouldRedAttemptMove(): Boolean {
        return isRedBlockPresent() && level.state == GameState.IN_PROGRESS
    }

    private fun isRedBlockPresent() = level.redIndex != -1

    fun tryAgain() {
        level.resetLevel()
        _state.postValue(
            LevelState(
                blocks = level.blocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    private fun getMinMoves(): Int {
        return level.minMoves
    }

    fun getStars(movesUsed: Int): Int {
        return if (movesUsed <= getMinMoves()) 3 else if (movesUsed - 2 <= getMinMoves()) 2 else 1
    }
}

@Immutable
data class LevelState(
    val blocks: List<Char>,
    var movesUsed: Int,
    var gameState: GameState,
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